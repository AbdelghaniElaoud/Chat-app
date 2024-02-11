import entities.User;
import org.hibernate.Session;
import util.HibernateUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JDialog{
    private JTextField tfEmail;
    private JButton btnOk;
    private JButton btnCancel;
    private JPanel loginPanel;
    private JPasswordField pfPassword;
    private JButton btnRegister;

    private Session session;

    public User user;

    public LoginForm(JFrame parent){
        super(parent);

        this.session = HibernateUtil.getSessionFactory().getCurrentSession();

        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Electro-Market.ma\\IdeaProjects\\JakartaEESchoolManagement\\src\\main\\resources\\icon.png");
        setIconImage(icon);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnOk.addActionListener(e -> {
            String email = tfEmail.getText();
            String password = String.valueOf(pfPassword.getPassword());

            user = getAuthenticatedUser(email, password);

            if (user != null && !user.isActive()) {

                JOptionPane.showMessageDialog(LoginForm.this,
                        "Your account has been deactivated",
                        "Contact the admin",
                        JOptionPane.ERROR_MESSAGE);

            }else if (user != null ){
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginForm.this,
                        "Email or password is invalid",
                        "Please try again / Contact the admin",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*showLoginForm();*/
                RegistrationForm registrationForm = new RegistrationForm(null);

            }
        });
        setVisible(true);
    }

    private void showLoginForm() {

    }


    /**
     *
     * @param email
     * @param password
     *  : This method checks the credentials and gives the access
     */
    private User getAuthenticatedUser(String email, String password) {
        User user = null;

        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();


            User user1 = (User) session.createQuery("from User where email like :email")
                    .setParameter("email", "%" + email + "%").uniqueResult();


            User userWeFind = user1;



            if (userWeFind != null && userWeFind.getPassword().equals(password) ) {
                user = userWeFind;
                /*System.out.println("There is a user");*/
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }



        return user;
    }

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
    }
}

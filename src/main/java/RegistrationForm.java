import dao.UserDaoImpl;
import entities.User;
import org.hibernate.Session;
import util.HibernateUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationForm extends JDialog{
    private JTextField tfPhone;
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JTextField tfName;
    private JPanel registrePanel;

    private Session session;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }

    public RegistrationForm(JFrame parent){
        super(parent);

        this.session = HibernateUtil.getSessionFactory().getCurrentSession();

        setTitle("Create a new account");
        setContentPane(registrePanel);
        setMinimumSize(new Dimension(470,474));
        setModal(true);
        setLocationRelativeTo(parent);
        Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Electro-Market.ma\\IdeaProjects\\JakartaEESchoolManagement\\src\\main\\resources\\icon.png");
        setIconImage(icon);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    /**
     * Register the user
     */
    private void registerUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phoneNumber = tfPhone.getText();
        String password = pfPassword.getText();
        String confirmPassword = pfConfirmPassword.getText();

        if (name.isEmpty() || email.isEmpty() ||phoneNumber.isEmpty() ||password.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter all the fields","Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!validate(email)){
            JOptionPane.showMessageDialog(this, "Email is not valid","Change it please!!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)){
            JOptionPane.showMessageDialog(this, "Confirm Passowrd doesn't match","Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserDaoImpl userDao = new UserDaoImpl();

        if (!userDao.exists(email)){
            user = userDao.createUser(name,email,phoneNumber,"",password);
        }else {
            JOptionPane.showMessageDialog(this, "User already exists!!","Change information", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (user != null){
            dispose();
        }else {
            JOptionPane.showMessageDialog(this, "Failed to register new user","Try again", JOptionPane.ERROR_MESSAGE);
        }
    }

    public User user;


    public static void main(String[] args) {
        RegistrationForm registrationForm = new RegistrationForm(null);
    }

}

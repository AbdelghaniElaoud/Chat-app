import entities.User;
import org.hibernate.Session;
import util.HibernateUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminBoard extends JFrame{
    private JPanel adminPanel;
    private JButton btnUsers;
    private JButton btnProfil;
    private JButton disconnectButton;

    private Session session;
    private User user;

    public AdminBoard(User user) {
        this.user = user;
        this.session = HibernateUtil.getSessionFactory().getCurrentSession();
        setTitle("Dashboard");
        setContentPane(adminPanel);
        setMinimumSize(new Dimension(450,474));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                DashboardForm dashboardForm = new DashboardForm();
            }
        });
        btnProfil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProfileForm profileForm = new ProfileForm(null,user);
            }
        });
        btnUsers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserManagementTable userManagementTable = new UserManagementTable(new JFrame());
            }
        });
    }


    public static void main(String[] args) {
        AdminBoard adminBoard = new AdminBoard(null);
    }
}

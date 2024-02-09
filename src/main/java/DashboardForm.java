import dao.UserDaoImpl;
import entities.Conversation;
import entities.User;
import entities.UserInvitationRequest;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DashboardForm extends JFrame {
    private JPanel dashboardPanel;
    private JLabel lbAdmin;
    private JButton btnProfile;
    private JButton friendRequestsButton;
    private JButton btnConversations;
    private JButton disconnectButton;
    private JButton sendFriendRequestsButton;

    private Session session;

    public User user;

    public DashboardForm() {
        this.session = HibernateUtil.getSessionFactory().getCurrentSession();
        setTitle("Dashboard");
        setContentPane(dashboardPanel);
        setMinimumSize(new Dimension(450,474));
        setSize(450,474);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        boolean hasRegisteredUsers = connectToDatabase();

        if (hasRegisteredUsers) {
            // Show the LoginForm
            LoginForm loginForm = new LoginForm(this);
            user = loginForm.user;

            if (user != null) {
                if (!user.getRole().equals("admin")) {
                    lbAdmin.setText(user.getName());
                    setLocationRelativeTo(null);
                    setVisible(true);
                } else {
                    AdminBoard adminBoard = new AdminBoard(user);
                }
            } else {
                dispose();
            }
        } else {
            // Show the registration form
            RegistrationForm registrationForm = new RegistrationForm(this);
            User user = registrationForm.user;

            if (user != null) {
                lbAdmin.setText(user.getName());
                setLocationRelativeTo(null);
                setVisible(true);
            } else {
                dispose();
            }
        }

        btnProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ProfileForm profileForm = new ProfileForm(null,user);

            }
        });

        friendRequestsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFriendRequestsDialog(user);
            }
        });
        btnConversations.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserDaoImpl userDao = new UserDaoImpl();
                List<Conversation> conversations = userDao.getUserConversations(user.getUserId());

                /*for (Conversation conversation: conversations){
                    System.out.println(conversation);
                }*/

                ConversationsForm conversationsForm = new ConversationsForm(conversations, user);
            }
        });
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                DashboardForm dashboardForm = new DashboardForm();
            }
        });
        sendFriendRequestsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserDaoImpl userDao = new UserDaoImpl();

                SendFriendRequestsUI sendFriendRequestsUI = new SendFriendRequestsUI(userDao.getTheNonFriendsOfUserById(user.getUserId()), user);
            }
        });
    }

    private void showFriendRequestsDialog(User user) {

        UserDaoImpl userDao = new UserDaoImpl();

        List<UserInvitationRequest> invitationRequest = getTheUnacceptedInvitations();

        List<String> invitations  = new ArrayList<>();
        this.session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();

        for (int i = 0 ; i < invitationRequest.size() ; i++){

            invitations.add(session.find(User.class,invitationRequest.get(i).getInvitationRequest().getSenderId()).getName());
        }

        session.getTransaction().commit();

        FriendRequestUI friendRequestUI = new FriendRequestUI(invitations, user);
    }

    private boolean connectToDatabase() {
        boolean hasRegisteredUsers = false;

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            Query<Long> userCountQuery = session.createQuery("SELECT COUNT(u) FROM User u", Long.class);
            Long userCount = userCountQuery.uniqueResult();

            if (userCount > 0) {
                hasRegisteredUsers = true;
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Handle or log the exception as needed
        }

        return hasRegisteredUsers;
    }

    private List<UserInvitationRequest> getTheUnacceptedInvitations(){
        UserDaoImpl userDao = new UserDaoImpl();
        List<UserInvitationRequest> invitationRequests = userDao.checkInvitations(user.getUserId());

        List<UserInvitationRequest> copy = new ArrayList<>();

        for (UserInvitationRequest invitationRequest : invitationRequests){
            if (!invitationRequest.getInvitationRequest().isAccepted()){
                copy.add(invitationRequest);
            }
        }

        return copy;
    }

    public static void main(String[] args) {

        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (UnsupportedLookAndFeelException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }

        DashboardForm dashboardForm = new DashboardForm();

        UserDaoImpl userDao = new UserDaoImpl();
        final String mail = "admin@admin.com";
        if (!userDao.exists(mail)){
            userDao.createUser("admin", mail,"+2424526623","admin","admin");
        }
    }

}
import dao.UserDaoImpl;
import entities.User;
import entities.UserInvitationRequest;
import org.hibernate.Session;
import util.HibernateUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestUI extends JFrame {

    private JList<String> friendRequestsList;
    private DefaultListModel<String> friendRequestsModel;

    private Session session;
    private User user;

    public FriendRequestUI(List<String> friendRequests, User user) {
        this.session = HibernateUtil.getSessionFactory().getCurrentSession();
        setTitle("Friend Requests");
        setSize(300, 200);
        Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Electro-Market.ma\\IdeaProjects\\JakartaEESchoolManagement\\src\\main\\resources\\icon.png");
        setIconImage(icon);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        friendRequestsModel = new DefaultListModel<>();
        friendRequestsList = new JList<>(friendRequestsModel);

        JButton acceptButton = new JButton("Accept");
        JButton rejectButton = new JButton("Reject");
        JButton backButton = new JButton("Back");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle accepting friend request
                int selectedIndex = friendRequestsList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedRequest = friendRequestsModel.getElementAt(selectedIndex);
                    UserDaoImpl userDao = new UserDaoImpl();
                    List<UserInvitationRequest> invitationRequests = getTheUnacceptedInvitations(user);
//                    session = HibernateUtil.getSessionFactory().getCurrentSession();

                    /*for (UserInvitationRequest invitationRequest : invitationRequests) {
                        session.getTransaction().begin();
                        if (session.find(User.class,invitationRequest.getInvitationRequest().getSenderId()).getName().equals(selectedRequest)) {
                            userDao.acceptOrDeclineAnInvitation(invitationRequest.getId(), true);
                        }
                        session.getTransaction().commit();
                    }*/

                    for (UserInvitationRequest invitationRequest : invitationRequests) {
                        if (!session.isOpen()) {
                            session = HibernateUtil.getSessionFactory().getCurrentSession();
                        }

                        if (!session.getTransaction().isActive()) {
                            session.getTransaction().begin();
                            boolean var = session.find(User.class, invitationRequest.getInvitationRequest().getSenderId()).getName().equals(selectedRequest);
                            User theAcceptedUser = session.find(User.class, invitationRequest.getInvitationRequest().getSenderId());
                            session.getTransaction().commit();

                            if (var) {
                                userDao.acceptOrDeclineAnInvitation(invitationRequest.getId(), true);
                                userDao.createConversation(user,theAcceptedUser);
                            }


                        }
                    }



                    friendRequestsModel.removeElement(selectedRequest);
                }
            }
        });

        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle rejecting friend request
                int selectedIndex = friendRequestsList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedRequest = friendRequestsModel.getElementAt(selectedIndex);
                    UserDaoImpl userDao = new UserDaoImpl();
                    List<UserInvitationRequest> invitationRequests = getTheUnacceptedInvitations(user);


                    //The reject button logic
                    for (UserInvitationRequest invitationRequest : invitationRequests) {
                        if (!session.isOpen()) {
                            session = HibernateUtil.getSessionFactory().getCurrentSession();
                        }

                        if (!session.getTransaction().isActive()) {
                            session.getTransaction().begin();
                            boolean var = session.find(User.class, invitationRequest.getInvitationRequest().getSenderId()).getName().equals(selectedRequest);
                            session.getTransaction().commit();

                            // Your logic here
                            if (var) {
                                userDao.acceptOrDeclineAnInvitation(invitationRequest.getId(), false);
                            }


                        }
                    }



                    friendRequestsModel.removeElement(selectedRequest);
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(backButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(friendRequestsList), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Populate the initial friend requests
        for (String request : friendRequests) {
            friendRequestsModel.addElement(request);
        }
        setVisible(true);
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserDaoImpl userDao = new UserDaoImpl();
            List<UserInvitationRequest> invitations = userDao.checkInvitations(1L);


            List<String> friendRequests = new ArrayList<>();
            for(int i=0 ; i < invitations.size() ; i++){
                friendRequests.add(invitations.get(i).getUser().getName());
            }


            FriendRequestUI friendRequestUI = new FriendRequestUI(friendRequests);
            friendRequestUI.setVisible(true);
        });
    }*/


    private List<UserInvitationRequest> getTheUnacceptedInvitations(User user){
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
}

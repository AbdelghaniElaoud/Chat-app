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

public class SendFriendRequestsUI extends JFrame {

    private JList<String> friendRequestsList;
    private DefaultListModel<String> friendRequestsModel;

    private Session session;
    private User user;

    public SendFriendRequestsUI(List<User> nonFriendsList, User user) {
        this.session = HibernateUtil.getSessionFactory().getCurrentSession();
        setTitle("Friend Requests");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        friendRequestsModel = new DefaultListModel<>();
        friendRequestsList = new JList<>(friendRequestsModel);

        JButton addFriendButton = new JButton("Add friend");
        JButton backButton = new JButton("Back");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        addFriendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle accepting friend request
                int selectedIndex = friendRequestsList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedRequest = friendRequestsModel.getElementAt(selectedIndex);
                    UserDaoImpl userDao = new UserDaoImpl();
                    Long receiverId = userDao.getUserIdByName(selectedRequest);

                    userDao.sendInvitation(user.getUserId(), receiverId);

                    friendRequestsModel.removeElement(selectedRequest);
                }



            }
        });



        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addFriendButton);
        buttonPanel.add(backButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(friendRequestsList), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Populate the initial friend requests
        for (User user1 : nonFriendsList) {
            friendRequestsModel.addElement(user1.getName());
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

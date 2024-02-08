import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

class FriendRequestsDialog extends JDialog {
    private JPanel friendRequestsPanel;
    private JList<String> friendRequestsList;
    private JButton btnAccept;
    private JButton btnRefuse;
    private JButton btnBack;

    public FriendRequestsDialog(JFrame parent) {
        super(parent, "Friend Requests", true);

        // Initialize components
        friendRequestsPanel = new JPanel();
        friendRequestsList = new JList<>();
        btnAccept = new JButton("Accept");
        btnRefuse = new JButton("Refuse");
        btnBack = new JButton("Back");

        // Set layout manager for the dialog
        friendRequestsPanel.setLayout(new BorderLayout());

        // Simulated friend requests
        List<String> friendRequests = List.of("Friend1", "Friend2", "Friend3");
        friendRequestsList.setListData(friendRequests.toArray(new String[0]));

        // Add components to the dialog's content pane
        friendRequestsPanel.add(new JScrollPane(friendRequestsList), BorderLayout.CENTER);
        friendRequestsPanel.add(btnAccept, BorderLayout.NORTH);
        friendRequestsPanel.add(btnRefuse, BorderLayout.SOUTH);
        friendRequestsPanel.add(btnBack, BorderLayout.EAST);

        setContentPane(friendRequestsPanel);

        // Set listeners for buttons
        btnAccept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle accept action
                // You can implement your logic here
            }
        });

        btnRefuse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle refuse action
                // You can implement your logic here
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the dialog
            }
        });

        // Set dialog properties
        setSize(400, 300);
        setLocationRelativeTo(parent);
    }
}

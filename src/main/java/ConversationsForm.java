import dao.UserDaoImpl;
import entities.Conversation;
import entities.Message;
import entities.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ConversationsForm extends JDialog{
    public static final String SPACES = "                                                                                          ";
    private JList<String> jListConversations;
    private JPanel mainPanel;
    private JTextArea tAreaMessages;
    private JTextArea tAreaInput;
    private JButton btnSend;
    private DefaultListModel<String> conversationsListModel;

    private User user;

    private UserDaoImpl userDao;


    private Conversation conversation;

    public ConversationsForm(List<Conversation> conversations, User user) {
        initComponents();
        this.user = user;
        this.userDao = new UserDaoImpl();

        setUserConversations(conversations);
        addListSelectionListener(); // Add ListSelectionListener
        // Do something with the user ID if needed
    }

    private void initComponents() {
        JFrame frame = new JFrame("Conversations Form");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);


        conversationsListModel = new DefaultListModel<>();
        jListConversations = new JList<>(conversationsListModel);
        tAreaMessages = new JTextArea();
        tAreaMessages.setEditable(false);
        tAreaInput = new JTextArea();
        btnSend = new JButton("Send");
        mainPanel = new JPanel();


        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Set layout
        mainPanel.setLayout(new BorderLayout());

        // Set preferred size for the JList
        jListConversations.setPreferredSize(new Dimension(150, 400));

        // Adjust the font of the elements
        // Custom cell renderer for the JList
        jListConversations.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                // Customize the font or other properties as needed
                // For example, increase the font size:
                setFont(getFont().deriveFont(Font.PLAIN, 14));
                return renderer;
            }
        });
        // End of adjusting the font

        mainPanel.add(new JScrollPane(jListConversations), BorderLayout.WEST);
        mainPanel.add(new JScrollPane(tAreaMessages), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JScrollPane(tAreaInput), BorderLayout.CENTER);
        inputPanel.add(btnSend, BorderLayout.EAST);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);


        mainPanel.setPreferredSize(new Dimension(800, 600));


        frame.setContentPane(mainPanel);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void sendMessage() {
        String message = tAreaInput.getText().trim();
        if (isMessageEmpty(message)) {
            JOptionPane.showMessageDialog(ConversationsForm.this, "The message should not be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        UserDaoImpl userDao = new UserDaoImpl();

        Conversation conversation = userDao.getConversationByNameAndUser(this.conversation.getName(), user.getUserId());
        if (conversation == null){
            conversation = userDao.getConversationByNameAndUser(user.getName(), user.getUserId());
        }
        userDao.sendMessage(user.getUserId(), conversation.getConversationId(), message,null);
        // TODO: Implement logic to send the message
        tAreaMessages.append(SPACES + user.getName() + " : " + message + "\n");
        tAreaInput.setText("");
    }

    private boolean isMessageEmpty(String message) {
        return message == null || message.trim().isEmpty();
    }

    private void addListSelectionListener() {
        jListConversations.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // When the user finishes selecting an item
                int selectedIndex = jListConversations.getSelectedIndex();
                String selectedName = conversationsListModel.getElementAt(selectedIndex);



                Conversation conversation = userDao.getConversationByNameAndUser(selectedName, user.getUserId());
                if (conversation == null){
                    conversation = userDao.getConversationByNameAndUser(user.getName(), user.getUserId());
                }
                this.conversation = conversation;

                if (selectedIndex != -1) {
                    String selectedConversation = conversationsListModel.getElementAt(selectedIndex);
                    // Retrieve the messages for the selected conversation
                    List<String> messages = new ArrayList<>();
                    if (conversation != null && user.getUserId() != null){

                        List<Message> messages1 = userDao.getMessagesOfConversation(conversation.getConversationId());
                        for (Message message : messages1){
                            String value;
                            final String usernameById = userDao.getUsernameById(message.getSenderId());
                            if(user.getName().equals(usernameById)){
                                 value = SPACES + usernameById + " : " + message.getTextType();
                            }else {
                                 value = usernameById + " : " + message.getTextType();
                            }
                            messages.add(value);
                        }
                    }

                    // Display the messages in the tAreaMessages
                    displayMessages(messages);
                }
            }
        });
    }


    private void displayMessages(List<String> messages) {
        tAreaMessages.setText(""); // Clear existing messages
        for (String message : messages) {
            tAreaMessages.append(message + "\n");
        }
    }

    public void setUserConversations(List<Conversation> userConversations) {
        // Clear existing conversations
        conversationsListModel.clear();

        UserDaoImpl userDao = new UserDaoImpl();

        // Add user conversations to the list model
        for (Conversation conversation : userConversations) {
            String value = conversation.getName();
            for (User user1 : userDao.getUsersOfConversationByConversationName(value)){
                if (!user1.getName().equals(user.getName())){
                    conversationsListModel.addElement(user1.getName());
                }
            }

        }

        // Set the model to the JList
        jListConversations.setModel(conversationsListModel);

        // Repaint the mainPanel to update the GUI
        mainPanel.repaint();
    }


    public static void main(String[] args) {

    }
}

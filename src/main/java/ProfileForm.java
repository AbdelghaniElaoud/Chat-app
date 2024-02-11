import dao.UserDaoImpl;
import entities.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfileForm extends JDialog{
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JButton btnSubmit;
    private JPanel mainPanel;
    private JButton btnBack;
    private JButton btnChangePassword;
    private User user;

    public ProfileForm(JFrame jFrame, User user) {
        this.user = user;
        setTitle("Profile");
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(490,474));
        setModal(true);
        setLocationRelativeTo(jFrame);
        Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Electro-Market.ma\\IdeaProjects\\JakartaEESchoolManagement\\src\\main\\resources\\icon.png");
        setIconImage(icon);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        init();

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        btnChangePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ChangePasswordForm changePasswordForm = new ChangePasswordForm(user);
            }
        });
        setVisible(true);
    }

    private void init() {
        nameField.setEditable(false);

        // Set the user data to the fields
        if (user != null) {
            nameField.setText(user.getName());
            emailField.setText(user.getEmail());
            phoneField.setText(user.getPhone());
        }

        // Add action listener to the submit button
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the updateProfile method with the entered data
                UserDaoImpl userDao = new UserDaoImpl();
                boolean success = userDao.updateProfile(user.getUserId(),emailField.getText(), phoneField.getText());

                // Display a message based on the result
                if (success) {
                    JOptionPane.showMessageDialog(ProfileForm.this, "Profile updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Close the dialog on success
                } else {
                    JOptionPane.showMessageDialog(ProfileForm.this, "Failed to update profile", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Dummy method for updating the profile (replace with your actual implementation)
    private boolean updateProfile(String email, String phone) {
        // Implement your logic to update the profile here
        // For now, just return true for demonstration purposes
        return true;
    }



    public static void main(String[] args) {
        UserDaoImpl userDao = new UserDaoImpl();
        User user1 = userDao.getUserById(1L);

        ProfileForm profileForm = new ProfileForm(null, user1);
    }
}

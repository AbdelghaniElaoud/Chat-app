import dao.UserDaoImpl;
import entities.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePasswordForm extends JDialog {
    private JButton btnSubmit;
    private JButton btnBack;
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField retypeNewPasswordField;
    private JPanel mainPanel;
    private User user;

    public ChangePasswordForm( User user) {
        this.user = user;
        setTitle("Profile");
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(520,474));
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ProfileForm profileForm = new ProfileForm(null,user);
            }
        });


        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean success = false;


                if (!user.getPassword().equals(String.valueOf(oldPasswordField.getPassword()))) {
                    JOptionPane.showMessageDialog(ChangePasswordForm.this, "The old password is incorrect", "Error", JOptionPane.ERROR_MESSAGE);
                } else {

                    if (!String.valueOf(newPasswordField.getPassword()).equals(String.valueOf(retypeNewPasswordField.getPassword()))) {
                        JOptionPane.showMessageDialog(ChangePasswordForm.this, "You did not retype correctly", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {

                        UserDaoImpl userDao = new UserDaoImpl();
                        success = userDao.updatePassword(user.getUserId(), String.valueOf(newPasswordField.getPassword()));
                    }
                }


                if (success) {
                    JOptionPane.showMessageDialog(ChangePasswordForm.this, "Profile updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(ChangePasswordForm.this, "Failed to update profile", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

}

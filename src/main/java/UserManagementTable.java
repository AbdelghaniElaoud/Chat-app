import dao.UserDaoImpl;
import entities.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserManagementTable extends JFrame{

    JFrame f;
    JTable j;

    UserManagementTable(JFrame jFrame) {

        f = new JFrame();


        f.setTitle("User Management");
        f.setLocationRelativeTo(jFrame);
        f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        UserDaoImpl userDao = new UserDaoImpl();
        List<User> users = userDao.getAllUsersWithoutAdmins();

        // Convert List<User> to Object[][]
        Object[][] data = new Object[users.size()][8];

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data[i][0] = user.getUserId();
            data[i][1] = user.getName();
            data[i][2] = user.getEmail();
            data[i][3] = user.getPhone();
            data[i][4] = user.getPassword();
            data[i][5] = user.getRole();
            data[i][6] = user.isActive();
            data[i][7] = "Activate/Deactivate";
        }


        String[] columnNames = {"Id", "Name", "Email", "Phone", "Password", "Role", "Active", "Action"};

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 7) {
                    return JButton.class;
                }
                return super.getColumnClass(column);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };


        j = new JTable(model);


        j.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        j.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox(), j)); // Pass 'j' here



        JScrollPane sp = new JScrollPane(j);
        f.add(sp);


        f.setSize(850, 300);

        f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        f.setVisible(true);
    }



}


class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText("Activate/Deactivate");
        return this;
    }
}


class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private int clickedRow;
    private JTable table;

    public ButtonEditor(JCheckBox checkBox, JTable table) {
        super(checkBox);
        this.table = table;
        button = new JButton();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Object idValue = table.getValueAt(clickedRow, 0);
                UserDaoImpl userDao = new UserDaoImpl();
                userDao.activateOrDeactivateUser((Long) idValue);
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        clickedRow = row;
        button.setText("Activate/Deactivate");
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return "Button Clicked";
    }
}


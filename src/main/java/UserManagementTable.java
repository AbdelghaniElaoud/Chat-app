import dao.UserDaoImpl;
import entities.User;
import util.DisplayUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

import static util.DisplayUtil.ACTION_COLUMN_NUMBER;
import static util.DisplayUtil.ID_COLUMN_NUMBER;

public class UserManagementTable extends JFrame{

    JFrame frame;
    JTable table;

    UserManagementTable(JFrame jFrame) {

        frame = new JFrame();


        frame.setTitle("User Management");
        frame.setLocationRelativeTo(jFrame);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        UserDaoImpl userDao = new UserDaoImpl();
        List<User> users = userDao.getAllUsersWithoutAdmins();

        // Convert List<User> to Object[][]
        Object[][] data = new Object[users.size()][7];

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data[i][0] = user.getUserId();
            data[i][1] = user.getName();
            data[i][2] = user.getEmail();
            data[i][3] = user.getPhone();
            data[i][4] = user.getRole();
            data[i][5] = DisplayUtil.displayState(user);
            data[i][6] = DisplayUtil.displayButtonText(user);
        }


        String[] columnNames = {"Id", "Name", "Email", "Phone", "Role", "State", "Action"};

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == ACTION_COLUMN_NUMBER) {
                    return JButton.class;
                }
                return super.getColumnClass(column);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == ACTION_COLUMN_NUMBER;
            }
        };


        table = new JTable(model);


        table.getColumnModel().getColumn(ACTION_COLUMN_NUMBER).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(ACTION_COLUMN_NUMBER).setCellEditor(new ButtonEditor(new JCheckBox(), table)); // Pass 'table' here



        JScrollPane sp = new JScrollPane(table);
        frame.add(sp);


        frame.setSize(850, 300);

        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        frame.setVisible(true);
    }


}


class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((String) table.getValueAt(row, ACTION_COLUMN_NUMBER));
        return this;
    }
}


class ButtonEditor extends DefaultCellEditor {
    private final JButton button;
    private int clickedRow;

    public ButtonEditor(JCheckBox checkBox, JTable table) {
        super(checkBox);
        button = new JButton();
        button.setText(((String) table.getValueAt(clickedRow, DisplayUtil.STATE_COLUMN_NUMBER)));
        button.addActionListener(e -> {

            Object idValue = table.getValueAt(clickedRow, ID_COLUMN_NUMBER);
            UserDaoImpl userDao = new UserDaoImpl();
            User savedUser = userDao.activateOrDeactivateUser((Long) idValue);
            fireEditingStopped();
            table.setValueAt(DisplayUtil.displayState(savedUser), clickedRow, ACTION_COLUMN_NUMBER);
            table.setValueAt(DisplayUtil.displayButtonText(savedUser), clickedRow, DisplayUtil.STATE_COLUMN_NUMBER);
            button.setText(DisplayUtil.displayButtonText(savedUser));
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        clickedRow = row;
        return button;
    }
}


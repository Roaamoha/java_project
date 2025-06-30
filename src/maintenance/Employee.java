package maintenance;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;

public class Employee extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private String employeeUsername;
    private String employeeType;
    private Thread requestUpdaterThread;
    // كونستركتور يستقبل اسم المستخدم
    public Employee(String username) {
        this.employeeUsername = username;
        setEmployeeType();
        initUI();
        loadRequests();
        requestUpdaterThread = new Thread(new RequestUpdater(this));
        requestUpdaterThread.start();
    }

    @Override
public void dispose() {
    if (requestUpdaterThread != null && requestUpdaterThread.isAlive()) {
        requestUpdaterThread.interrupt(); // إيقاف الخيط
    }
    super.dispose();
}
    
    private void setEmployeeType() {
        HashMap<String, String> userTypeMap = new HashMap<>();
        userTypeMap.put("user_electrical", "كهرباء");
        userTypeMap.put("user_plumbing", "سباكة");
        userTypeMap.put("user_paint", "دهانات وتجديد");
        userTypeMap.put("user_electronics", "صيانة إلكترونيات");

        employeeType = userTypeMap.getOrDefault(employeeUsername.toLowerCase().trim(), "");
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("طلبات الصيانة");

        JLabel jLabel1 = new JLabel("طلبات الصيانة لموظف: " + employeeType);
        jLabel1.setFont(new Font("Arial", Font.BOLD, 20));
        jLabel1.setForeground(Color.decode("#6C63FF"));

        model = new DefaultTableModel(new String[]{"الرقم", "اسم المستخدم", "رقم الهاتف", "الوصف", "النوع", "العمليات"}, 0);
        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        table.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        table.getTableHeader().setBackground(Color.decode("#6C63FF"));
        table.getTableHeader().setForeground(new Color(108, 99, 255));
        table.setSelectionBackground(new Color(230, 230, 250));
        table.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(table);

        JButton refreshButton = new JButton("تحديث البيانات");
        refreshButton.setBackground(Color.decode("#6C63FF"));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.addActionListener(e -> refreshRequests());

        JButton saveButton = new JButton("حفظ نسخة");
        saveButton.setBackground(Color.decode("#6C63FF"));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.addActionListener(e -> saveRequestsToFile());

        table.getColumn("العمليات").setCellRenderer(new ButtonRenderer());
        table.getColumn("العمليات").setCellEditor(new ButtonEditor(new JCheckBox()));

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1)
                        .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 700, GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(refreshButton)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(saveButton)))
                    .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(refreshButton)
                        .addComponent(saveButton))
                    .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void loadRequests() {
        model.setRowCount(0);
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT id, name, phone, description, type FROM requests WHERE type = ? AND status = 0";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, employeeType.trim());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("description"),
                    rs.getString("type"),
                    "اكتمل"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "حدث خطأ أثناء تحميل الطلبات: " + e.getMessage());
        }
    }

   private synchronized void refreshRequests() {
    // لا تحدّث الجدول إذا كان المستخدم يعدّل صفًا حاليًا
    if (table.isEditing()) {
        return;
    }

    // استدعاء دالة تحميل الطلبات من قاعدة البيانات
    loadRequests();
}

    private void startAutoRefresh() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10000); // كل 10 ثواني
                    SwingUtilities.invokeLater(this::refreshRequests);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void saveRequestsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/archives/requests_backup.txt"))) {

            writer.write(String.format("%-10s%-20s%-15s%-40s%-20s", "الرقم", "اسم المستخدم", "رقم الهاتف", "الوصف", "النوع"));
            writer.newLine();
            writer.write("==============================================================================================");
            writer.newLine();

            for (int i = 0; i < model.getRowCount(); i++) {
                String formattedRow = String.format("%-10s%-20s%-15s%-40s%-20s",
                        model.getValueAt(i, 0).toString(),
                        model.getValueAt(i, 1).toString(),
                        model.getValueAt(i, 2).toString(),
                        model.getValueAt(i, 3).toString(),
                        model.getValueAt(i, 4).toString());

                writer.write(formattedRow);
                writer.newLine();
            }

            JOptionPane.showMessageDialog(this, "تم حفظ نسخة من الطلبات في الملف requests_backup.txt");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "خطأ أثناء حفظ النسخة: " + e.getMessage());
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
            setBackground(Color.decode("#6C63FF"));
            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        private JButton button;
        private String label;
        private boolean clicked;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(Color.decode("#6C63FF"));
            button.setForeground(Color.WHITE);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            button.addActionListener((ActionEvent e) -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            clicked = true;
            selectedRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                int requestId = (int) table.getValueAt(selectedRow, 0);
                updateRequestStatus(requestId);
            }
            clicked = false;
            return label;
        }

        private void updateRequestStatus(int requestId) {
            try (Connection con = DatabaseConnection.getConnection()) {
                String updateQuery = "UPDATE requests SET status = 1 WHERE id = ?";
                PreparedStatement ps = con.prepareStatement(updateQuery);
                ps.setInt(1, requestId);
                int result = ps.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(null, "تم تحديث حالة الطلب بنجاح ✅");
                    refreshRequests();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "فشل تحديث حالة الطلب: " + ex.getMessage());
            }
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
    class RequestUpdater implements Runnable {
    private Employee employee;

    public RequestUpdater(Employee employee) {
        this.employee = employee;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(30000); // انتظار 30 ثانية
                SwingUtilities.invokeLater(() -> employee.refreshRequests());
            } catch (InterruptedException e) {
                System.out.println("تم إيقاف خيط التحديث.");
                break;
            }
        }
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}

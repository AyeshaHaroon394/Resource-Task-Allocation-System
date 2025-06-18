import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TaskManagementFrame {

    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private TaskService taskService;
    private Connection conn;
    private TaskService taskService2;
    public TaskManagementFrame(Connection connection, TaskService taskService2, SkillService skillService) throws SQLException {
        this.taskService = new TaskService(connection);
        this.taskService2 = taskService2;
        this.conn=connection;
        initComponents();
        loadTasks();
    }

    private void initComponents() {
        frame = new JFrame("Task Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Column names according to the TaskWithSkills view
        String[] columnNames = {"Task ID", "Task Name", "Skills", "Levels"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make table cells non-editable
                return false;
            }
        };
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete Task");
        deleteButton.addActionListener(this::deleteAction);

        JButton updateButton = new JButton("Update Task");
        updateButton.addActionListener(this::updateAction);

        JButton refresh  = new JButton("Refresh");
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadTasks();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TaskForm(conn);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(refresh);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void loadTasks() throws SQLException {
        List<TaskWithSkills> tasks = taskService.getAllTasksWithSkills(); // Method to fetch data from TaskWithSkills view
        tableModel.setRowCount(0); // Clear the table
        for (TaskWithSkills task : tasks) {
            Object[] row = new Object[]{
                    task.getTaskId(),
                    task.getTaskName(),
                    task.getSkills(),
                    task.getLevels()
            };
            tableModel.addRow(row);
        }
    }

    private void deleteAction(ActionEvent event)
    {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(frame, "Please select a task to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this task?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int taskId = (Integer) tableModel.getValueAt(row, 0);
        try {
            taskService.deleteTaskRequirement(taskId);
            loadTasks();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error deleting task: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAction(ActionEvent event) {
        int row = table.getSelectedRow();
        int taskId = (Integer) tableModel.getValueAt(row, 0);
        new SkillListFrame(conn,taskId);
    }

}

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SkillListFrame extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JButton updateSkillButton;
    private JButton updateTaskButton; // Add the new button
    private List<Skill> skills;
    private SkillService skillService;
    private int selectedSkillId = -1; // Initialize with an invalid ID
    private TaskService taskService;
    private int taskId;

    public SkillListFrame(Connection connection, int taskId) {
        this.taskId = taskId; // Store the taskId
        this.skillService = new SkillService(new SkillDAO(connection));
        this.taskService = new TaskService(connection);

        // Create the JFrame
        setTitle("Skill List");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create the table and table model without the "Experience" column
        tableModel = new DefaultTableModel(new Object[]{"Skill ID", "Language", "Level"}, 0);
        table = new JTable(tableModel);

        // Create the update skill button
        updateSkillButton = new JButton("Update Skill");
        updateSkillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if a skill is selected
                int selectedRow = table.getSelectedRow();
                selectedSkillId = (Integer) table.getValueAt(selectedRow, 0);
                if (selectedSkillId != -1) {
                    // Show the update dialog for skill
                    TaskSkillUpdateDialog dialog = new TaskSkillUpdateDialog(SkillListFrame.this);
                    if (dialog.isUpdateConfirmed()) {
                        String newSkillName = dialog.getNewSkillName();
                        String newLevel = dialog.getNewLevel();
                        try {
                            skillService.updateSkillName(selectedSkillId, newSkillName, newLevel);
                            JOptionPane.showMessageDialog(null, "Skill name and level updated successfully.");
                            loadSkills(taskId); // Reload the skills after the update
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Error updating skill name and level: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a skill to update.");
                }
            }
        });

        // Create the update task name button
        updateTaskButton = new JButton("Update Task Name");
        updateTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show an input dialog to update the task name
                String newTaskName = JOptionPane.showInputDialog("Enter the new task name:");
                if (newTaskName != null && !newTaskName.isEmpty()) {
                    try {
                        taskService.updateTask(taskId, newTaskName);
                        JOptionPane.showMessageDialog(null, "Task name updated successfully.");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error updating task name: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a new task name.");
                }
            }
        });

        // Add components to the JFrame
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateSkillButton);
        buttonPanel.add(updateTaskButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // Load skills data for the specified task
        try {
            loadSkills(taskId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadSkills(int taskId) throws SQLException {
        // Clear the table
        tableModel.setRowCount(0);

        // Load skills data for the specified task
        skills = taskService.getSkillsForTask(taskId);

        // Populate the table with skills data
        for (Skill skill : skills) {
            tableModel.addRow(new Object[]{skill.getId(), skill.getLanguage(), skill.getLevel()});
        }
    }
}

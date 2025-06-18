import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class TaskForm extends JFrame {

    private JLabel nameLabel;
    private JTextField nameField;
    private JButton addButton; // Add button for adding the task
    private JLabel skillsLabel;
    private JTextField skillsField;
    private JLabel levelLabel;
    private JTextField levelField; // Use a text field for comma-separated levels
    private JButton submitButton;

    private TaskDAO taskDAO;
    private SkillDAO skillDAO;

    private boolean isTaskAdded = false; // To track if Task is added
    private int taskid;

    public TaskForm(Connection connection) {
        taskDAO = new TaskDAO(connection);
        skillDAO = new SkillDAO(connection);

        setTitle("Task Form");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        // Create panels for each section
        JPanel namePanel = new JPanel();
        JPanel skillsPanel = new JPanel();
        JPanel levelPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        // Set layouts for the panels
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        skillsPanel.setLayout(new BoxLayout(skillsPanel, BoxLayout.X_AXIS));
        levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.X_AXIS));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        nameLabel = new JLabel("Task Name:");
        nameField = new JTextField(20);
        addButton = new JButton("Add");
        skillsLabel = new JLabel("Skills (Comma-separated):");
        skillsField = new JTextField(20);
        levelLabel = new JLabel("Skill Level (Comma-separated):");
        levelField = new JTextField(20);
        levelField.setEnabled(false);
        submitButton = new JButton("Submit");

        levelField.setEnabled(false); // Initially disabled
        skillsField.setEnabled(false);

        // Add components to panels
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        namePanel.add(addButton);
        skillsPanel.add(skillsLabel);
        skillsPanel.add(skillsField);
        levelPanel.add(levelLabel);
        levelPanel.add(levelField);
        buttonPanel.add(submitButton);

        // Create main panel and set its layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Add panels to the main panel
        mainPanel.add(namePanel);
        mainPanel.add(skillsPanel);
        mainPanel.add(levelPanel);
        mainPanel.add(buttonPanel);

        // Add the main panel to the frame
        add(mainPanel);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitForm();
            }
        });

        nameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });
    }
    private void addTask() {
        String taskName = nameField.getText().trim();

        if (!taskName.isEmpty()) {
            try {
                this.taskid = taskDAO.insertTask(taskName);
                JOptionPane.showMessageDialog(this, "Task added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                nameField.setEnabled(false); // Disable Task Name field after adding the task
                addButton.setEnabled(false); // Disable Add button after adding the task
                isTaskAdded = true; // Set the flag to indicate Task is added
                levelField.setEnabled(true); // Enable Level field
                skillsField.setEnabled(true); // Enable Skills field
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding Task.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter Task Name.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void submitForm() {
        if (!isTaskAdded) {
            JOptionPane.showMessageDialog(this, "Please add Task first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String skillsInput = skillsField.getText().trim();
        String levelInput = levelField.getText().trim();

        try {

            String[] skillsArray = skillsInput.split("\\s*,\\s*");
            String[] levelArray = levelInput.split("\\s*,\\s*");

            if (skillsArray.length != levelArray.length) {
                JOptionPane.showMessageDialog(this, "Number of skills must match number of levels.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (int i = 0; i < skillsArray.length; i++) {
                String skillName = skillsArray[i];
                char skillLevel = levelArray[i].charAt(0); // Get the first character as char

                int skillId = skillDAO.insertSkill(new Skill(skillName, skillLevel));
                taskDAO.insertTaskRequirement(taskid, skillId);
            }

            JOptionPane.showMessageDialog(this, "Task and Requirements added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding Task and Requirements.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        nameField.setText("");
        skillsField.setText("");
        levelField.setText("");
        isTaskAdded = false;
        nameField.setEnabled(true); // Enable Task Name field for a new task
        addButton.setEnabled(true); // Enable Add button for a new task
        levelField.setEnabled(false);
        skillsField.setEnabled(false);
    }
}

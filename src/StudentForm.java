import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class StudentForm extends JFrame {

    private JLabel nameLabel;
    private JTextField nameField;
    private JButton addButton; // Add button for adding the student
    private JLabel skillsLabel;
    private JTextField skillsField;
    private JLabel levelLabel;
    private JTextField levelField; // Use a text field for comma-separated levels
    private JButton submitButton;

    private StudentDAO studentDAO;
    private SkillDAO skillDAO;

    private boolean isStudentAdded = false; // To track if Student is added
    private int studentId;

    public StudentForm(Connection connection) {
        studentDAO = new StudentDAO(connection);
        skillDAO = new SkillDAO(connection);

        setTitle("Student Form");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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

        nameLabel = new JLabel("Student Name:");
        nameField = new JTextField(20);
        addButton = new JButton("Add");
        skillsLabel = new JLabel("Skills (Comma-separated):");
        skillsField = new JTextField(20);
        levelLabel = new JLabel("Experience Level (Comma-separated):");
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
                addStudent();
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
                addStudent();
            }
        });

        setVisible(true);

    }

    private void addStudent() {
        String studentName = nameField.getText().trim();

        if (!studentName.isEmpty()) {
            try {
                this.studentId = studentDAO.insertStudent(studentName);
                JOptionPane.showMessageDialog(this, "Student added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                nameField.setEnabled(false); // Disable Student Name field after adding the student
                addButton.setEnabled(false); // Disable Add button after adding the student
                isStudentAdded = true; // Set the flag to indicate Student is added
                levelField.setEnabled(true); // Enable Level field
                skillsField.setEnabled(true); // Enable Skills field
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding Student.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter Student Name.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void submitForm() {
        if (!isStudentAdded) {
            JOptionPane.showMessageDialog(this, "Please add Student first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String skillsInput = skillsField.getText().trim();
        String levelInput = levelField.getText().trim();

        try {
            String[] skillsArray = skillsInput.split("\\s*,\\s*");
            String[] levelArray = levelInput.split("\\s*,\\s*");


            if (skillsArray.length != levelArray.length) {
                JOptionPane.showMessageDialog(this, "Number of skills must match number of experience years.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (int i = 0; i < skillsArray.length; i++) {
                String skillName = skillsArray[i];
                int skillExperience = Integer.parseInt(levelArray[i]); // Parse the experience as an integer


                int skillId = skillDAO.insertSkill(new Skill(skillName, skillExperience));
                studentDAO.insertStudentSkill(studentId, skillId);
            }

            JOptionPane.showMessageDialog(this, "Student and Skills added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding Student and Skills.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        nameField.setText("");
        skillsField.setText("");
        levelField.setText("");
        isStudentAdded = false;
        nameField.setEnabled(true); // Enable Student Name field for a new student
        addButton.setEnabled(true); // Enable Add button for a new student
        levelField.setEnabled(false);
        skillsField.setEnabled(false);
    }
}

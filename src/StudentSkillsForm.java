import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.NumberFormat;

public class StudentSkillsForm extends JFrame {
    private JTextField studentNameField;
    private JTextField studentSkillsField;
    private JTextField experienceField;
    private SkillDAO skillDAO;
    private StudentSkillsDAO studentSkillsDAO;
    private StudentDAO studentDAO;

    public StudentSkillsForm(StudentDAO studentDAO, SkillDAO skillDAO, StudentSkillsDAO studentSkillsDAO) {
        super("Student Skills Form");
        this.studentDAO=studentDAO;
        this.studentSkillsDAO=studentSkillsDAO;
        this.skillDAO=skillDAO;
        initializeForm();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // Center the form on the screen
    }

    private void initializeForm() {
        setLayout(new GridLayout(0, 2, 5, 5)); // Grid layout with padding

        // Student Name Field
        add(new JLabel("Student Name:"));
        studentNameField = new JTextField(20);
        add(studentNameField);

        // Student Skills Field
        add(new JLabel("Student Skills:"));
        studentSkillsField = new JTextField(20);
        add(studentSkillsField);

        // Experience Field (only accepts positive numbers)
        add(new JLabel("Experience:"));
        //NumberFormat format = NumberFormat.getInstance();
        //format.setGroupingUsed(false); // Remove comma from number greater than 1000
        experienceField = new JTextField();
        experienceField.setColumns(10);
        add(experienceField);

        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitForm();
            }
        });
        add(submitButton);
    }

    private void submitForm() {
        String studentName = studentNameField.getText().trim();
        String skillsInput = studentSkillsField.getText().trim();
        String experienceInput = experienceField.getText().trim();

        String[] skillsArray = skillsInput.split("\\s*,\\s*");
        String[] experiencesArray = experienceInput.split("\\s*,\\s*");

        // Check if the number of skills matches the number of experiences entered
        if (skillsArray.length != experiencesArray.length) {
            JOptionPane.showMessageDialog(this, "The number of skills must match the number of experiences.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Parse experiences into integers and validate
        int[] experiences = new int[experiencesArray.length];
        for (int i = 0; i < experiencesArray.length; i++) {
            try {
                experiences[i] = Integer.parseInt(experiencesArray[i]);
                if (experiences[i] < 0) {
                    JOptionPane.showMessageDialog(this, "Experience values must be non-negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Experience fields must contain only numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        try {
            // Begin transaction
            // Assuming that each DAO manages its own connection, and they are set to not auto-commit
            // Insert the student
            int studentId = studentDAO.insertStudent(studentName);

            // For each skill, insert it and link it to the student
            for (int i = 0; i < skillsArray.length; i++) {
                // Create a new Skill object
                Skill skill = new Skill(skillsArray[i], experiences[i]);
                // Insert the skill
                int skillId = skillDAO.insertSkill(skill);
                // Link the student and skill in the student_skills table
                studentSkillsDAO.insertStudentSkill(studentId, skillId);
            }

            // Commit transaction at each DAO level if required
            // studentDAO.commit(), skillDAO.commit(), studentSkillsDAO.commit();
            JOptionPane.showMessageDialog(this, "Data has been inserted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to insert data into the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            // Handle rollback if necessary
            // studentDAO.rollback(), skillDAO.rollback(), studentSkillsDAO.rollback();
        }
    }
}

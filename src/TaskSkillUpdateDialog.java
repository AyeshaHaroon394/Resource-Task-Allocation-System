import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaskSkillUpdateDialog extends JDialog {
    private JTextField skillNameField;
    private JComboBox<String> levelComboBox; // Use JComboBox for level
    private JButton updateButton;
    private boolean updateConfirmed = false;

    private String newSkillName;
    private String newLevel;

    public TaskSkillUpdateDialog(JFrame parent) {
        super(parent, "Update Skill", true);

        skillNameField = new JTextField(20);
        levelComboBox = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Expert"}); // Add combo box
        updateButton = new JButton("Update");

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newSkillName = skillNameField.getText();
                newLevel = levelComboBox.getSelectedItem().toString(); // Get selected level from combo box
                if (!newSkillName.isEmpty()) {
                    updateConfirmed = true;
                    dispose(); // Close the dialog
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a value for skill name.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("New Skill Name:"));
        panel.add(skillNameField);
        panel.add(new JLabel("New Level:"));
        panel.add(levelComboBox);
        panel.add(updateButton);

        getContentPane().add(panel, BorderLayout.CENTER);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }

    public boolean isUpdateConfirmed() {
        return updateConfirmed;
    }

    public String getNewSkillName() {
        return newSkillName;
    }

    public String getNewLevel() {
        String selectedLevel = newLevel;
        return selectedLevel.toLowerCase().charAt(0) + "";
    }

}

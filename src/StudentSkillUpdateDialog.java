import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import javax.swing.text.NumberFormatter;

public class StudentSkillUpdateDialog extends JDialog {
    private JTextField skillNameField;
    private JFormattedTextField experienceField;
    private JButton updateButton;

    private String newSkillName;
    private Integer newExperience;

    public StudentSkillUpdateDialog(JFrame parent) {
        super(parent, "Update Skill", true);

        skillNameField = new JTextField(20);

        // Create a NumberFormatter to accept only integers for experience
        NumberFormatter formatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0); // Set a minimum value of 0
        experienceField = new JFormattedTextField(formatter);

        updateButton = new JButton("Update");

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newSkillName = skillNameField.getText();
                try {
                    Object value = experienceField.getValue();
                    if (value instanceof Integer) {
                        newExperience = (Integer) value;
                        if (newExperience < 0) {
                            JOptionPane.showMessageDialog(null, "Please enter a non-negative integer for experience.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please enter a valid integer for experience.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    dispose(); // Close the dialog
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid integer for experience.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("New Skill Name:"));
        panel.add(skillNameField);
        panel.add(new JLabel("New Experience:"));
        panel.add(experienceField);
        panel.add(updateButton);

        getContentPane().add(panel, BorderLayout.CENTER);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }

    public String getNewSkillName() {
        return newSkillName;
    }

    public Integer getNewExperience() {
        return newExperience;
    }
}

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class StudentSkillsListFrame extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JButton updateNameButton;
    private JButton updateExperienceButton;
    private List<Skill> skills;
    private StudentService studentService;
    private SkillService skillService;

    private int selectedSkillId = -1;

    public StudentSkillsListFrame(Connection connection, int studentId) {
        this.studentService = new StudentService(new StudentDAO(connection));
        this.skillService = new SkillService(new SkillDAO(connection));

        setTitle("Student Skills List");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel(new Object[]{"Skill ID", "Language", "Level", "Experience"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        table = new JTable(tableModel);

        updateNameButton = new JButton("Update Student Name");
        updateExperienceButton = new JButton("Update Skill");
        updateExperienceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                selectedSkillId = (Integer) table.getValueAt(selectedRow, 0);
                if (selectedSkillId != -1) {
                    StudentSkillUpdateDialog dialog = new StudentSkillUpdateDialog(StudentSkillsListFrame.this);
                    String newSkillName = dialog.getNewSkillName();
                    int newExperience = dialog.getNewExperience();
                    if (newSkillName != null && newExperience >= 0) {
                        try {
                            skillService.updateSkillNameAndExperience(selectedSkillId, newSkillName, newExperience);
                            JOptionPane.showMessageDialog(null, "Skill name and experience updated successfully.");
                            loadSkills(studentId);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Error updating skill name and experience: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a skill to update.");
                }
            }
        });

        updateNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newStudentName = JOptionPane.showInputDialog("Enter the new student name:");
                if (newStudentName != null && !newStudentName.isEmpty()) {
                    try {
                        studentService.updateStudent(studentId, newStudentName);
                        JOptionPane.showMessageDialog(null, "Student name updated successfully.");
                        loadSkills(studentId);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error updating student name: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a valid student name.");
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateExperienceButton);
        buttonPanel.add(updateNameButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        try {
            loadSkills(studentId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadSkills(int studentId) throws SQLException {
        tableModel.setRowCount(0);
        skills = studentService.getSkillsForStudent(studentId);
        for (Skill skill : skills) {
            tableModel.addRow(new Object[]{skill.getId(), skill.getLanguage(), skill.getLevel(), skill.getExperience()});
        }
    }
}

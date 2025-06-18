import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class StudentManagementFrame {

    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private StudentService studentService;
    private Connection conn;

    public StudentManagementFrame(Connection connection, StudentService studentService, SkillService skillService) throws SQLException {
        this.studentService = new StudentService(new StudentDAO(connection));
        this.conn = connection;
        initComponents();
        loadStudents();
    }

    private void initComponents() {
        frame = new JFrame("Student Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Column names according to the StudentWithSkills view
        String[] columnNames = {"Student ID", "Student Name", "Skills", "Levels", "Experiences (years)"};
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

        JButton deleteButton = new JButton("Delete Student");
        deleteButton.addActionListener(this::deleteAction);

        JButton updateButton = new JButton("Update Student");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                int studentId = (Integer) tableModel.getValueAt(row, 0);
                new StudentSkillsListFrame(conn,studentId);
                try {
                    loadStudents();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StudentForm(conn);
                try {
                    loadStudents();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        JButton refresh  = new JButton("Refresh");
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadStudents();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
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

    private void loadStudents() throws SQLException {
        List<StudentWithSkills> students = studentService.getAllStudentsWithSkills();
        tableModel.setRowCount(0); // Clear the table
        for (StudentWithSkills student : students) {
            Object[] row = new Object[]{
                    student.getStudentId(),
                    student.getStudentName(),
                    student.getSkills(),
                    student.getLevels(),
                    student.getExperiences()
            };
            tableModel.addRow(row);
        }
    }

    private void deleteAction(ActionEvent event) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(frame, "Please select a student to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this student?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int studentId = (Integer) tableModel.getValueAt(row, 0);
        try {
            studentService.deleteStudent(studentId);
            loadStudents();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error deleting student: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}

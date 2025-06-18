import java.sql.*;
import java.util.*;
public class StudentService {
    private StudentDAO studentDAO;

    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    public int insertStudent(String name) throws SQLException {
        return studentDAO.insertStudent(name);
    }

    public List<StudentWithSkills> getAllStudentsWithSkills() throws SQLException
    {
        return studentDAO.getAllStudentsWithSkills();
    }

    public void deleteStudent(int studentId) throws SQLException {
        studentDAO.deleteStudent(studentId);
    }

    public void updateStudent(int studentid, String newStudentName) throws SQLException
    {
        studentDAO.updateStudent(studentid,newStudentName);
    }


    public List<Skill> getSkillsForStudent(int studentId) throws SQLException
    {
        return studentDAO.getSkillsForStudent(studentId);
    }

}

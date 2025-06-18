import java.sql.*;
public class StudentSkillService {
    private StudentSkillsDAO studentSkillsDAO;

    public StudentSkillService(StudentSkillsDAO studentSkillsDAO) {
        this.studentSkillsDAO = studentSkillsDAO;
    }

    public boolean insertStudentSkill(int studentId, int skillId) {
        try {
            studentSkillsDAO.insertStudentSkill(studentId, skillId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: Unable to assign skill to student.");
            return false;
        }
    }
}

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentSkillsDAO
{
    private Connection conn;

    public StudentSkillsDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertStudentSkill(int studentId, int skillId) throws SQLException {
        String sql = "INSERT INTO Student_Skills (student_id, skill_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, skillId);
            pstmt.executeUpdate();
        }
    }
}
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TaskRequirementDAO {
    private Connection conn;

    public TaskRequirementDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertTaskRequirement(int taskId, int skillId) throws SQLException {
        String sql = "INSERT INTO Task_Requirements (task_id, skill_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            pstmt.setInt(2, skillId);
            pstmt.executeUpdate();
        }
    }
}

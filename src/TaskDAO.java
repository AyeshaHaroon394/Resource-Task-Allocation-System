import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskDAO {
    private Connection conn;

    public TaskDAO(Connection conn) {
        this.conn = conn;
    }

    public int insertTask(String taskName) throws SQLException {
        String sql = "INSERT INTO Task (task_name) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, taskName);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating task failed, no rows affected.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating task failed, no ID obtained.");
                }
            }
        }
    }

    public void insertTaskRequirement(int taskId, int skillId) throws SQLException {
        String sql = "INSERT INTO Task_Requirements (task_id, skill_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            pstmt.setInt(2, skillId);
            pstmt.executeUpdate();
        }
    }
    public List<TaskWithSkills> getAllTasksWithSkills() throws SQLException {
        List<TaskWithSkills> tasks = new ArrayList<>();
        String sql = "SELECT * FROM TaskWithSkills";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int taskId = rs.getInt("task_id");
                String taskName = rs.getString("task_name");
                String skills = rs.getString("skills");
                String levels = rs.getString("levels");

                tasks.add(new TaskWithSkills(taskId, taskName, skills, levels));
            }
        }
        return tasks;
    }

    public void updateTask(int taskId, String newTaskName) throws SQLException {
        String sql = "UPDATE task SET task_name = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newTaskName);
            pstmt.setInt(2, taskId);
            pstmt.executeUpdate();
        }
    }

    public List<Skill> getSkillsForTask(int taskId) throws SQLException {
        List<Skill> skills = new ArrayList<>();
        String sql = "SELECT sk.id, sk.language, sk.level, sk.experience FROM Skill sk " +
                "INNER JOIN Task_Requirements tr ON sk.id = tr.skill_id " +
                "WHERE tr.task_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int skillId = rs.getInt("id");
                    String language = rs.getString("language");
                    char level = rs.getString("level").charAt(0);
                    int experience = rs.getInt("experience");

                    Skill skill = new Skill(skillId, language, level, experience);
                    skills.add(skill);
                }
            }
        }
        return skills;
    }

    public void deleteTaskRequirement(int taskId) throws SQLException
    {
            conn.setAutoCommit(false);

            try {
                // Step 1: Find corresponding skill IDs
                String findSkillsSql = "SELECT skill_id FROM task_requirements WHERE task_id = ?";
                List<Integer> skillIds = new ArrayList<>();
                try (PreparedStatement pstmt = conn.prepareStatement(findSkillsSql)) {
                    pstmt.setInt(1, taskId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            skillIds.add(rs.getInt("skill_id"));
                        }
                    }
                }

                // Step 2: Delete skills
                String deleteSkillSql = "DELETE FROM skill WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteSkillSql)) {
                    for (int skillId : skillIds) {
                        pstmt.setInt(1, skillId);
                        pstmt.executeUpdate();
                    }
                }

                // Step 3: Delete task
                String deleteTaskSql = "DELETE FROM task WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteTaskSql)) {
                    pstmt.setInt(1, taskId);
                    pstmt.executeUpdate();
                }

                conn.commit(); // Commit the transaction
            } catch (SQLException e) {
                conn.rollback(); // Roll back if there was a problem
                throw e;
            } finally {
                conn.setAutoCommit(true); // Restore default behavior
            }
    }
}


import java.sql.*;
public class SkillDAO {
    private Connection conn;

    public SkillDAO(Connection conn) {
        this.conn = conn;
    }

    public int insertSkill(Skill skill) throws SQLException {
        String sql = "INSERT INTO Skill (language, level, experience) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, skill.getLanguage());
            pstmt.setString(2, String.valueOf(skill.getLevel()));
            pstmt.setInt(3, skill.getExperience());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1; // Return an invalid ID if insertion failed
    }

    public int insertTaskSkill(Skill skill) throws SQLException {
        String sql = "INSERT INTO Skill (language, level) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, skill.getLanguage());
            pstmt.setString(2, String.valueOf(skill.getLevel()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating task skill failed, no rows affected.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating task skill failed, no ID obtained.");
                }
            }
        }
    }

    public int insertTaskSkill2(Skill skill) throws SQLException {
        String sql = "INSERT INTO Skill (language, level, experience) VALUES (?, ?, NULL)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, skill.getLanguage());
            pstmt.setString(2, String.valueOf(skill.getLevel()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating task skill failed, no rows affected.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating task skill failed, no ID obtained.");
                }
            }
        }
    }

    public void updateSkillName(int skillId, String newSkillName, String newLevel) throws SQLException {
        String sql = "UPDATE skill SET language = ?, level = ?, experience = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, newSkillName);
            pstmt.setString(2, newLevel);
            pstmt.setInt(3,-1);
            pstmt.setInt(4, skillId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating skill failed, no rows affected.");
            }
        }
    }

    public void updateSkillNameAndExperience(int skillId, String newSkillName, int newExperience) throws SQLException {
        String sql = "UPDATE skill SET language = ?, experience = ?, level = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, newSkillName);
            pstmt.setInt(2,newExperience);
            pstmt.setInt(4, skillId);

            Skill skill = new Skill(newSkillName, newExperience);
            char newLevel = skill.getLevel();

            pstmt.setString(3, String.valueOf(newLevel));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating skill failed, no rows affected.");
            }
        }
    }
}
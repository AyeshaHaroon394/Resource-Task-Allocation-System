import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private Connection conn;

    public StudentDAO(Connection conn) {
        this.conn = conn;
    }

    public int insertStudent(String name) throws SQLException {
        String sql = "INSERT INTO Student (name) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1; // Return an invalid ID if insertion failed
    }

    public List<StudentWithSkills> getAllStudentsWithSkills() throws SQLException {
        List<StudentWithSkills> students = new ArrayList<>();
        String sql = "SELECT * FROM StudentWithSkills";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int studentId = rs.getInt("student_id");
                String studentName = rs.getString("student_name");
                String skills = rs.getString("skills");
                String levels = rs.getString("levels");
                String experiences = rs.getString("experiences");

                students.add(new StudentWithSkills(studentId, studentName, skills, levels, experiences));
            }
        }
        return students;
    }

    public void deleteStudent(int studentId) throws SQLException {
        conn.setAutoCommit(false);

        try {
            // Step 1: Find corresponding skill IDs
            String findSkillsSql = "SELECT skill_id FROM student_skills WHERE student_id = ?";
            List<Integer> skillIds = new ArrayList<>();
            try (PreparedStatement pstmt = conn.prepareStatement(findSkillsSql)) {
                pstmt.setInt(1, studentId);
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

            // Step 3: Delete student
            String deleteStudentSql = "DELETE FROM student WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteStudentSql)) {
                pstmt.setInt(1, studentId);
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

    public void updateStudent(int studentid, String newStudentName) throws SQLException {
        String sql = "UPDATE student SET name = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStudentName);
            pstmt.setInt(2, studentid);
            pstmt.executeUpdate();
        }
    }

    public List<Skill> getSkillsForStudent(int studentId) throws SQLException {
        List<Skill> skills = new ArrayList<>();
        String sql = "SELECT sk.id, sk.language, sk.level, sk.experience FROM skill sk " +
                "INNER JOIN student_skills ss ON sk.id = ss.skill_id " +
                "WHERE ss.student_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
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


    public void insertStudentSkill(int studentId, int skillId) throws SQLException {
        String sql = "INSERT INTO Student_Skills (student_id, skill_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, skillId);
            pstmt.executeUpdate();
        }
    }

}
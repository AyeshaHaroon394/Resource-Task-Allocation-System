import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SkillMatcher {

    private Connection connection;

    public SkillMatcher(Connection connection) {
        this.connection = connection;
    }

    public void matchSkillsAndExportToFile() {
        try {
            MatchingStrategy exactMatchStrategy = new ExactMatch();
            MatchingStrategy skillOnlyMatchStrategy = new SkillOnlyMatch();

            StringBuilder matchResults = new StringBuilder();

            // ExactMatch Query
            String exactMatchQuery = "SELECT * FROM ExactMatch";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(exactMatchQuery)) {
                while (rs.next()) {
                    String studentName = rs.getString("student_name");
                    String taskName = rs.getString("task_name");
                    matchResults.append("Student: ")
                            .append(studentName)
                            .append(" matches with Task: ")
                            .append(taskName)
                            .append(" (Exact Match)")
                            .append(System.lineSeparator());
                }
            }

            // SkillOnlyMatch Query
            String skillOnlyMatchQuery = "SELECT * FROM SkillOnlyMatch";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(skillOnlyMatchQuery)) {
                while (rs.next()) {
                    String studentName = rs.getString("student_name");
                    String taskName = rs.getString("task_name");
                    matchResults.append("Student: ")
                            .append(studentName)
                            .append(" matches with Task: ")
                            .append(taskName)
                            .append(" (Skill Only Match)")
                            .append(System.lineSeparator());
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("matchResults.txt"))) {
                writer.write(matchResults.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

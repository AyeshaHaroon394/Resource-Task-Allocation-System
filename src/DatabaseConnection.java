import javax.swing.*;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/ass3";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static void main(String[] args) {

        try
        {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);


            StudentDAO studentDAO = new StudentDAO(connection);
            SkillDAO skillDAO = new SkillDAO(connection);
            TaskDAO taskDAO = new TaskDAO(connection);

            SkillService skillService = new SkillService(skillDAO);
            StudentService studentService = new StudentService(studentDAO);
            TaskService taskService = new TaskService(connection);

            new StudentManagementFrame(connection, studentService, skillService);
            //new TaskManagementFrame(connection, taskService, skillService);

           // SkillMatcher skillMatcher = new SkillMatcher(connection);
            //skillMatcher.matchSkillsAndExportToFile();


        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


}

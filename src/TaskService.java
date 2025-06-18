import java.sql.*;
import java.util.List;
import java.util.Map;

public class TaskService {
    private TaskDAO taskDAO;

    public TaskService(Connection conn) {
        this.taskDAO = new TaskDAO(conn);
    }

    public int createTask(String taskName) {
        try {
            return taskDAO.insertTask(taskName);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error creating task: " + e.getMessage());
            return -1; // Indicating failure
        }
    }

    public void addTaskRequirement(int taskId, int skillId) {
        try {
            taskDAO.insertTaskRequirement(taskId, skillId);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding task requirement: " + e.getMessage());
        }
    }

    public List<TaskWithSkills> getAllTasksWithSkills() throws SQLException
    {
        return(taskDAO.getAllTasksWithSkills());
    }

    public void updateTask(int taskId, String newTaskName) throws SQLException
    {
        taskDAO.updateTask(taskId,newTaskName);
    }

    public List<Skill> getSkillsForTask(int taskId) throws SQLException
    {
        return (taskDAO.getSkillsForTask(taskId));
    }


    public void deleteTaskRequirement(int taskId)
    {
        try {
            taskDAO.deleteTaskRequirement(taskId);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error removing task requirement: " + e.getMessage());
        }
    }
}

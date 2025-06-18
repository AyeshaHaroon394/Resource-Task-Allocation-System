import java.sql.*;
public class TaskRequirementService {
    private TaskRequirementDAO taskRequirementDAO;

    public TaskRequirementService(TaskRequirementDAO taskRequirementDAO) {
        this.taskRequirementDAO = taskRequirementDAO;
    }

    public boolean addRequirementToTask(int taskId, int skillId) {
        try {
            taskRequirementDAO.insertTaskRequirement(taskId, skillId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: Unable to add requirement to task.");
            return false;
        }
    }


}

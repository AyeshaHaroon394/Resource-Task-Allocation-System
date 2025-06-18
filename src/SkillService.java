import java.sql.*;
public class SkillService {
    private SkillDAO skillDAO;

    public SkillService(SkillDAO skillDAO) {
        this.skillDAO = skillDAO;
    }

    public int insertSkill(Skill skill) {
        int skillId = -1;
        try {
            skillId = skillDAO.insertSkill(skill);
            if (skillId == -1) {
                System.out.println("Skill insertion failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: Unable to add skill to database.");
        }
        return skillId;
    }

    public boolean insertTaskSkill(Skill skill) {
        try {
            int skillId = skillDAO.insertTaskSkill(skill);
            if (skillId > 0) {
                System.out.println("Task skill inserted with ID: " + skillId);
                return true;
            } else {
                System.out.println("Task skill insertion failed.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: Unable to insert task skill.");
            return false;
        }
    }

    public void updateSkillName(int skillId, String newSkillName, String newLevel) throws SQLException
    {
        skillDAO.updateSkillName(skillId, newSkillName, newLevel);
    }

    public void updateSkillNameAndExperience(int skillId, String newSkillName, int newExperience) throws SQLException
    {
        skillDAO.updateSkillNameAndExperience(skillId, newSkillName, newExperience);
    }



        // Service method for insertTaskSkill2
    public boolean insertTaskSkill2(Skill skill) {
        try {
            int skillId = skillDAO.insertTaskSkill2(skill);
            if (skillId > 0) {
                System.out.println("Task skill inserted with ID: " + skillId);
                return true;
            } else {
                System.out.println("Task skill insertion failed.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: Unable to insert task skill.");
            return false;
        }
    }
}

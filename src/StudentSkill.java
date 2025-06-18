public class StudentSkill {
    private int studentId;
    private int skillId;
    private int yearsOfExperience;

    public int getSkillId() { return skillId; }
    public int getStudentId() { return studentId; }
    public void setSkillId(int skillId) { this.skillId = skillId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(int yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }

    public StudentSkill(int studentId, int skillId, int yearsOfExperience) {
        this.studentId = studentId;
        this.skillId = skillId;
        this.yearsOfExperience = yearsOfExperience;
    }
}

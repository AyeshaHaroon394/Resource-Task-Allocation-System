public class StudentWithSkills {
    private int studentId;
    private String studentName;
    private String skills;
    private String levels;
    private String experiences;

    public StudentWithSkills(int studentId, String studentName, String skills, String levels, String experiences) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.skills = skills;
        this.levels = levels;
        this.experiences = experiences;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getExperiences()
    {
        return experiences;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getLevels() {
        return levels;
    }

    public void setLevels(String levels) {
        this.levels = levels;
    }

    @Override
    public String toString() {
        return "StudentWithSkills{" +
                "studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", skills='" + skills + '\'' +
                ", levels='" + levels + '\'' +
                '}';
    }
}

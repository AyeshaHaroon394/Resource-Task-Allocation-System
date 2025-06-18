import java.util.List;
class Student {
    private String name;
    private List<Skill> skills;
    private int id;
    public Student(String name, List<Skill> skills) {
        this.name = name;
        this.skills = skills;
    }

    public Student(int id, String n)
    {
        this.id=id;
        name=n;
    }

    public Student(String name)
    {
        this.name=name;
    }

    public void printResources() {
        System.out.println("Name: " + this.name);
        for (Skill skill : skills) {
            System.out.println("Skill: " + skill.getLanguage());
            System.out.println("Experience: " + skill.getExperience());
        }
        System.out.println("--------------------------------------");
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
}

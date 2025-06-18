public class Skill {
    String language;
    char level;
    int experience;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //for student skill insertion we convert level
    public Skill(String language, int experience) {
        this.language = language;
        this.experience = experience;
        this.level = convertExperienceToLevel(experience);
    }

    //for task insertion we set experience to -1
    public Skill(String language, char level) {
        this.language = language;
        this.level = level;
        this.experience=-1;
    }

    public char convertExperienceToLevel(int experience)
    {
        if (experience <= 1)
        {
            return 'b';
        }
        else if (experience == 2)
        {
            return 'i';
        }
        else
        {
            return 'e';
        }
    }

    public String getLanguage() {
        return language;
    }

    public char getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }
    public String getExperienceLevel()
    {
        switch (this.level) {
            case 'b':
                return "beginner";
            case 'i':
                return "intermediate";
            case 'e':
                return "expert";
            default:
                return "beginner";
        }
    }

    public Skill(int skillId, String language, char level, int experience)
    {
        this.language=language;
        this.level=level;
        this.experience=experience;
        this.id=skillId;
    }

}

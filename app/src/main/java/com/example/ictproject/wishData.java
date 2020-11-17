package com.example.ictproject;

import java.io.Serializable;

public class wishData implements Serializable {
    private String age;
    private String sex;
    private String experience;

    public wishData(String age, String sex, String experience) {
        this.age = age;
        this.sex = sex;
        this.experience = experience;
    }

    public String getAge() { return age; }

    public String getSex() { return sex; }

    public String getExperience() { return experience; }
}

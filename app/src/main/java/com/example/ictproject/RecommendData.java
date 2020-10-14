package com.example.ictproject;

import java.io.Serializable;

public class RecommendData implements Serializable{
    private String sex;
    private String ageRange;
    private String experience;
    private String region;

    public RecommendData(String sex, String ageRange, String experience, String region) {
        this.sex = sex;
        this.ageRange = ageRange;
        this.experience = experience;
        this.region = region;
    }

    public String getSex() {
        return sex;
    }
    public String getAgeRange() {
        return ageRange;
    }
    public String getExperience() {
        return experience;
    }
    public String getRegion() {
        return region;
    }
}

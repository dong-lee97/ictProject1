package com.example.ictproject.upload;

public class Upload {
    private String name;
    private String imageUrl;
    private String age;
    private String sex;
    private String experience;
    private String detail;
    private String region;
    private String day;
    private String uid;
    private String companyName;
    private String companyPhone;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String companyName, String companyPhone, String uid) {
        this.companyName = companyName;
        this.companyPhone = companyPhone;
        this.uid = uid;
    }

    public Upload(String name, String imageUrl, String age, String sex, String experience, String detail, String region, String day, String uid) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.age = age;
        this.sex = sex;
        this.experience = experience;
        this.detail = detail;
        this.region = region;
        this.day = day;
        this.uid = uid;
    }

    public Upload(String name, String imageUrl, String age, String sex, String experience, String detail, String region, String day, String uid, String companyName, String companyPhone) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.age = age;
        this.experience = experience;
        this.detail = detail;
        this.region = region;
        this.day = day;
        this.uid = uid;
        this.companyName = companyName;
        this.companyPhone = companyPhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
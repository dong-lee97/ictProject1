package com.example.ictproject.upload;

public class CompanyUpload {
    private String name;
    private String imageUrl;
    private String age;
    private String sex;
    private String experience;
    private String detail;
    private String region;
    private String day;
    private String companyName;
    private String companyPhone;
    private String uid;

    public CompanyUpload() {
        //empty constructor needed
    }

    public CompanyUpload(String cName, String cPhone, String uid) {
        this.companyName = cName;
        this.companyPhone = cPhone;
        this.uid = uid;
    }
    public CompanyUpload(String name, String cName, String cPhone, String uid) {
        this.name = name;
        this.companyName = cName;
        this.companyPhone = cPhone;
        this.uid = uid;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name;}
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String name) {
        companyName = name;
    }
    public String getCompanyPhone() {
        return companyPhone;
    }
    public void setCompanyPhone(String phone) {
        companyPhone = phone;
    }
    public String getUid() {return uid; }
    public void setUid(String uid) { this.uid = uid; }

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
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getExperience() {
        return experience;
    }
    public void setExperience(String experience) {
        this.experience = experience;
    }
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
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
}

package com.example.ictproject.upload;

public class CompanyUpload {
    private String name;
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

}

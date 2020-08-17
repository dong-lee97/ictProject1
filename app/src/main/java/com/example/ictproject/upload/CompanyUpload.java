package com.example.ictproject.upload;

public class CompanyUpload {
    private String cName;
    private String cPhone;
    private String uId;

    public CompanyUpload() {
        //empty constructor needed
    }

    public CompanyUpload(String name, String phone, String uid) {
        cName = name;
        cPhone = phone;
        uId = uid;
    }
    public String getCName() {
        return cName;
    }
    public void setCName(String name) {
        cName = name;
    }
    public String getCPhone() {
        return cPhone;
    }
    public void setCPhone(String phone) {
        cPhone = phone;
    }
    public String getuId() {return uId; }
    public void setuId(String uId) { this.uId = uId; }
}

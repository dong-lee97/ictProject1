package com.example.ictproject.upload;

public class Feedback {
    private String companyName;
    private String feedback;

    public Feedback() {
        //empty constructor needed
    }

    public Feedback(String companyName, String feedback) {
        this.companyName = companyName;
        this.feedback = feedback;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}

package com.example.travelbuddy.Objects;

import java.io.Serializable;
import java.util.List;

public class Forum implements Serializable {
    private String countryName;
    private String photoUrl;
    private String forumId;
    private List<String> questionIds;

    public Forum(String countryName, String photoUrl, String forumId, List<String> questionIds) {
        this.countryName = countryName;
        this.photoUrl = photoUrl;
        this.forumId = forumId;
        this.questionIds = questionIds;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    public List<String> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<String> questionIds) {
        this.questionIds = questionIds;
    }

}

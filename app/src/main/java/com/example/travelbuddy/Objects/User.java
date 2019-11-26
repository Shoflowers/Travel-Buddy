package com.example.travelbuddy.Objects;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private String username;
    private String name;
    private String email;
    private String profilePhotoUrl;
    private String userId;
    private List<String> forumIds;
    private List<String> questionIds;
    private List<String> answerIds;

    public User(String username, String name, String email, String profilePhotoUrl,
                String userId, List<String> forumIds, List<String> questionIds, List<String> answerIds) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.profilePhotoUrl = profilePhotoUrl;
        this.userId = userId;
        this.forumIds = forumIds;
        this.questionIds = questionIds;
        this.answerIds = answerIds;
    }

    public User(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getForumIds() {
        return forumIds;
    }

    public void setForumIds(List<String> forumIds) {
        this.forumIds = forumIds;
    }

    public List<String> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<String> questionIds) {
        this.questionIds = questionIds;
    }

    public List<String> getAnswerIds() {
        return answerIds;
    }

    public void setAnswerIds(List<String> answerIds) {
        this.answerIds = answerIds;
    }
}

package com.example.travelbuddy.Objects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Answer implements Serializable {

    String answerBody;
    String answerId;
    String questionId;
    String userId;
    String userName;
    String userPhotoUrl;
    int vote;
    int view;
    Date dateTime;
    List<String> commentList;

    public Answer(){}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public Answer(String answerBody, String answerId, String questionId, String userId,
                  int vote, int view, Date dateTime, List<String> commentList, String userName, String url) {
        this.answerBody = answerBody;
        this.answerId = answerId;
        this.questionId = questionId;
        this.userId = userId;
        this.vote = vote;
        this.view = view;
        this.dateTime = dateTime;
        this.commentList = commentList;
        this.userName = userName;
        this.userPhotoUrl = url;
    }

    public String getAnswerBody() {
        return answerBody;
    }

    public void setAnswerBody(String answerBody) {
        this.answerBody = answerBody;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public List<String> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<String> commentList) {
        this.commentList = commentList;
    }
}

package com.example.travelbuddy.Objects;

import java.util.Date;
import java.util.List;

public class Answer {

    String answerBody;
    String answerId;
    String questionId;
    String userId;
    int vote;
    int view;
    Date date;
    List<String> commentList;

    public Answer(){}

    public Answer(String answerBody, String answerId, String questionId, String userId,
                  int vote, int view, Date date, List<String> commentList) {
        this.answerBody = answerBody;
        this.answerId = answerId;
        this.questionId = questionId;
        this.userId = userId;
        this.vote = vote;
        this.view = view;
        this.date = date;
        this.commentList = commentList;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<String> commentList) {
        this.commentList = commentList;
    }
}

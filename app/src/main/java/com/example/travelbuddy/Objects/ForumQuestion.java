package com.example.travelbuddy.Objects;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class ForumQuestion implements Serializable {
    private String questionId;
    private String fourmId;
    private String questionTitle;
    private String questionBody;
    private String userId;
    private List<String> tags;
    private List<String> answerIds;
    private int votes;
    private Timestamp dateTime;
    private int viewCount;

    public ForumQuestion(String questionId, String questionTitle, String questionBody,
                         String userId, List<String> tags, List<String> answerIds, int votes,
                         Timestamp dateTime, String fourmId, int viewCount) {
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.questionBody = questionBody;
        this.userId = userId;
        this.tags = tags;
        this.answerIds = answerIds;
        this.votes = votes;
        this.dateTime = dateTime;
        this.viewCount = viewCount;
        this.fourmId = fourmId;
    }

    public ForumQuestion(){}

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionBody() {
        return questionBody;
    }

    public void setQuestionBody(String questionBody) {
        this.questionBody = questionBody;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getAnswerIds() {
        return answerIds;
    }

    public void setAnswerIds(List<String> answerIds) {
        this.answerIds = answerIds;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void setFourmId(String fourmId) {
        this.fourmId = fourmId;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getFourmId() {
        return fourmId;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public int getViewCount() { return viewCount;}
}

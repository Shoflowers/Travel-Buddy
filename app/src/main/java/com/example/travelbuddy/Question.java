package com.example.travelbuddy;

import java.sql.Timestamp;
import java.util.List;

public class Question {
    String qId, qTitle, qDetail, userId;
    List<String> tags;
    List<String> answerIds;
    int votes;
    Timestamp timestamp;
    int viewCount;

    public Question() {}

    public Question(String qId, String qTitle, String qDetail, String userId, List<String> tags,
                    List<String> answerIds, int votes, Timestamp timestamp) {
        this.qId = qId;
        this.qTitle = qTitle;
        this.qDetail = qDetail;
        this.userId = userId;
        this.tags = tags;
        this.answerIds = answerIds;
        this.votes = votes;
        this.timestamp = timestamp;

        //TODO: enable count view
        viewCount = 100;
    }

    public String getqId() {
        return qId;
    }

    public String getqTitle() {
        return qTitle;
    }

    public String getqDetail() {
        return qDetail;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getAnswerIds() {
        return answerIds;
    }

    public int getVotes() {
        return votes;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getViewCount() {
        return viewCount;
    }
}

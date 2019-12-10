package com.example.travelbuddy.Objects;

import java.io.Serializable;

public class Comment implements Serializable {

    String answerId;
    String commenterId;
    String commenterName;
    String commentBody;

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(String commenterId) {
        this.commenterId = commenterId;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public Comment(String answerId, String commenterId, String commenterName, String commentBody) {
        this.answerId = answerId;
        this.commenterId = commenterId;
        this.commenterName = commenterName;
        this.commentBody = commentBody;
    }

    public Comment() {

    }
}

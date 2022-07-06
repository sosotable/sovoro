package com.sovoro;

public class SoVoRoCommentInfo {
    private String userImage;
    private String userComment;
    private String userCommentLikesCount;

    public SoVoRoCommentInfo(String userImage, String userComment, String userCommentLikesCount) {
        this.userImage = userImage;
        this.userComment = userComment;
        this.userCommentLikesCount = userCommentLikesCount;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public String getUserCommentLikesCount() {
        return userCommentLikesCount;
    }

    public void setUserCommentLikesCount(String userCommentLikesCount) {
        this.userCommentLikesCount = userCommentLikesCount;
    }
}


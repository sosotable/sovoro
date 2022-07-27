package com.sovoro;

public class SoVoRoCommentInfo {
    public String userImage;
    public String userNickname;
    public String userComment;
    public String userCommentLikesCount;
    public String userCommentNumber;

    public SoVoRoCommentInfo(String userImage, String userNickname, String userComment, String userCommentLikesCount, String userCommentNumber) {
        this.userImage = userImage;
        this.userNickname=userNickname;
        this.userComment = userComment;
        this.userCommentLikesCount = userCommentLikesCount;
        this.userCommentNumber=userCommentNumber;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
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


package com.sovoro;

public class SoVoRoCommentInfo {
    public String userImage;
    public String userNickname;
    public String userComment;
    public int userCommentLikesCount;
    public int userCommentNumber;
    public SoVoRoCommentInfo(String userImage, String userNickname, String userComment, int userCommentLikesCount, int userCommentNumber) {
        this.userImage = userImage;
        this.userNickname=userNickname;
        this.userComment = userComment;
        this.userCommentLikesCount = userCommentLikesCount;
        this.userCommentNumber=userCommentNumber;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public int getUserCommentLikesCount() {
        return userCommentLikesCount;
    }

    public void setUserCommentLikesCount(int userCommentLikesCount) {
        this.userCommentLikesCount = userCommentLikesCount;
    }

    public int getUserCommentNumber() {
        return userCommentNumber;
    }

    public void setUserCommentNumber(int userCommentNumber) {
        this.userCommentNumber = userCommentNumber;
    }

    @Override
    public String toString() {
        return "SoVoRoCommentInfo{" +
                "userImage='" + userImage + '\'' +
                ", userNickname='" + userNickname + '\'' +
                ", userComment='" + userComment + '\'' +
                ", userCommentLikesCount=" + userCommentLikesCount +
                ", userCommentNumber=" + userCommentNumber +
                '}';
    }
}


package com.sovoro;

public class CommentInfo {
    public String userid;
    public String nickname;
    public String commentcontent;
    public int commentlikes;
    public int commentnumber;

    public CommentInfo(String userid, String nickname, String commentcontent, int commentlikes, int commentnumber) {
        this.userid = userid;
        this.nickname = nickname;
        this.commentcontent = commentcontent;
        this.commentlikes = commentlikes;
        this.commentnumber = commentnumber;
    }

    @Override
    public String toString() {
        return "CommentInfo{" +
                "userid='" + userid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", commentcontent='" + commentcontent + '\'' +
                ", commentlikes=" + commentlikes +
                ", commentnumber=" + commentnumber +
                '}';
    }
}

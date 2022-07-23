package com.sovoro;

public class LoginFlag {
    private boolean idCheck;
    private boolean passwordCheck;
    private boolean nicknameCheck;
    private boolean smsCheck;
    public LoginFlag() {
        idCheck=false;
        passwordCheck=false;
        nicknameCheck=false;
        smsCheck=false;
    }

    public boolean isIdCheck() {
        return idCheck;
    }

    public void setIdCheck(boolean idCheck) {
        this.idCheck = idCheck;
    }

    public boolean isPasswordCheck() {
        return passwordCheck;
    }

    public void setPasswordCheck(boolean passwordCheck) {
        this.passwordCheck = passwordCheck;
    }

    public boolean isNicknameCheck() {
        return nicknameCheck;
    }

    public void setNicknameCheck(boolean nicknameCheck) {
        this.nicknameCheck = nicknameCheck;
    }

    public boolean isSmsCheck() {
        return smsCheck;
    }

    public void setSmsCheck(boolean smsCheck) {
        this.smsCheck = smsCheck;
    }
}

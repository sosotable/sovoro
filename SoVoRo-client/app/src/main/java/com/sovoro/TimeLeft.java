package com.sovoro;

public class TimeLeft {
    public int totalTime;
    public int timeFlow;
    public int minute;
    public int second;
    public TimeLeft() {
        totalTime=180;
        timeFlow=0;
    }
    public void setTimeFlow(int timeFlow) {
        this.timeFlow=timeFlow;
    }
    public void calcCurrentTimeLeft() {
        totalTime-=1;
    }
    public void calcMinute() {
        minute=totalTime/60;
    }
    public void calcSecond() {
        second=totalTime%60;
    }
    public String getSecond() {
        if(second<10)
            return "0"+Integer.toString(second);
        else
            return ""+second;
    }
    public String getTimeLeft() {
        String fmt="%s : %s";
        return String.format(fmt,Integer.toString(minute),getSecond());
    }
}

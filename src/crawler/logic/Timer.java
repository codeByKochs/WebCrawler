package crawler.logic;

import java.util.TimerTask;

public class Timer extends java.util.Timer {

    static final int DEFAULT_TIME_LIMIT = 30;
    static final int DELAY = 0;
    static final int PERIOD = 1000;

    private int timeLimit;
    private int elapsedTime;
    private boolean isRunning;
    private boolean enabled;
    private boolean isTimeLimitReached;

    public Timer(){
        this.elapsedTime = 0;
        this.isRunning = false;
        this.enabled = false;
        this.isTimeLimitReached = false;
        this.scheduleAtFixedRate(createTimerTask(), DELAY, PERIOD);
    }

    public TimerTask createTimerTask(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (enabled){
                    if (elapsedTime > timeLimit){
                        isTimeLimitReached = true;
                    }
                    elapsedTime++;
                }
            }
        };
        return timerTask;
    }

    public void setTimeLimit(int timeLimit){
        this.timeLimit = timeLimit;
    }

    public boolean getIsTimeLimitReached(){
        return isTimeLimitReached;
    }

    public int getElapsedTime(){
        return elapsedTime;
    }

    public void start(){
        this.enabled = true;
    }

    public void stop(){
        this.enabled = false;
    }

    public void reset(){
        elapsedTime = 0;
        enabled = false;
        isTimeLimitReached = false;
    }
}

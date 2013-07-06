package org.ristek.strokeapp.support;


import android.os.Handler;

public class ClockTimer {
    private static final int REFRESH_RATE = 100;

    public static final int MM_SS = 0;
    public static final int HH_MM_SS = 1;

    public static String timeToString(long timemilis, int type) {
        final long second = timemilis / 1000L;
        final long minute = second / 60L;
        final long hour = minute / 60L;
        if (type == MM_SS)
            return String.format("%02d:%02d", minute, second % 60);
        else
            return String.format("%02d:%02d:%02d", hour, minute % 60, second % 60);
    }

    private long timeBefore;
    private long timeLeft;
    private TimerListener listener;
    private Handler mHandler;


    public ClockTimer(TimerListener listener) {
        this.mHandler = new Handler();
        this.listener = listener;
    }

    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            long timeNow = System.currentTimeMillis();
            timeLeft = Math.max(0L, timeLeft - (timeNow - timeBefore));
            timeBefore = timeNow;
            listener.onTimerUpdate(timeLeft);
            mHandler.postDelayed(this, REFRESH_RATE);
        }
    };

    public interface TimerListener {
        public void onTimerUpdate(long timeLeft);
    }


    public long getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(long timemilis) {
        this.timeLeft = timemilis;
    }

    public void start() {
        timeBefore = System.currentTimeMillis();
        mHandler.removeCallbacks(timer);
        mHandler.postDelayed(timer, REFRESH_RATE);
    }

    public void stop() {
        mHandler.removeCallbacks(timer);
    }


}

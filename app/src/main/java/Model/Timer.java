package Model;

import android.os.SystemClock;

import java.sql.SQLOutput;
import java.util.Locale;

public class Timer {
    private long mTargetTime;
    private long mTimeLeft;
    private boolean mRunning;
    private long mDurationMillis;
    Boolean positive;

    // 用参数确定当前是正计时还是倒计时
    public Timer(Boolean positive) {
        mRunning = false;
        this.positive = positive;
    }

    public boolean isRunning() {
        return mRunning;
    }

    // 选择正计时的start
    public void positiveStart() {
        mDurationMillis = 0;
        mTargetTime = -SystemClock.uptimeMillis() + mDurationMillis;
        mRunning = true;
    }

    // 选择倒计时
    public void countdownStart(int hours, int minutes, int seconds) {
        mDurationMillis = (hours * 60 * 60 + minutes * 60 + seconds + 1) * 1000;
        // Add 1 sec to duration so timer stays on current second longer.
        mTargetTime = SystemClock.uptimeMillis() + mDurationMillis;
        mRunning = true;
    }

    // 暂停，将runnable设置为flase
    public void stop() {
        mRunning = false;
    }

    // 点击后暂停计时并统计剩余时间/当前时间（判断是正计时还是倒计时）
    public void pause() {
        if (!positive){
            mTimeLeft = mTargetTime - SystemClock.uptimeMillis();
        } else {
            mTimeLeft = mTargetTime + SystemClock.uptimeMillis();
        }
        mRunning = false;
    }

    // 恢复计时且判断计时状态（正计时还是倒计时）
    public void resume() {
        if (!positive) {
            mTargetTime = SystemClock.uptimeMillis() + mTimeLeft;
        } else {
            mTargetTime = mTimeLeft - SystemClock.uptimeMillis();
        }
        mRunning = true;
    }

    // 得到当前计时时间+判断正计时还是倒计时
    public long getRemainingMilliseconds() {
        if (mRunning) {
            if (!positive){
                return Math.max(0, mTargetTime - SystemClock.uptimeMillis());
            } else {
                return Math.max(0, mTargetTime + SystemClock.uptimeMillis());
            }
        }
        return 0;
    }

    // 得到当前秒数
    public int getRemainingSeconds() {
        if (mRunning) {
            return (int) ((getRemainingMilliseconds() / 1000) % 60);
        }
        return 0;
    }

    // 得到当前分钟数
    public int getRemainingMinutes() {
        if (mRunning) {
            return (int) (((getRemainingMilliseconds() / 1000) / 60) % 60);
        }
        return 0;
    }

    // 得到当前小时数
    public int getRemainingHours() {
        if (mRunning) {
            return (int) (((getRemainingMilliseconds() / 1000) / 60) / 60);
        }
        return 0;
    }

    // 得到当前进度条的进度
    public int getProgressPercent() {
        if (mDurationMillis != 1000) {
            return Math.min(100, 100 - (int) ((getRemainingMilliseconds() - 1000) * 100 / (mDurationMillis - 1000)));
        }
        return 0;
    }


    // 将当前时间以字符串的形式显示出来
    @Override
    public String toString() {
        // 设定剩余的时间为string
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", getRemainingHours(),
                getRemainingMinutes(), getRemainingSeconds());
    }

}

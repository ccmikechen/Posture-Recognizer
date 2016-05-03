package mis.kuas.chart;

import java.util.concurrent.TimeUnit;

/**
 * Created by mingjia on 2016/2/22.
 */
public class Timer implements Runnable {

    public interface OnTickListener {

        public void tick(long time);

    }

    private OnTickListener onTickListener;

    private long tickTime;

    private long startTime;

    private boolean isStarted = false;

    public Timer(long tickTime) {
        this.tickTime = tickTime;
    }

    public void setOnTickListener(OnTickListener listener) {
        this.onTickListener = listener;
    }

    public void removeOnTickListener() {
        this.onTickListener = null;
    }

    public void restart() {
        this.stop();
        this.start();
    }

    public void start() {
        this.isStarted = true;
        this.startTime = System.nanoTime();
        new Thread(this).start();
    }

    public void stop() {
        this.isStarted = false;
    }

    public boolean isStarted() {
        return this.isStarted;
    }

    public void run() {
        while (this.isStarted) {
            try {
                TimeUnit.MILLISECONDS.sleep(this.tickTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                stop();
            }
            if (this.onTickListener != null) {
                long elapsedTime = System.nanoTime() - startTime;
                this.onTickListener.tick(elapsedTime);
            }
        }
    }

}

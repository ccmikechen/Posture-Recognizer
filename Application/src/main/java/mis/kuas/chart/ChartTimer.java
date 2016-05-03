package mis.kuas.chart;

import android.os.Handler;

import java.util.concurrent.TimeUnit;

public class ChartTimer implements Runnable {

    private long delay;
    private Handler handler;
    private boolean isStart = false;

    public ChartTimer(Handler handler, long delay) {
        this.handler = handler;
        this.delay = delay;
    }

    synchronized public void setDelay(long delay) {
        this.delay = delay;
    }

    public void start() {
        isStart = true;
        new Thread(this).start();
    }

    public void stop() {
        isStart = false;
    }

    public void run() {
        while (isStart) {
            handler.sendEmptyMessage(1);
            try {
                TimeUnit.MILLISECONDS.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

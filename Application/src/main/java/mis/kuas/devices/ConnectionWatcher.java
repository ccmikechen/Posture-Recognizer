package mis.kuas.devices;

import java.util.concurrent.TimeUnit;

/**
 * Created by mingjia on 2016/2/19.
 */
public class ConnectionWatcher implements Runnable {

    public interface OnDisconnectedListener {
        public void onDisconnected();
    }

    private boolean isAlive = false;

    private boolean isStarted = false;

    private long lastUpdateTime;

    private long limitTime;

    private long tickTime;

    private OnDisconnectedListener disconnectedListener;

    public ConnectionWatcher(long limitTime, long tickTime) {
        this.limitTime = limitTime;
        this.tickTime = tickTime;
    }

    public void start(OnDisconnectedListener disconnectedListener) {
        this.disconnectedListener = disconnectedListener;
        this.isAlive = true;
        this.lastUpdateTime = System.nanoTime();
        this.isStarted = true;
        new Thread(this).start();
    }

    public void stop() {
        this.isStarted = false;
    }

    public void update() {
        this.lastUpdateTime = System.nanoTime();
    }

    @Override
    public void run() {
        while (isStarted) {
            try {
                TimeUnit.MILLISECONDS.sleep(tickTime);
            } catch (InterruptedException e) {
                this.isStarted = false;
                this.isAlive = false;
                e.printStackTrace();
            }
            checkAlive();
        }
    }

    private void checkAlive() {
        long currentTime = System.nanoTime();
        if ((currentTime - this.lastUpdateTime) > (limitTime * 1e6)) {
            this.isAlive = false;
            this.isStarted = false;
            this.disconnectedListener.onDisconnected();
        }
    }

}
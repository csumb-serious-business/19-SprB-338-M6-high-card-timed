/**
 * todo: add desc
 *
 * @author todo
 */
class Timer extends Thread {
    private int timePlayed;
    private boolean isRunning;
    private GameController controller;

    public Timer(GameController controller) {
        this.controller = controller;

    }

    /**
     * sets isRunning of the Timer
     *
     * @param running todo
     */
    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void start() {
        isRunning = true;
        Thread timerThread = new Thread(this);
        timerThread.start();
    }

    public void doNothing(int milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (isRunning) {
                    controller.changeTimerDisplay(timePlayed);
                    doNothing(1000);
                    timePlayed++;
                } else {
                    doNothing(0);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

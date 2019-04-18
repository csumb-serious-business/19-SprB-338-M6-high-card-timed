/**
 * A timer for the game
 */
class Timer extends Thread {
    private final GameController controller;
    private int timePlayed;
    private boolean isRunning;

    /**
     * Creates a new game timer
     *
     * @param controller the game controller for this timer
     */
    public Timer(GameController controller) {
        this.controller = controller;

    }

    /**
     * sets isRunning of the Timer
     *
     * @param running true if this timer should be running
     */
    public void setRunning(boolean running) {
        isRunning = running;
    }

    /**
     * Starts this timer
     */
    public void start() {
        isRunning = true;
        Thread timerThread = new Thread(this);
        timerThread.start();
    }

    /**
     * sets this timer to idle for a number of milliseconds
     *
     * @param milliseconds the time to idle in milliseconds
     * @throws InterruptedException when thread interrupted
     */
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

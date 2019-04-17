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

    public void start() {
        isRunning = true;
        Thread timerThread = new Thread(this);
        timerThread.start();
    }

    public void doNothing(int mili) throws InterruptedException {
        Thread.sleep(mili);
    }

    @Override
    public void run() {
        GameView view = controller.getView();
        while (true) {
            isRunning = view.getTimerStop().getActionCommand().equals("stop");
            try {
                if (isRunning) {
                    view.changeTimerDisplay(timePlayed);
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

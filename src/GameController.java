import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController {

    private Game model;
    private GameView view;
    private Timer timer;

    public GameController(Game game, GameView view) {
        model = game;

        this.view = view;
        view.setController(this);

        this.timer = new Timer(this);
    }

    public Hand getPlayerHand() {
        return model.getHand(1);

    }

    public Hand getAiHand() {
        return model.getHand(0);
    }

    public int getPlayerSkips() {
        return model.getPlayerSkips();
    }

    public int getAiSkips() {
        return model.getAiSkips();
    }

    public boolean changeTimerDisplay(int time) {
        view.changeTimerDisplay(time);
        return true;
    }


    /**
     * @return cannotPlayListener of the GameControl
     */
    public ActionListener getSkipTurnListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Skip Turn")) {
                    model.skipPlayer();
                }
            }
        };
    }

    /**
     * @return timerListener of the GameControl
     */
    public ActionListener getTimerListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("start")) {
                    timer.setRunning(true);
                    view.timerState(true);
                } else {
                    timer.setRunning(false);
                    view.timerState(false);
                }
            }
        };
    }

    /**
     * @return an action listener for played cards
     */
    public ActionListener playCardListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int humanIndex = Integer.parseInt(e.getActionCommand());

                // cards are ordered by value
                Card humanCard = model.getHand(1).playCard(humanIndex);
                Card botCard = model.getHand(0).playCard(humanIndex);

                // If the bot has a card higher, should we do something?
                view.takeTurn(humanCard, botCard);
                view.repaint();
            }
        };
    }

    public void startGame() {

        // shuffle and deal into the hands.
        model.deal();

        //view.takeTurn(null, null); // Start off with nothing selected
        view.build(model.getTitle(), model.getNumPlayers(), model.getNumCardsPerHand());
        timer.start();

    }
}


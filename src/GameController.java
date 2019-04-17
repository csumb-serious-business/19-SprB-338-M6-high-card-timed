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

    /**
     * @return cannotPlayListener of the GameControl
     */
    public ActionListener getCannotPlayListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Cannot Play")) {
                    view.playerCannotPlay();
                }
            }
        };
    }

    /**
     * @return model of the GameControl
     */
    public Game getModel() {
        return model;
    }

    /**
     * @return view of the GameControl
     */
    public GameView getView() {
        return view;
    }

    /**
     * @return timerListener of the GameControl
     */
    public ActionListener getTimerListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("stop")) {
                    view.getTimerStop().setText("start");
                } else {
                    view.getTimerStop().setText("stop");
                }
            }
        };
    }

    /**
     * @return handListener of the GameControl
     */
    public ActionListener getHandListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int humanIndex = Integer.parseInt(e.getActionCommand());

                // cards are ordered by value
                Card humanCard = model.getHand(1).playCard(humanIndex);
                Card botCard = model.getHand(0).playCard(humanIndex);

                // If the bot has a card higher, should we do something?
                view.displayGame(humanCard, botCard);
                view.getTable().repaint();
            }
        };
    }


    public void startGame() {

        // shuffle and deal into the hands.
        model.deal();

        //view.displayGame(null, null); // Start off with nothing selected
        view.addLabelsForPlayers();
        view.addLabelsForTimer();

        view.addLabelsForCannotPlay();
        //view.addLabelsForStartGame();

        view.getTable().setVisible(true);
        timer.start();

    }
}


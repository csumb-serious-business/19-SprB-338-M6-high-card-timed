import javax.swing.*;
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
                    view.updateScore();
                    // TODO AI to play here
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
                String[] actionCommand = e.getActionCommand().split("\\|");
                Card.FaceValue stackValue = Card.FaceValue.valueOf(actionCommand[0]);
                boolean onLeftStack = Boolean.valueOf(actionCommand[1]);

                // get player's selected card, if any
                JButton[] playerHand = view.getBtnsPlayerHand();

                int found = -1;
                for (int i = 0; i < playerHand.length; i++) {
                    JButton card = playerHand[i];
                    if (!card.isEnabled()) {
                        found = i;
                        break;
                    }
                }

                if (found >= 0) {
                    Card playerCard = model.getHand(1).inspectCard(found);

                    if (model.canPlay(playerCard, onLeftStack)) {
                        model.getHand(1).playCard(found);
                        view.cardPlayed(playerCard, onLeftStack);
                    }
//                Card botCard = model.getHand(0).playCard(playerIndex);

                }
            }
        };
    }

    public ActionListener selectCardListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int playerIndex = Integer.parseInt(e.getActionCommand());
                JButton[] playerHand = view.getBtnsPlayerHand();
                playerHand[playerIndex].setEnabled(false);
                for (int i = 0; i < playerHand.length; i++) {
                    if (i != playerIndex) {
                        playerHand[i].setEnabled(true);
                    }
                }
            }
        };
    }

    public void startGame() {

        // shuffle and deal into the hands.
        model.deal();

        //view.cardPlayed(null, null); // Start off with nothing selected
        view.build(model.getTitle(), model.getNumCardsPerHand(), model.getLeftStack(), model.getRightStack());
        timer.start();

    }
}


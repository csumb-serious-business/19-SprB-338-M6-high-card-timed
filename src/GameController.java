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
                    aiPlay();
                    //todo add both skip -> deal from deck to both stacks
                    //todo add empty deck -> count min skip to winner logic
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
                    playerPlay(found, onLeftStack);


                }
                //todo add empty deck -> count min skip to winner logic

            }
        };
    }

    private void playerPlay(int playerCardIndex, boolean onLeftStack) {
        Hand playerHand = model.getHand(1);
        Card playerCard = playerHand.inspectCard(playerCardIndex);
        if (model.canPlay(playerCard, onLeftStack)) {
            Card played = playerHand.playCard(playerCardIndex);

            view.cardPlayed(playerCard, onLeftStack);
            if (onLeftStack) {
                model.playOnLeftStack(1, played);
            } else {
                model.playOnRightStack(1, played);
            }
        }
        aiPlay();

    }

    private void aiPlay() {
        Hand aiHand = model.getHand(0);
        Card toPlay;
        Card played = null;
        for (int i = 0; i < model.getHand(0).getNumCards(); i++) {
            toPlay = aiHand.inspectCard(i);
            if (model.canPlay(toPlay, true)) {
                played = aiHand.playCard(i);
                model.playOnLeftStack(0, played);
                view.cardPlayed(played, true);
            } else if (model.canPlay(toPlay, false)) {
                played = aiHand.playCard(i);
                model.playOnRightStack(0, played);
                view.cardPlayed(played, false);
            }
        }
        if (played == null) {
            //todo add skips
        }
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


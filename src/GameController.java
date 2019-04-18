import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controls interactions between a game and its UI
 */
public class GameController {
    private final Game model;
    private final GameView view;
    private final Timer timer;

    /**
     * Create a controller for a given game/view pair
     *
     * @param game the game for this controller
     * @param view the view to control
     */
    public GameController(Game game, GameView view) {
        model = game;

        this.view = view;
        view.setController(this);

        this.timer = new Timer(this);
    }

    /**
     * @return the player's hand
     */
    public Hand getPlayerHand() {
        return model.getHand(1);

    }

    /**
     * @return the AI player's hand
     */
    public Hand getAiHand() {
        return model.getHand(0);
    }

    /**
     * @return the number of skips from the player
     */
    public int getPlayerSkips() {
        return model.getPlayerSkips();
    }


    /**
     * @return the number of skips for the AI player
     */
    public int getAiSkips() {
        return model.getAiSkips();
    }

    /**
     * updates the timer display
     *
     * @param time the time to use
     * @return true if the update was successful
     */
    public boolean changeTimerDisplay(int time) {
        return view.changeTimerDisplay(time);
    }


    /**
     * @return cannotPlayListener of the GameControl
     */
    public ActionListener getSkipTurnListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Skip Turn")) {
                    skipTurn();
                }
            }
        };
    }

    /**
     * deals a card onto each stack in the game
     */
    private void dealFromDeck() {
        model.deckToStacks();
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
                    takeTurn(found, onLeftStack);
                }
            }
        };
    }

    /**
     * Checks whether the winning condition has been met
     * if it has, updates the UI to reflect that
     */
    private void evalWinCon() {
        if (model.getNumCardsRemainingInDeck() <= 1) {
            String winner;
            int skips;
            if (model.getPlayerSkips() == model.getAiSkips()) {
                view.updateScore("GAME OVER! %s  having only skipped %s times.");
                return;
            } else if (model.getPlayerSkips() < model.getAiSkips()) {
                winner = "You win,";
                skips = getPlayerSkips();

            } else {
                winner = "The Computer wins,";
                skips = getAiSkips();
            }
            String message = String.format("GAME OVER! %s having only skipped %s times.", winner, skips);
            view.updateScore(message);
        }
    }

    /**
     * plays a card onto one stack
     *
     * @param playerCardIndex the index of the card to play
     * @param onLeftStack     true if the card is played on the left stack
     */
    private void playerPlay(int playerCardIndex, boolean onLeftStack) {
        Hand playerHand = model.getHand(1);
        Card playerCard = playerHand.inspectCard(playerCardIndex);
        if (model.canPlay(playerCard, onLeftStack)) {
            Card played = playerHand.playCard(playerCardIndex);

            if (onLeftStack) {
                model.playOnLeftStack(1, played);
            } else {
                model.playOnRightStack(1, played);
            }

            view.updateViews(playerCard, onLeftStack);
        }
    }

    /**
     * The AI player plays a card onto one stack
     *
     * @return true if a card was played
     */
    private boolean aiPlay() {
        Hand aiHand = model.getHand(0);
        Card toPlay;
        Card played = null;
        for (int i = 0; i < model.getHand(0).getNumCards(); i++) {
            toPlay = aiHand.inspectCard(i);
            if (model.canPlay(toPlay, true)) {
                played = aiHand.playCard(i);
                model.playOnLeftStack(0, played);
                view.updateViews(played, true);
                break;
            } else if (model.canPlay(toPlay, false)) {
                played = aiHand.playCard(i);
                model.playOnRightStack(0, played);
                view.updateViews(played, false);
                break;
            }
        }
        if (played == null) {
            model.skipAi();
        }
        return played != null;
    }

    /**
     * @return an action listener for when a card
     * is selected by the player in the UI
     */
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

    /**
     * @return the number of cards remaining in the deck
     */
    public int getCardsInDeck() {
        return model.getNumCardsRemainingInDeck();
    }

    private void takeTurn(int playerCardIndex, boolean onLeftStack) {
        playerPlay(playerCardIndex, onLeftStack);
        aiPlay();
        evalWinCon();
    }

    /**
     * skips a players turn, and lets the AI player take a turn
     */
    private void skipTurn() {
        model.skipPlayer();

        boolean played = aiPlay();
        if (!played) {
            model.skipAi();
            if (model.isPlayerSkipped()) {
                dealFromDeck();
                view.updateStacks(model.getLeftStack(), model.getRightStack());
            }
        }

        view.updateScore();
        evalWinCon();
    }

    /**
     * starts running the game
     */
    public void startGame() {

        // shuffle and deal into the hands.
        model.deal();

        //view.updateStack(null, null); // Start off with nothing selected
        view.build(model.getTitle(), model.getNumCardsPerHand(), model.getLeftStack(), model.getRightStack());
        timer.start();

    }


}


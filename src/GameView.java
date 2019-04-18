import javax.swing.*;
import java.awt.*;

/**
 * This is the UI view for the game
 */
class GameView extends JFrame {
    private GameController controller;
    private JPanel pnlHandAi;
    private JPanel pnlHandPlayer;
    private JPanel pnlPlayArea;
    private JPanel pnlSkipTurn;
    private JButton btnStopTimer;
    private JLabel txtTimerTime;
    private JLabel txtNotifications;
    private JPanel pnlLeftStack;
    private JPanel pnlRightStack;
    private JButton[] btnsPlayerHand;

    /**
     * Simply calls super, call build after setting the controller
     * to render completely wired UI
     */
    public GameView() {
        super();
    }

    /**
     * @return btnsPlayerHand of the GameView
     */
    public JButton[] getBtnsPlayerHand() {
        return btnsPlayerHand;
    }

    /**
     * After creating this game view, build assembles
     * all the parts an wires them up to the controller
     *
     * @param title           the tile for this view
     * @param numCardsPerHand the number cards in each hand
     * @param leftStackCard   the card on top of the left card stack
     * @param rightStackCard  the card on top of the right card stack
     */
    public void build(String title, int numCardsPerHand, Card leftStackCard, Card rightStackCard) {
        setTitle(title);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        //--- Top panel -- AI hand (face down) ------------------------------\\
        pnlHandAi = new JPanel();
        pnlHandAi.setLayout(new GridLayout(1, numCardsPerHand));

        add(pnlHandAi, BorderLayout.NORTH);

        //--- Middle panel -- play area -------------------------------------\\
        pnlPlayArea = new JPanel();

        pnlLeftStack = new JPanel();
        pnlRightStack = new JPanel();
        pnlPlayArea.add(pnlLeftStack, BorderLayout.WEST);
        pnlPlayArea.add(pnlRightStack, BorderLayout.EAST);

        add(pnlPlayArea, BorderLayout.CENTER);

        //--- Bottom panel -- player's controls & notifications -------------\\
        JPanel pnlPlayer = new JPanel();
        pnlPlayer.setLayout(new BorderLayout());

        // player hand
        pnlHandPlayer = new JPanel();
        pnlHandPlayer.setLayout(new GridLayout(1, numCardsPerHand));
        pnlPlayer.add(pnlHandPlayer, BorderLayout.NORTH);

        // status notifications & controls
        // skip | notifications | timer & button
        JPanel pnlStatus = new JPanel();
        pnlStatus.setLayout(new BorderLayout());

        pnlSkipTurn = new JPanel();
        pnlStatus.add(pnlSkipTurn, BorderLayout.WEST);

        JButton btnSkipTurn = new JButton("Skip Turn");
        pnlSkipTurn.add(btnSkipTurn);
        btnSkipTurn.addActionListener(controller.getSkipTurnListener());


        txtNotifications = new JLabel();
        pnlStatus.add(txtNotifications, BorderLayout.CENTER);

        JPanel pnlTimer = new JPanel();
        pnlStatus.add(pnlTimer, BorderLayout.EAST);

        btnStopTimer = new JButton("stop");
        btnStopTimer.addActionListener(controller.getTimerListener());

        txtTimerTime = new JLabel("0");
        pnlTimer.add(txtTimerTime);
        pnlTimer.add(btnStopTimer);


        pnlPlayer.add(pnlStatus, BorderLayout.SOUTH);
        add(pnlPlayer, BorderLayout.SOUTH);

        addPlayerHands();
        updateStack(leftStackCard, true);
        updateStack(rightStackCard, false);

        setVisible(true);
    }

    /**
     * sets controller of the GameView
     *
     * @param controller the game controller to wire into this game view
     */
    public void setController(GameController controller) {
        this.controller = controller;
    }

    /**
     * Validate the panels in the CardTable
     *
     * @return true if successful
     */
    private boolean validateAll() {
        pnlPlayArea.validate();
        pnlHandAi.validate();
        pnlHandPlayer.validate();
        pnlSkipTurn.validate();
        super.validate();
        return true;
    }

    /**
     * populates UI buttons for a given hand
     *
     * @param hand the hand to display
     * @return an array of buttons to place into a UI container.
     */
    public JButton[] buildHandButtons(Hand hand) {
        JButton[] buttons = new JButton[hand.getNumCards()];

        for (int i = 0; i < hand.getNumCards(); i++) {
            Card card = hand.inspectCard(i);
            JButton button = new JButton(CardViewBuilder.getIcon(card));
            button.addActionListener(controller.selectCardListener());
            button.setActionCommand(String.valueOf(i));
            buttons[i] = button;
        }

        return buttons;
    }


    /**
     * populates UI labels for a given hand
     *
     * @param hand     the hand to display
     * @param isFaceUp whether the cards are face up or face down in the UI
     * @return an array of labels to place into a UI container
     */
    private JLabel[] buildHandLabels(Hand hand, boolean isFaceUp) {
        JLabel[] handLabels = new JLabel[hand.getNumCards()];

        for (int i = 0; i < hand.getNumCards(); i++) {
            JLabel label;
            if (isFaceUp) {
                label = new JLabel(CardViewBuilder.getIcon(hand.inspectCard(i)));
            } else {
                label = new JLabel(CardViewBuilder.getBackCardIcon());
            }
            handLabels[i] = label;
        }
        return handLabels;
    }

    /**
     * Adds labels for CardTable's players
     *
     * @return true if successful
     */
    public boolean addPlayerHands() {
        btnsPlayerHand = buildHandButtons(controller.getPlayerHand());
        for (JButton button : btnsPlayerHand) {
            pnlHandPlayer.add(button);
        }

        JLabel[] lblsAiHand = buildHandLabels(controller.getAiHand(), false);
        for (JLabel label : lblsAiHand) {
            pnlHandAi.add(label);
        }

        return true;
    }

    /**
     * try to play a card on a stack
     *
     * @param onLeftStack true if the card is to be played on the left stack
     */
    private void updateStack(Card card, boolean onLeftStack) {
        // update play area
        JButton btnChosen = new JButton(CardViewBuilder.getIcon(card));
        btnChosen.addActionListener(controller.playCardListener());

        // update the correct stack
        if (onLeftStack) {
            pnlLeftStack.removeAll();
            btnChosen.setActionCommand(card.getValue().name() + "|" + true);
            pnlLeftStack.add(btnChosen);
        } else {
            pnlRightStack.removeAll();
            btnChosen.setActionCommand(card.getValue().name() + "|" + false);
            pnlRightStack.add(btnChosen);
        }
    }

    /**
     * Updates both stacks of cards given a pair of cards
     *
     * @param leftCard  the card to place on the left stack
     * @param rightCard the card to place on the right stack
     */
    public void updateStacks(Card leftCard, Card rightCard) {
        updateStack(leftCard, true);
        updateStack(rightCard, false);
    }


    /**
     * updates the ui for player / ai hands
     */
    private void updateHands() {
        pnlHandPlayer.removeAll();
        pnlHandAi.removeAll();

        addPlayerHands();
    }

    /**
     * updates all the view panels after updating
     * a play stack with a given card
     *
     * @param card        the card to play on the stack
     * @param onLeftStack true if the card is intended to
     *                    be played on the left stack
     */
    public void updateViews(Card card, boolean onLeftStack) {
        updateStack(card, onLeftStack);
        updateHands();
        updateScore();
        validateAll();
    }


    /**
     * updates the score area of the UI
     */
    public void updateScore() {
        String message = String.format(
                "Player skips: %s, AI skips: %s, Cards in Deck: %s",
                controller.getPlayerSkips(),
                controller.getAiSkips(),
                controller.getCardsInDeck());

        updateScore(message);
    }

    /**
     * updates the score area of the UI with a given message
     *
     * @param message the message to use
     */
    public void updateScore(String message) {
        txtNotifications.setText(message);

    }


    /**
     * updates the timer value in the UI
     *
     * @param time the current timer value
     * @return true if successful
     */
    public boolean changeTimerDisplay(int time) {
        txtTimerTime.setText(Integer.toString(time));
        txtTimerTime.repaint();
        return true;
    }


    /**
     * toggles the timer state and button
     *
     * @param state the timer state, true if running
     */
    public void timerState(boolean state) {
        if (state) {
            // start the txtTimerTime and set button text to stop
            btnStopTimer.setText("stop");
        } else {
            btnStopTimer.setText("start");
        }
    }


}

import javax.swing.*;
import java.awt.*;

/**
 * todo: add desc
 *
 * @author todo
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
    private JButton btnSkipTurn;
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
     * @return pnlHandPlayer of the GameView
     */
    public JPanel getPnlHandPlayer() {
        return pnlHandPlayer;
    }

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

        btnSkipTurn = new JButton("Skip Turn");
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
        cardPlayed(leftStackCard, true);
        cardPlayed(rightStackCard, false);

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
    public void cardPlayed(Card card, boolean onLeftStack) {

        // update hands
        pnlHandPlayer.removeAll();
        pnlHandAi.removeAll();

        addPlayerHands();

        // update play area
        JButton btnChosen = new JButton(CardViewBuilder.getIcon(card));
        btnChosen.addActionListener(controller.playCardListener());

        // update the correct stack
        if (onLeftStack) {
            pnlLeftStack.removeAll();
            pnlLeftStack.add(btnChosen);
            btnChosen.setActionCommand(card.getValue().name() + "|" + true);
        } else {
            pnlRightStack.removeAll();
            pnlRightStack.add(btnChosen);
            btnChosen.setActionCommand(card.getValue().name() + "|" + false);
        }


        updateScore();

        validateAll();

    }

    public void updateScore() {
        String message = String.format("Player skips: %s, AI skips: %s", controller.getPlayerSkips(), controller.getAiSkips());
        txtNotifications.setText(message);
    }

    public void updateScore(String message) {
        txtNotifications.setText(message);

    }


    public boolean changeTimerDisplay(int time) {
        txtTimerTime.setText(Integer.toString(time));
        txtTimerTime.repaint();
        return true;
    }


    public void timerState(boolean state) {
        if (state) {
            // start the txtTimerTime and set button text to stop
            btnStopTimer.setText("stop");
        } else {
            btnStopTimer.setText("start");
        }
    }


}

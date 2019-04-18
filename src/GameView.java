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
    private JLabel[] lblPlayedCard;
    private JButton btnSkipTurn;

    /**
     * Simply calls super, call build after setting the controller
     * to render completely wired UI
     */
    public GameView() {
        super();
    }

    public void build(String title, int numCardsPerHand, int numPlayers) {
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
        pnlPlayArea.setLayout(new GridLayout(2, numPlayers));

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

        setVisible(true);
    }

    /**
     * sets controller of the GameView
     *
     * @param controller todo
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
     * Clears the panels in the CardTable
     *
     * @return true if successful
     */
    public boolean removeAllPanels() {
        pnlHandAi.removeAll();
        pnlHandPlayer.removeAll();
        pnlPlayArea.removeAll();
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
            button.addActionListener(controller.playCardListener());
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
        JButton[] btnsPlayerHand = buildHandButtons(controller.getPlayerHand());
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
     * The main loop for this Card Game
     *
     * @param playerChoice   the player's chosen card
     * @param computerChoice the computer's chosen card
     */
    public void takeTurn(Card playerChoice, Card computerChoice) {
        String playerPrompt = "Test";
        String computerPrompt = "Test1";
        String tieCountPrompt = "";

        // clear everything
        removeAllPanels();

        Hand playHand = new Hand();
        if (playerChoice != null && computerChoice != null) {
            playHand.takeCard(computerChoice);
            playHand.takeCard(playerChoice);
            lblPlayedCard = buildHandLabels(playHand, true);
            playerPrompt = "[Status]User Cannot Plays: " + controller.getPlayerSkips();
            computerPrompt = "[Status]Computer Cannot Plays: " + controller.getAiSkips();
        }

        //TODO: Add cards to table first but cant because of null check
        if (lblPlayedCard[0] != null || lblPlayedCard[1] != null) {
            pnlPlayArea.add(lblPlayedCard[0]);
            pnlPlayArea.add(lblPlayedCard[1]);
            addPlayerHands();
        }

        pnlPlayArea.add(new JLabel(computerPrompt, SwingConstants.HORIZONTAL));
        pnlPlayArea.add(new JLabel(playerPrompt + "\n" + tieCountPrompt, SwingConstants.HORIZONTAL));

        validateAll();
        // If something goes wrong we know because this will return false

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

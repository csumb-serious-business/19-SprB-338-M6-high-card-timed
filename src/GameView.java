import javax.swing.*;
import java.awt.*;

/**
 * todo: add desc
 *
 * @author todo
 */
class GameView extends JFrame {
    private JPanel pnlHandAi;
    private JPanel pnlHandPlayer;

    private JPanel pnlPlayArea;

    private JPanel pnlTimer;
    private JLabel txtTimerTime;
    private JButton btnStopTimer;

    private JPanel pnlSkipTurn;

    private JLabel[] lblPlayedCard;


    private CardView cardView;
    private GameController controller;

    /**
     * Arranges panels for the card table
     *
     * @param title           the name of the game played on this table
     * @param numCardsPerHand the max number of per player hand
     * @param numPlayers      the number of players for this game
     */
    public GameView(String title, int numCardsPerHand, int numPlayers) {
        super();


        setSize(1150, 650);
        setTitle(title);

        setLayout(new BorderLayout());

        pnlHandAi = new JPanel();
        pnlHandAi.setLayout(new GridLayout(1, numCardsPerHand));
        add(pnlHandAi, BorderLayout.NORTH);

        pnlPlayArea = new JPanel();
        pnlPlayArea.setLayout(new GridLayout(2, numPlayers));
        add(pnlPlayArea, BorderLayout.CENTER);

        pnlHandPlayer = new JPanel();
        pnlHandPlayer.setLayout(new GridLayout(1, numCardsPerHand));
        add(pnlHandPlayer, BorderLayout.SOUTH);

        pnlTimer = new JPanel();
        pnlTimer.setLayout(new FlowLayout());
        add(pnlTimer, BorderLayout.EAST);

        pnlSkipTurn = new JPanel();
        pnlSkipTurn.setLayout(new FlowLayout());
        add(pnlSkipTurn, BorderLayout.WEST);


        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);

        cardView = new CardView();

    }

    public boolean addLabelsForTimer() {
        btnStopTimer = new JButton("stop");
        btnStopTimer.addActionListener(controller.getTimerListener());
        txtTimerTime = new JLabel("0");
        pnlTimer.add(txtTimerTime);
        pnlTimer.add(btnStopTimer);
        return true;
    }

    public boolean addLabelsForCannotPlay() {
        JButton btnSkipTurn = new JButton("Cannot Play");
        pnlSkipTurn.add(btnSkipTurn);
        btnSkipTurn.addActionListener(controller.getCannotPlayListener());
        return true;
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

        //table.pnlSkipTurn.removeAll();
        return true;
    }


    public boolean setController(GameController controller) {
        this.controller = controller;
        return true;
    }

    /**
     * populates UI labels for a given hand
     *
     * @param hand     the hand to display
     * @param isFaceUp whether the cards are face up or face down in the UI
     * @return an array of labels to place into a UI container
     */
    private JLabel[] populateLabels(Hand hand, boolean isFaceUp) {
        JLabel[] handLabels = new JLabel[hand.getNumCards()];


        for (int i = 0; i < hand.getNumCards(); i++) {
            JLabel label;
            if (isFaceUp) {
                label = new JLabel(cardView.getIcon(hand.inspectCard(i)));
            } else {
                label = new JLabel(cardView.getBackCardIcon());
            }
            handLabels[i] = label;
        }
        return handLabels;
    }

    /**
     * //   * Adds labels for CardTable's players
     * //   *
     * //   * @return true if successful
     * //
     */
    public boolean addLabelsForPlayers() {
        JButton[] btnsPlayerHand = populateButtons(controller.getPlayerHand());
        JLabel[] lblsAiHand = populateLabels(controller.getAiHand(), false);

        for (JButton button : btnsPlayerHand) {
            pnlHandPlayer.add(button);
        }

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
            lblPlayedCard = populateLabels(playHand, true);
            playerPrompt = "[Status]User Cannot Plays: " + controller.getPlayerSkips();
            computerPrompt = "[Status]Computer Cannot Plays: " + controller.getAiSkips();
        }

        //TODO: Add cards to table first but cant because of null check
        if (lblPlayedCard[0] != null || lblPlayedCard[1] != null) {
            pnlPlayArea.add(lblPlayedCard[0]);//start throwing cards out
            pnlPlayArea.add(lblPlayedCard[1]);
            addLabelsForPlayers();
        }

        pnlPlayArea.add(new JLabel(computerPrompt, SwingConstants.HORIZONTAL));
        pnlPlayArea.add(new JLabel(playerPrompt + "\n" + tieCountPrompt, SwingConstants.HORIZONTAL));

        validateAll();
        // If something goes wrong we know because this will return false

    }

    /**
     * populates UI buttons for a given hand
     *
     * @param hand the hand to display
     * @return an array of buttons to place into a UI container.
     */
    public JButton[] populateButtons(Hand hand) {
        JButton[] buttons = new JButton[hand.getNumCards()];

        for (int i = 0; i < hand.getNumCards(); i++) {
            Card card = hand.inspectCard(i);
            JButton button = new JButton(cardView.getIcon(card));
            button.addActionListener(controller.playCardListener());
            button.setActionCommand(String.valueOf(i));
            buttons[i] = button;
        }

        return buttons;
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

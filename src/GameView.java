import javax.swing.*;

public class GameView {
    //todo remove these
    private static final int NUM_CARDS_PER_HAND = 7;
    private static final int NUM_PLAYERS = 2;
    private JButton cannotPlayButton;
    private int playerCannotPlayCount;
    private int computerCannotPlayCount;
    private JTextField timer;
    private JButton timerStop;
    private TableView table;
    private CardView cardView;
    private JLabel[] playedCardLabels;
    private JLabel[] botLabels;
    private JButton[] userLabels;
    private GameController controller;

    public GameView() {
        playerCannotPlayCount = 0;
        computerCannotPlayCount = 0;

        table = new TableView("Timed Card Game", NUM_CARDS_PER_HAND, NUM_PLAYERS);
        table.setSize(800, 600);
        table.setLocationRelativeTo(null);
        table.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        table.setVisible(true);

        cardView = new CardView();
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

    public int playerCannotPlay() {
        return playerCannotPlayCount++;

    }

    public int computerCannotPlay() {
        return computerCannotPlayCount++;
    }

    public boolean changeTimerDisplay(int time) {
        timer.setText(Integer.toString(time));
        timer.repaint();
        return true;
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
            button.addActionListener(controller.getHandListener());
            button.setActionCommand(String.valueOf(i));
            buttons[i] = button;
        }

        return buttons;
    }

    public boolean setController(GameController controller) {
        this.controller = controller;
        return true;
    }

    /**
     * @return timerStop of the GameView
     */
    public JButton getTimerStop() {
        return timerStop;
    }

    /**
     * Validate the panels in the CardTable
     *
     * @return true if successful
     */
    private boolean validateAll() {
        table.pnlPlayArea.validate();
        table.pnlComputerHand.validate();
        table.pnlHumanHand.validate();
        table.pnlCannotPlay.validate();
        table.validate();
        return true;
    }

    public boolean addLabelsForTimer() {
        timerStop = new JButton("stop");
        timerStop.addActionListener(controller.getTimerListener());
        timer = new JTextField("0", 6);
        timer.setEditable(false);
        table.pnlTimer.add(timer);
        table.pnlTimer.add(timerStop);
        return true;
    }

    public boolean addLabelsForCannotPlay() {
        cannotPlayButton = new JButton("Cannot Play");
        table.pnlCannotPlay.add(cannotPlayButton);
        cannotPlayButton.addActionListener(controller.getCannotPlayListener());
        //leftPanel[0]=cannotPlayButton;
        return true;
    }

    /**
     * Clears the panels in the CardTable
     *
     * @return true if successful
     */
    private boolean removeAll() {
        table.pnlComputerHand.removeAll();
        table.pnlHumanHand.removeAll();
        table.pnlPlayArea.removeAll();

        //table.pnlCannotPlay.removeAll();
        return true;
    }

    /**
     * @return table of the GameView
     */
    public TableView getTable() {
        return table;
    }

    /**
     * The main loop for this Card Game
     *
     * @param playerChoice   the player's chosen card
     * @param computerChoice the computer's chosen card
     */
    public void displayGame(Card playerChoice, Card computerChoice) {
        String playerPrompt = "Test";
        String computerPrompt = "Test1";
        String tieCountPrompt = "";

        // clear everything
        removeAll();

        Hand playHand = new Hand();
        if (playerChoice != null && computerChoice != null) {
            playHand.takeCard(computerChoice);
            playHand.takeCard(playerChoice);
            playedCardLabels = populateLabels(playHand, true);

            //TODO Rules:
            //			if (compare(playerChoice, computerChoice) >= 1) {
            //				playerWinCount++;
            //			} else if (compare(playerChoice, computerChoice) == 0) {
            //				tieCount++;
            //			} else if (compare(playerChoice, computerChoice) <= -1) {
            //				computerWinCount++;
            //			}

            playerPrompt = "[Status]User Cannot Plays: " + playerCannotPlayCount;
            computerPrompt = "[Status]Computer Cannot Plays: " + computerCannotPlayCount;
        }

        //TODO: Add cards to table first but cant because of null check
        if (playedCardLabels[0] != null || playedCardLabels[1] != null) {
            table.pnlPlayArea.add(playedCardLabels[0]);//start throwing cards out
            table.pnlPlayArea.add(playedCardLabels[1]);
            addLabelsForPlayers();
        }

        table.pnlPlayArea.add(new JLabel(computerPrompt, 0));
        table.pnlPlayArea.add(new JLabel(playerPrompt + "\n" + tieCountPrompt, 0));

        validateAll();
        // If something goes wrong we know because this will return false

    }

    /**
     * //   * Adds labels for CardTable's players
     * //   *
     * //   * @return true if successful
     * //
     */
    public boolean addLabelsForPlayers() {
        Game model = controller.getModel();
        userLabels = populateButtons(model.getHand(1));
        botLabels = populateLabels(model.getHand(0), false);

        for (JButton button : userLabels) {
            table.pnlHumanHand.add(button);
        }

        for (JLabel label : botLabels) {
            table.pnlComputerHand.add(label);
        }

        return true;

    }
}



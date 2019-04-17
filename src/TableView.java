import javax.swing.*;
import java.awt.*;

/**
 * todo: add desc
 *
 * @author todo
 */
class TableView extends JFrame {
    static int MAX_CARDS_PER_HAND = 56;
    static int MAX_PLAYERS = 2;
    public JPanel pnlComputerHand;
    public JPanel pnlHumanHand;
    public JPanel pnlPlayArea;
    public JPanel pnlTimer;

    public JPanel pnlCannotPlay;

    private int numCardsPerHand;
    private int numPlayers;

    /**
     * Arranges panels for the card table
     *
     * @param title           the name of the game played on this table
     * @param numCardsPerHand the max number of per player hand
     * @param numPlayers      the number of players for this game
     */
    public TableView(String title, int numCardsPerHand, int numPlayers) {
        super();

        if (numPlayers > MAX_PLAYERS) {
            numPlayers = MAX_PLAYERS;
        }

        if (numCardsPerHand > MAX_CARDS_PER_HAND) {
            numCardsPerHand = MAX_CARDS_PER_HAND;
        }

        this.numPlayers = numPlayers;
        this.numCardsPerHand = numCardsPerHand;

        setSize(1150, 650);
        setTitle(title);

        setLayout(new BorderLayout());

        pnlComputerHand = new JPanel();
        pnlComputerHand.setLayout(new GridLayout(1, numCardsPerHand));
        add(pnlComputerHand, BorderLayout.NORTH);

        pnlPlayArea = new JPanel();
        pnlPlayArea.setLayout(new GridLayout(2, numPlayers));
        add(pnlPlayArea, BorderLayout.CENTER);

        pnlHumanHand = new JPanel();
        pnlHumanHand.setLayout(new GridLayout(1, numCardsPerHand));
        add(pnlHumanHand, BorderLayout.SOUTH);

        pnlTimer = new JPanel();
        pnlTimer.setLayout(new FlowLayout());
        add(pnlTimer, BorderLayout.EAST);

        pnlCannotPlay = new JPanel();
        pnlCannotPlay.setLayout(new FlowLayout());
        add(pnlCannotPlay, BorderLayout.WEST);
    }

    /**
     * @return gets the number of players in this game
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * @return gets the maximum number of cards in each hand
     */
    public int getNumCardsPerHand() {
        return numCardsPerHand;
    }

}

/**
 * todo: add desc
 *
 * @author todo
 */
public class Game {
    public static final int DEFAULT_NUM_PLAYERS = 2;
    private static final int DEFAULT_CARDS_PER_HAND = 12; //7; todo revert
    private static final int MAX_PLAYERS = 50;
    private static final String DEFAULT_TITLE = "Timed Card Game";
    private String title;
    private int numPlayers;
    private int numCardsPerHand; // count of cards dealt to each player
    private Deck deck;
    private Hand[] hand; //-------/ each player's hand
    private Card leftStack; //----/ top card on the left stack
    private Card rightStack; //---/ top card on the right stack
    private int playerSkips;
    private boolean playerSkipped;
    private boolean aiSkipped;
    private int aiSkips;

    public Game(Deck deck, String title, int numPlayers, int numCardsPerHand) {
        this.deck = deck;
        this.title = title;

        // players
        if (1 <= numPlayers && numPlayers <= MAX_PLAYERS) {
            this.numPlayers = numPlayers;
        } else {
            this.numPlayers = 4;
        }

        // hand sizes
        if (1 <= numCardsPerHand && numCardsPerHand <= (deck.getCardCount() / numPlayers)) {
            this.numCardsPerHand = numCardsPerHand;
        } else {
            this.numCardsPerHand = deck.getCardCount() / numPlayers;
        }

        // hand
        this.hand = new Hand[numPlayers];
        for (int i = 0; i < numPlayers; i++)
            this.hand[i] = new Hand();


        // prepare deck and shuffle
        playerSkips = 0;
        playerSkipped = false;
        aiSkips = 0;
        aiSkipped = false;

        newGame();
    }

    public Game() {
        this(Deck.DEFAULT_DECK, DEFAULT_TITLE, DEFAULT_NUM_PLAYERS, DEFAULT_CARDS_PER_HAND);
    }

    /**
     * sets leftStack of the Game
     *
     * @param toPlay todo
     */
    public void playOnLeftStack(int player, Card toPlay) {
        if (canPlay(toPlay, true)) {
            this.leftStack = toPlay;
            hand[player].takeCard(deck.dealCard());

        }
    }

    /**
     * @return leftStack of the Game
     */
    public Card getLeftStack() {
        return leftStack;
    }

    /**
     * @return rightStack of the Game
     */
    public Card getRightStack() {
        return rightStack;
    }


    /**
     * sets rightStack of the Game
     *
     * @param toPlay todo
     */
    public void playOnRightStack(int player, Card toPlay) {
        if (canPlay(toPlay, false)) {
            this.rightStack = toPlay;
            if (player == 0) {
                aiSkipped = false;
            } else {
                playerSkipped = false;
            }
            hand[player].takeCard(deck.dealCard());
        }
    }

    /**
     * @return title of the Game
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return numPlayers of the Game
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * @return numCardsPerHand of the Game
     */
    public int getNumCardsPerHand() {
        return numCardsPerHand;
    }

    /**
     * @return playerSkips of the Game
     */
    public int getPlayerSkips() {
        return playerSkips;
    }

    /**
     * @return aiSkips of the Game
     */
    public int getAiSkips() {
        return aiSkips;
    }

    public int skipPlayer() {
        playerSkipped = true;
        return playerSkips++;
    }

    public boolean isBothPlayersSkipped() {
        return playerSkipped && aiSkipped;
    }

    public int skipAi() {
        aiSkipped = true;
        return aiSkips++;
    }

    public Hand getHand(int k) {
        // on error return automatic empty hand
        if (0 <= k && k < numPlayers) {
            return hand[k];
        }
        return new Hand();
    }

    public Card getCardFromDeck() {
        return deck.dealCard();
    }

    public int getNumCardsRemainingInDeck() {
        return deck.getNumCards();
    }

    public void newGame() {

        // clear the hands
        for (int i = 0; i < numPlayers; i++) {
            hand[i].resetHand();
        }

        // restock & shuffle the deck
        deck.init();
        deck.shuffle();
    }

    public boolean deal() {
        // returns false if not enough cards, but deals what it can
        boolean enoughCards;

        // clear all hands
        for (int i = 0; i < numPlayers; i++)
            hand[i].resetHand();

        enoughCards = true;
        for (int i = 0; i < numCardsPerHand && enoughCards; i++) {
            for (int j = 0; j < numPlayers; j++) {
                if (deck.getNumCards() > 0) {
                    hand[j].takeCard(deck.dealCard());
                } else {
                    enoughCards = false;
                    break;
                }
            }
        }
        enoughCards = deck.getCardCount() >= 2;
        if (enoughCards) {
            leftStack = deck.dealCard();
            rightStack = deck.dealCard();
        }
        return enoughCards;
    }

    void sortHands() {
        for (int i = 0; i < numPlayers; i++) {
            hand[i].sort();
        }
    }

    public boolean canPlay(Card toPlay, boolean isLeftStack) {
        if (isLeftStack) {
            return toPlay.getValue().previous() == leftStack.getValue() ||
                    toPlay.getValue().next() == leftStack.getValue();
        }
        return toPlay.getValue().previous() == rightStack.getValue() ||
                toPlay.getValue().next() == rightStack.getValue();
    }
}

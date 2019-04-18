/**
 * todo: add desc
 *
 * @author todo
 */
public class Game {
    public static final int DEFAULT_NUM_CARDS_PER_HAND = 7;
    public static final int DEFAULT_NUM_PLAYERS = 2;
    private static final int DEFAULT_CARDS_PER_HAND = 7;
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
        aiSkips = 0;

        newGame();
    }
    public Game() {
        this(Deck.DEFAULT_DECK, DEFAULT_TITLE, DEFAULT_NUM_PLAYERS, DEFAULT_CARDS_PER_HAND);
    }

    /**
     * @return leftStack of the Game
     */
    public Card getLeftStack() {
        return leftStack;
    }

    //todo add click to select card, click on stack to play card.
    //todo add stack card value constraint
    //todo add draw on play logic
    //todo add ai play/skip logic
    //todo add skip turn functionality
    //todo add both skip -> deal from deck to both stacks
    //todo add empty deck -> count min skip to winner logic

    /**
     * @return rightStack of the Game
     */
    public Card getRightStack() {
        return rightStack;
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
        return playerSkips++;

    }

    public int skipAi() {
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

    Card playCard(int playerIndex, int cardIndex) {
        // card is ok
        if ((0 <= playerIndex && playerIndex <= (numPlayers - 1)) &&
                (0 <= cardIndex && cardIndex <= (numCardsPerHand - 1))) {
            // return the played card
            return hand[playerIndex].playCard(cardIndex);
        }

        // bad card, return invalid
        return new Card(null, Card.Suit.spades);
    }

    boolean takeCard(int playerIndex) {
        // Are there enough Cards?
        if (deck.getNumCards() <= 0) {
            return false;
        }

        // is the index ok?
        if (0 <= playerIndex && playerIndex <= (numPlayers - 1)) {
            return hand[playerIndex].takeCard(deck.dealCard());
        }

        return false;
    }
}

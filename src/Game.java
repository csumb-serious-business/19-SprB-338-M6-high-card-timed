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


    private int numPlayers;
    private int numCardsPerHand; //-----/ count of cards dealt to each player
    private Deck deck; //---------------/ holds the initial full deck and gets
    private Hand[] hand; //-------------/ each player's hand

    private int numJokersPerPack;

    private int playerSkips;
    private int aiSkips;

    public Game(Deck deck, int numPlayers, int numCardsPerHand) {
        this.deck = deck;

        // filter bad values
        if (1 <= numPlayers && numPlayers <= MAX_PLAYERS) {
            this.numPlayers = numPlayers;
        } else {
            this.numPlayers = 4;
        }

        // one of many ways to assure at least one full deal to each player
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
        this(Deck.DEFAULT_DECK, DEFAULT_NUM_PLAYERS, DEFAULT_CARDS_PER_HAND);
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
        // hands start from 0 like arrays

        // on error return automatic empty hand
        if (k < 0 || k >= numPlayers)
            return new Hand();

        return hand[k];
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

        // restock & suffle the deck
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

        return enoughCards;
    }

    void sortHands() {
        for (int i = 0; i < numPlayers; i++) {
            hand[i].sort();
        }
    }

    Card playCard(int playerIndex, int cardIndex) {
        // returns bad card if either argument is bad
        if (playerIndex < 0 || playerIndex > numPlayers - 1 || cardIndex < 0 || cardIndex > numCardsPerHand - 1) {
            // Creates a card that does not work
            return new Card(null, Card.Suit.spades);
        }

        // return the card played
        return hand[playerIndex].playCard(cardIndex);

    }

    boolean takeCard(int playerIndex) {
        // returns false if either argument is bad
        if (playerIndex < 0 || playerIndex > numPlayers - 1)
            return false;

        // Are there enough Cards?
        if (deck.getNumCards() <= 0)
            return false;

        return hand[playerIndex].takeCard(deck.dealCard());
    }
}

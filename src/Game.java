/**
 * todo: add desc
 *
 * @author todo
 */
public class Game {
    public static final int DEFAULT_NUM_CARDS_PER_HAND = 7;
    public static final int DEFAULT_NUM_PLAYERS = 2;
    private static final int DEFAULT_CARDS_PER_HAND = 7;
    private static final Card[] DEFAULT_UNUSED_CARDS = new Card[0];
    private static final int DEFAULT_PACKS_PER_DECK = 1;
    private static final int DEFAULT_JOKERS_PER_PACK = 0;
    private static final int DEFAULT_NUM_UNUSED_CARDS_PER_PACK = 0; // what is this for?
    private static final int MAX_PLAYERS = 50;

    private int numPlayers;
    private int numPacks; // # standard 52-card packs per deck

    // ignoring jokers or unused cards
    private int numJokersPerPack; // if 2 per pack & 3 packs per deck, get 6
    private int numCardsPerHand; // # cards to deal each player
    private Deck deck; // holds the initial full deck and gets

    // smaller (usually) during play
    private Hand[] hand; // one Hand for each player
    private Card[] unusedCardsPerPack; // an array holding the cards not used
    private int playerSkips;
    private int aiSkips;

    public Game(int numPacks,
                int numJokersPerPack,
                int numUnusedCardsPerPack,
                Card[] unusedCardsPerPack,
                int numPlayers,
                int numCardsPerHand) {
        int k;

        // filter bad values
        if (numPacks < 1 || numPacks > 6)
            numPacks = 1;
        if (numJokersPerPack < 0 || numJokersPerPack > 4)
            numJokersPerPack = 0;
        if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) // > 1 card
            numUnusedCardsPerPack = 0;
        if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
            numPlayers = 4;
        // one of many ways to assure at least one full deal to all players
        if (numCardsPerHand < 1 || numCardsPerHand > numPacks * (52 - numUnusedCardsPerPack) / numPlayers)
            numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;

        // allocate
        this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
        this.hand = new Hand[numPlayers];
        for (k = 0; k < numPlayers; k++)
            this.hand[k] = new Hand();
        deck = new Deck(numPacks);

        // assign to members
        this.numPacks = numPacks;
        this.numJokersPerPack = numJokersPerPack;
        this.numPlayers = numPlayers;
        this.numCardsPerHand = numCardsPerHand;
        for (k = 0; k < numUnusedCardsPerPack; k++)
            this.unusedCardsPerPack[k] = unusedCardsPerPack[k];
        // prepare deck and shuffle

        playerSkips = 0;
        aiSkips = 0;

        newGame();
    }

    public Game() {
        this(DEFAULT_PACKS_PER_DECK, DEFAULT_JOKERS_PER_PACK, DEFAULT_NUM_UNUSED_CARDS_PER_PACK,
                DEFAULT_UNUSED_CARDS, DEFAULT_NUM_PLAYERS, DEFAULT_CARDS_PER_HAND);
    }

    // in the game. e.g. pinochle does not
    // use cards 2-8 of any suit

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

        // restock the deck
        deck.init(numPacks);

        // remove unused cards
        for (int i = 0; i < DEFAULT_NUM_UNUSED_CARDS_PER_PACK; i++) {
            deck.removeCard(unusedCardsPerPack[i]);
        }

        // add jokers
        for (int i = 0; i < numPacks; i++) {
            for (int j = 0; j < numJokersPerPack; j++) {
                deck.addCard(new Card(Card.FaceValue.X, Card.Suit.values()[j]));
            }
        }
        // shuffle the cards
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

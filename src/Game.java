/**
 * A game where players take turns playing a card onto one of two stacks
 * the played card should have a face value one higher or lower than the
 * card on the stack. After each play the player takes a card from the deck
 * If a player has no plays they skip their turn.
 * If neither player has a play, a card is placed on each stack from the deck.
 * The game ends when the deck has no more cards in it. At that time the player
 * who has skipped the fewest number of turns wins.
 */
public class Game {
    public static final int DEFAULT_NUM_PLAYERS = 2;
    private static final int DEFAULT_CARDS_PER_HAND = 7;
    private static final int MAX_PLAYERS = 50;
    private static final String DEFAULT_TITLE = "Timed Card Game";
    private final String title;
    private final int numPlayers;
    private final int numCardsPerHand; // count of cards dealt to each player
    private final Deck deck;
    private final Hand[] hand; //-------/ each player's hand
    private Card leftStack; //----/ top card on the left stack
    private Card rightStack; //---/ top card on the right stack

    private int playerSkips;
    private boolean playerSkipped;
    private boolean aiSkipped;
    private int aiSkips;

    /**
     * Creates a new Game
     *
     * @param deck            the deck to use for this game
     * @param title           the game's name
     * @param numPlayers      the number of players who participate in this game
     * @param numCardsPerHand how many cards are dealt into the opening hands
     */
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

    /**
     * Creates a new game
     */
    public Game() {
        this(Deck.DEFAULT_DECK, DEFAULT_TITLE, DEFAULT_NUM_PLAYERS, DEFAULT_CARDS_PER_HAND);
    }

    /**
     * @return playerSkipped of the Game
     */
    public boolean isPlayerSkipped() {
        return playerSkipped;
    }

    /**
     * @return aiSkipped of the Game
     */
    public boolean isAiSkipped() {
        return aiSkipped;
    }

    /**
     * sets leftStack of the Game
     *
     * @param player the player who is playing onto the stack
     * @param toPlay the card to play
     */
    public void playOnLeftStack(int player, Card toPlay) {
        if (canPlay(toPlay, true)) {
            this.leftStack = toPlay;
            if (player == 0) {
                aiSkipped = false;
            } else {
                playerSkipped = false;
            }
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
     * @param player the player who is playing the card
     * @param toPlay the card to play onto the right stack
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

    /**
     * Skips the AI turn
     *
     * @return true if the turn was skipped
     */
    public boolean skipAi() {
        aiSkips++;
        aiSkipped = true;
        return true;
    }

    /**
     * gets the hand for a given player
     *
     * @param k the player number 0 is AI, 1 is player
     * @return the corresponding hand
     */
    public Hand getHand(int k) {
        // on error return automatic empty hand
        if (0 <= k && k < numPlayers) {
            return hand[k];
        }
        return new Hand();
    }

    /**
     * @return one card dealt from the deck
     */
    public Card getCardFromDeck() {
        return deck.dealCard();
    }

    /**
     * @return the count of cards in the deck
     */
    public int getNumCardsRemainingInDeck() {
        return deck.getCardCount();
    }

    /**
     * initializes a new game after its creation
     */
    public void newGame() {

        // clear the hands
        for (int i = 0; i < numPlayers; i++) {
            hand[i].resetHand();
        }

        // restock & shuffle the deck
        deck.init();
        deck.shuffle();
    }

    /**
     * deals cards from the deck until each hand is full
     * and the starting stacks are populated
     *
     * @return true if the operation was completed
     */
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
        enoughCards = deckToStacks();
        return enoughCards;
    }

    /**
     * deals a card onto each of the stacks from the deck
     *
     * @return true if successful
     */
    public boolean deckToStacks() {
        boolean enoughCards = deck.getCardCount() >= 2;
        if (enoughCards) {
            leftStack = deck.dealCard();
        }
        enoughCards = deck.getCardCount() >= 1;
        if (enoughCards) {
            rightStack = deck.dealCard();
        }
        return enoughCards;
    }

    /**
     * Sorts each player hand
     */
    void sortHands() {
        for (int i = 0; i < numPlayers; i++) {
            hand[i].sort();
        }
    }

    /**
     * checks whether a given card may be played onto a given stack
     *
     * @param toPlay      the card to play
     * @param isLeftStack the stack to play on
     * @return true if the card can legally be played on that stack
     */
    public boolean canPlay(Card toPlay, boolean isLeftStack) {
        if (isLeftStack) {
            return toPlay.getValue().previous() == leftStack.getValue() ||
                    toPlay.getValue().next() == leftStack.getValue();
        }
        return toPlay.getValue().previous() == rightStack.getValue() ||
                toPlay.getValue().next() == rightStack.getValue();
    }

}

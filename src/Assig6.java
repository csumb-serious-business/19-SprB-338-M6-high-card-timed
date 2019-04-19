import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringJoiner;

/**
 * Represents the source of playing cards in a game
 */
class Deck {
    public static final int DEFAULT_PACKS_COUNT = 1;
    public static final int MAX_PACKS = 6;
    public static final Deck DEFAULT_DECK = new Deck();

    private Card[] cards;
    private int packCount;
    private int topCard;
    private int cardCount;

    /**
     * Creates a new deck using a given number of packCount
     *
     * @param packCount the number of packCount within this deck
     */
    public Deck(int packCount) {
        this.init(packCount);
    }

    /**
     * Creates a new deck with a single pack
     */
    public Deck() {
        this.init();
    }

    /**
     * @return cardCount of the Deck
     */
    public int getCardCount() {
        return cardCount;
    }

    /**
     * @return packCount of the Deck
     */
    public int getPackCount() {
        return packCount;
    }

    /**
     * Refreshes this deck, discarding all current cards (if any)
     * and populating it with fresh packCount.
     *
     * @param packCount the number of packCount to refresh with
     */
    public void init(int packCount) {
        // enforce pack limit
        if (packCount > MAX_PACKS) {
            packCount = MAX_PACKS;
            System.out.printf("Maximum number of packCount exceeded, set to maximum: %d%n", packCount);
        }

        this.packCount = packCount;
        Pack pack = new Pack();

        int cardCount = packCount * pack.getCount();
        this.cards = new Card[cardCount];


        // for the desired number of packCount, copy the STANDARD pack into packCount
        Card[] master = Pack.getMaster();
        for (int i = 0; i < packCount; i++) {
            System.arraycopy(master, 0,
                    this.cards, i * pack.getCount(),
                    master.length);
        }

        // set the position of the top card
        this.topCard = cardCount - 1; // zero-indexed
        this.cardCount = cardCount;
    }

    /**
     * Refreshes this deck, discarding all current cards (if any)
     * and populating it with a fresh pack.
     */
    public void init() { // re-initializes an existing Deck object with one pack
        this.init(DEFAULT_PACKS_COUNT);
    }

    /**
     * Removes the top card of the deck and returns it
     *
     * @return the top card from the deck
     */
    public Card dealCard() {
        if (cardCount > 0) {
            Card dealtCard = cards[topCard];
            cards[topCard] = null;
            topCard--;
            cardCount--;
            return dealtCard;
        }
        cardCount--;
        return new Card();

    }

    /**
     * Fetches the top card from this deck without removing it
     *
     * @return the top card in this deck
     */
    public int getTopCard() { //returns the topCard integer
        return this.topCard;
    }

    /**
     * Fetches the card at a given position within the deck
     * -OR- an invalid card if that position is not populated
     * or the position is otherwise invalid
     * does not remove the card from the deck
     *
     * @param k the position of the card in the deck to inspect
     * @return the card at the given position, or and invalid card if not found
     */
    public Card inspectCard(int k) { //takes an integer and accesses the deck at that index and returns a card object
        if (k >= 0 && k <= topCard) {
            return cards[k];
        } else return new Card(null, Card.Suit.diamonds); //returns a card with errorFlag if index is out of range
    }

    /**
     * Exchanges the card in one position with the card in another position
     *
     * @param cardA the position of a card to swap
     * @param cardB the position of another card to swap
     */
    private void swap(int cardA, int cardB) { //helper function for shuffle, takes two ints and swaps the cards at those indexes
        if (cardA == cardB) {
            return;
        }

        Card tempCard = cards[cardA];
        cards[cardA] = cards[cardB];
        cards[cardB] = tempCard;
    }

    /**
     * Randomizes the order of the cards within this deck
     */
    public void shuffle() { //
        int numCards = this.topCard + 1;
        int shuffleSteps = numCards * 25;
        for (int i = 0; i < shuffleSteps; i++) {
            int cardA = (int) (Math.random() * numCards);
            int cardB = (int) (Math.random() * numCards);
            swap(cardA, cardB);
        }
    }

    /**
     * @return the number of cards in this deck
     */
    int getNumCards() {
        return cards.length;
    }

    /**
     * Adds a given card to this deck
     *
     * @param card the card to add
     * @return true if successful
     */
    boolean addCard(Card card) {
        cards[topCard + 1] = card;
        topCard++;
        return cards[topCard] == card;
    }

    /**
     * Removes a given card from this deck
     *
     * @param card the card to remove
     * @return true if successful
     */
    boolean removeCard(Card card) {
        boolean success = false;
        for (Card c : cards) {
            if (c == card) {
                cards[topCard] = null;
                topCard--;
                success = true;
            }
        }
        return success;
    }

    /**
     * Sorts the cards in this deck by face value and suit
     */
    void sort() {
        Card.arraySort(cards, topCard);
    }
}


/**
 * builds UI icons for individual cards
 */
class CardViewBuilder {
    private static final String imagesDir = "images/";
    private static final Icon[][] iconCards = new ImageIcon[14][4]; // 14 = A through K plus joker
    private static boolean iconsLoaded = false;
    private static Icon iconBack;

    /**
     * Populates the static iconCards array
     */
    private static void loadCardIcons() {
        if (iconsLoaded) {
            return;
        }
        for (Card.FaceValue value : Card.FaceValue.values()) {
            for (Card.Suit suit : Card.Suit.values()) {
                int vID = value.ordinal();
                int sID = suit.ordinal();

                iconCards[vID][sID] = new ImageIcon(
                        imagesDir + value.toString() + suit.toString() + ".gif");
            }
        }
        iconBack = new ImageIcon(imagesDir + "BK.gif");
        iconsLoaded = true;
    }

    /**
     * retrieves the icon for the backside of cards
     *
     * @return the backside UI icon
     */
    public static Icon getBackCardIcon() {
        loadCardIcons();
        return iconBack;
    }

    /**
     * retrieves a given card's icon
     *
     * @param card the card to retrieve
     * @return the corresponding UI icon for that card
     */
    public static Icon getIcon(Card card) {
        loadCardIcons();
        return iconCards[Card.valueAsInt(card)][Card.suitAsInt(card)];
    }
}

/**
 * represents a playing card
 */
class Card implements Comparable<Card> {
    private FaceValue value;
    private Suit suit;
    private boolean errorFlag;


    /**
     * Create a default card, an Ace of Spades
     */
    public Card() {
        this(FaceValue.A, Suit.spades);
    }

    /**
     * Create a card with a given value and suit
     *
     * @param value the card's value
     * @param suit  the card's suit
     */
    public Card(FaceValue value, Suit suit) {
        set(value, suit);

    }

    /**
     * Checks a given face value and suit for validity
     *
     * @param value the value to check
     * @param suit  the suit to check
     * @return true if valid
     */
    private static boolean isValid(FaceValue value, Suit suit) {
        return value != null && suit != null;
    }


    /**
     * Returns an int given a card value
     * turns "A", "2", "3", ... "Q", "K", "X" into 0 - 13
     *
     * @param card the card to check
     * @return cardValue
     */
    static int valueAsInt(Card card) {
        return card.value.ordinal();
    }

    /**
     * Sorts a given array of cards, first by values then by suit
     *
     * @param cards     the array to sort
     * @param arraySize the size of the array to sort
     */
    static void arraySort(Card[] cards, int arraySize) {
        for (int i = 0; i < arraySize - 1; i++) {
            for (int j = 0; j < arraySize - i - 1; j++) {
                Card card1 = cards[j];
                Card card2 = cards[j + 1];
                if (card1.getValue() == card2.getValue()) {
                    if (suitAsInt(card1) > suitAsInt(card2)) {
                        cards[j + 1] = card1;
                        cards[j] = card2;
                    }
                }

                for (FaceValue c : FaceValue.values()) {
                    if (c == card1.getValue()) {
                        break;
                    }
                    if (c == card2.getValue()) {
                        cards[j + 1] = card1;
                        cards[j] = card2;
                    }
                }
            }
        }
    }

    /**
     * Returns a int given a card suit
     *
     * @param card the card with a suit to check
     * @return cardSuit if j is valid or `false` in case of error message
     */
    static int suitAsInt(Card card) {
        return card.suit.ordinal();
    }

    /**
     * Assigns new values for this card's value and suit
     *
     * @param value the new value for this card
     * @param suit  the new suit for this card
     * @return true if the set operation was successful
     */
    public boolean set(FaceValue value, Suit suit) {
        if (isValid(value, suit)) {
            this.value = value;
            this.suit = suit;
            this.errorFlag = false;
        } else {
            this.errorFlag = true;

        }
        return this.errorFlag;
    }

    /**
     * @return the Card's suit
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * @return the Card's errorFlag
     */
    public boolean getErrorFlag() {
        return errorFlag;
    }

    /**
     * @return the Card's value
     */
    public FaceValue getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Card) {
            Card other = (Card) obj;
            return this.equals(other);
        }
        return false;
    }

    /**
     * Checks whether this card is equivalent to another card (has the same suit and value)
     *
     * @param card the other card to evaluate against
     * @return true if the two cards are equivalent
     */
    public boolean equals(Card card) {
        // errorFlag (invalid) cards can be checked also (not sure if that is OK)
        // they are technically evaluable, but not useful for the app
        return card.getSuit() == this.getSuit() &&
                card.getValue() == this.getValue() &&
                card.getErrorFlag() == this.getErrorFlag();
    }

    @Override
    public String toString() {
        if (this.getErrorFlag()) {
            return "\uFFFD\uFFFD"; // �� -- invalid card
        }

        return "" + this.getValue() + this.getSuit().toUnicode();
    }

    @Override
    public int compareTo(Card o) {
        int valueCompared = this.getValue().compareTo(o.getValue());

        // value tie-breaker
        if (valueCompared == 0) {
            return this.getSuit().compareTo(o.getSuit());
        }
        return valueCompared;
    }

    /**
     * Represents a Playing Card Card.Suit
     */
    enum Suit {
        clubs, diamonds, hearts, spades;

        @Override
        public String toString() {
            return this.name().substring(0, 1).toUpperCase();
        }

        /**
         * @return the corresponding Unicode character for a given suit
         */
        public char toUnicode() {
            switch (this) {
                case clubs:
                    return '\u2663'; // ♣
                case diamonds:
                    return '\u2666'; // ♦
                case hearts:
                    return '\u2665'; // ♥
                case spades:
                    return '\u2660'; // ♠
                default:
                    return '\uFFFD'; // � -- should never happen
            }
        }
    }

    /**
     * Represents a Playing Card face value
     */
    enum FaceValue {
        A,  // Ace
        _2, // Numeric
        _3,
        _4,
        _5,
        _6,
        _7,
        _8,
        _9,
        T, // 10
        J, // Jack
        Q, // Queen
        K, // King
        X; // Joker

        /**
         * Converts a character to its corresponding Card.FaceValue
         *
         * @param character the character to convert into a Card.FaceValue
         * @return the corresponding Card.FaceValue for this character
         * @throws IllegalArgumentException when character has no corresponding Card.FaceValue
         */
        public static FaceValue valueOf(char character) throws IllegalArgumentException {
            if (Character.isDigit(character)) {
                return FaceValue.valueOf("_" + character);
            }
            return FaceValue.valueOf("" + character);
        }

        @Override
        public String toString() {
            return this.name().substring(this.name().length() - 1);
        }

        public FaceValue next() {
            if (this.equals(X)) {
                return X;
            }

            if (this.equals(K)) {
                return A;
            }
            return FaceValue.values()[this.ordinal() + 1];
        }

        public FaceValue previous() {
            if (this.equals(X)) {
                return X;
            }

            if (this.equals(A)) {
                return K;
            }
            return FaceValue.values()[this.ordinal() - 1];
        }

    }
}

/**
 * A game where players take turns playing a card onto one of two stacks
 * the played card should have a face value one higher or lower than the
 * card on the stack. After each play the player takes a card from the deck
 * If a player has no plays they skip their turn.
 * If neither player has a play, a card is placed on each stack from the deck.
 * The game ends when the deck has no more cards in it. At that time the player
 * who has skipped the fewest number of turns wins.
 */
class Game {
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

/**
 * Controls interactions between a game and its UI
 */
class GameController {
    private final Game model;
    private final GameView view;
    private final Timer timer;

    /**
     * Create a controller for a given game/view pair
     *
     * @param game the game for this controller
     * @param view the view to control
     */
    public GameController(Game game, GameView view) {
        model = game;

        this.view = view;
        view.setController(this);

        this.timer = new Timer(this);
    }

    /**
     * @return the player's hand
     */
    public Hand getPlayerHand() {
        return model.getHand(1);

    }

    /**
     * @return the AI player's hand
     */
    public Hand getAiHand() {
        return model.getHand(0);
    }

    /**
     * @return the number of skips from the player
     */
    public int getPlayerSkips() {
        return model.getPlayerSkips();
    }


    /**
     * @return the number of skips for the AI player
     */
    public int getAiSkips() {
        return model.getAiSkips();
    }

    /**
     * updates the timer display
     *
     * @param time the time to use
     * @return true if the update was successful
     */
    public boolean changeTimerDisplay(int time) {
        return view.changeTimerDisplay(time);
    }


    /**
     * @return cannotPlayListener of the GameControl
     */
    public ActionListener getSkipTurnListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Skip Turn")) {
                    skipTurn();
                }
            }
        };
    }

    /**
     * deals a card onto each stack in the game
     */
    private void dealFromDeck() {
        model.deckToStacks();
    }

    /**
     * @return timerListener of the GameControl
     */
    public ActionListener getTimerListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("start")) {
                    timer.setRunning(true);
                    view.timerState(true);
                } else {
                    timer.setRunning(false);
                    view.timerState(false);
                }
            }
        };
    }

    /**
     * @return an action listener for played cards
     */
    public ActionListener playCardListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] actionCommand = e.getActionCommand().split("\\|");
                boolean onLeftStack = Boolean.valueOf(actionCommand[1]);

                // get player's selected card, if any
                JButton[] playerHand = view.getBtnsPlayerHand();

                int found = -1;
                for (int i = 0; i < playerHand.length; i++) {
                    JButton card = playerHand[i];
                    if (!card.isEnabled()) {
                        found = i;
                        break;
                    }
                }

                if (found >= 0) {
                    takeTurn(found, onLeftStack);
                }
            }
        };
    }

    /**
     * Checks whether the winning condition has been met
     * if it has, updates the UI to reflect that
     */
    private void evalWinCon() {
        if (model.getNumCardsRemainingInDeck() <= 1) {
            String winner;
            int skips;
            if (model.getPlayerSkips() == model.getAiSkips()) {
                view.updateScore("GAME OVER! %s  having only skipped %s times.");
                return;
            } else if (model.getPlayerSkips() < model.getAiSkips()) {
                winner = "You win,";
                skips = getPlayerSkips();

            } else {
                winner = "The Computer wins,";
                skips = getAiSkips();
            }
            String message = String.format("GAME OVER! %s having only skipped %s times.", winner, skips);
            view.updateScore(message);
        }
    }

    /**
     * plays a card onto one stack
     *
     * @param playerCardIndex the index of the card to play
     * @param onLeftStack     true if the card is played on the left stack
     */
    private void playerPlay(int playerCardIndex, boolean onLeftStack) {
        Hand playerHand = model.getHand(1);
        Card playerCard = playerHand.inspectCard(playerCardIndex);
        if (model.canPlay(playerCard, onLeftStack)) {
            Card played = playerHand.playCard(playerCardIndex);

            if (onLeftStack) {
                model.playOnLeftStack(1, played);
            } else {
                model.playOnRightStack(1, played);
            }

            view.updateViews(playerCard, onLeftStack);
        }
    }

    /**
     * The AI player plays a card onto one stack
     *
     * @return true if a card was played
     */
    private boolean aiPlay() {
        Hand aiHand = model.getHand(0);
        Card toPlay;
        Card played = null;
        for (int i = 0; i < model.getHand(0).getNumCards(); i++) {
            toPlay = aiHand.inspectCard(i);
            if (model.canPlay(toPlay, true)) {
                played = aiHand.playCard(i);
                model.playOnLeftStack(0, played);
                view.updateViews(played, true);
                break;
            } else if (model.canPlay(toPlay, false)) {
                played = aiHand.playCard(i);
                model.playOnRightStack(0, played);
                view.updateViews(played, false);
                break;
            }
        }
        if (played == null) {
            model.skipAi();
        }
        return played != null;
    }

    /**
     * @return an action listener for when a card
     * is selected by the player in the UI
     */
    public ActionListener selectCardListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int playerIndex = Integer.parseInt(e.getActionCommand());
                JButton[] playerHand = view.getBtnsPlayerHand();
                playerHand[playerIndex].setEnabled(false);
                for (int i = 0; i < playerHand.length; i++) {
                    if (i != playerIndex) {
                        playerHand[i].setEnabled(true);
                    }
                }
            }
        };
    }

    /**
     * @return the number of cards remaining in the deck
     */
    public int getCardsInDeck() {
        return model.getNumCardsRemainingInDeck();
    }

    private void takeTurn(int playerCardIndex, boolean onLeftStack) {
        playerPlay(playerCardIndex, onLeftStack);
        aiPlay();
        evalWinCon();
    }

    /**
     * skips a players turn, and lets the AI player take a turn
     */
    private void skipTurn() {
        model.skipPlayer();

        boolean played = aiPlay();
        if (!played) {
            model.skipAi();
            if (model.isPlayerSkipped()) {
                dealFromDeck();
                view.updateStacks(model.getLeftStack(), model.getRightStack());
            }
        }

        view.updateScore();
        evalWinCon();
    }

    /**
     * starts running the game
     */
    public void startGame() {

        // shuffle and deal into the hands.
        model.deal();

        //view.updateStack(null, null); // Start off with nothing selected
        view.build(model.getTitle(), model.getNumCardsPerHand(), model.getLeftStack(), model.getRightStack());
        timer.start();

    }


}

/**
 * This is the UI view for the game
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
     * After creating this game view, build assembles
     * all the parts an wires them up to the controller
     *
     * @param title           the tile for this view
     * @param numCardsPerHand the number cards in each hand
     * @param leftStackCard   the card on top of the left card stack
     * @param rightStackCard  the card on top of the right card stack
     */
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

        JButton btnSkipTurn = new JButton("Skip Turn");
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
        updateStack(leftStackCard, true);
        updateStack(rightStackCard, false);

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
    private void updateStack(Card card, boolean onLeftStack) {
        // update play area
        JButton btnChosen = new JButton(CardViewBuilder.getIcon(card));
        btnChosen.addActionListener(controller.playCardListener());

        // update the correct stack
        if (onLeftStack) {
            pnlLeftStack.removeAll();
            btnChosen.setActionCommand(card.getValue().name() + "|" + true);
            pnlLeftStack.add(btnChosen);
        } else {
            pnlRightStack.removeAll();
            btnChosen.setActionCommand(card.getValue().name() + "|" + false);
            pnlRightStack.add(btnChosen);
        }
    }

    /**
     * Updates both stacks of cards given a pair of cards
     *
     * @param leftCard  the card to place on the left stack
     * @param rightCard the card to place on the right stack
     */
    public void updateStacks(Card leftCard, Card rightCard) {
        updateStack(leftCard, true);
        updateStack(rightCard, false);
    }


    /**
     * updates the ui for player / ai hands
     */
    private void updateHands() {
        pnlHandPlayer.removeAll();
        pnlHandAi.removeAll();

        addPlayerHands();
    }

    /**
     * updates all the view panels after updating
     * a play stack with a given card
     *
     * @param card        the card to play on the stack
     * @param onLeftStack true if the card is intended to
     *                    be played on the left stack
     */
    public void updateViews(Card card, boolean onLeftStack) {
        updateStack(card, onLeftStack);
        updateHands();
        updateScore();
        validateAll();
    }


    /**
     * updates the score area of the UI
     */
    public void updateScore() {
        String message = String.format(
                "Player skips: %s, AI skips: %s, Cards in Deck: %s",
                controller.getPlayerSkips(),
                controller.getAiSkips(),
                controller.getCardsInDeck());

        updateScore(message);
    }

    /**
     * updates the score area of the UI with a given message
     *
     * @param message the message to use
     */
    public void updateScore(String message) {
        txtNotifications.setText(message);

    }


    /**
     * updates the timer value in the UI
     *
     * @param time the current timer value
     * @return true if successful
     */
    public boolean changeTimerDisplay(int time) {
        txtTimerTime.setText(Integer.toString(time));
        txtTimerTime.repaint();
        return true;
    }


    /**
     * toggles the timer state and button
     *
     * @param state the timer state, true if running
     */
    public void timerState(boolean state) {
        if (state) {
            // start the txtTimerTime and set button text to stop
            btnStopTimer.setText("stop");
        } else {
            btnStopTimer.setText("start");
        }
    }


}

/**
 * represents a player's hand
 */
class Hand {
    public static final int MAX_CARDS = 50; // no 'monster arrays'
    private Card[] cards;
    private int numCards;

    /**
     * Creates an empty hand
     */
    public Hand() {
        this.cards = new Card[MAX_CARDS];
        this.numCards = 0;
    }

    /**
     * Adds a card to the hand, usually from another play area, like a deck.
     *
     * @param card the card to add
     * @return true if card successfully taken
     */
    public boolean takeCard(Card card) {
        if (numCards < MAX_CARDS) {
            Card takenCard = new Card(card.getValue(), card.getSuit());
            cards[numCards++] = takenCard; //copies card to cards
            return true;//return true if success
        } else {
            return false;
        }
    }

    /**
     * Resets this hand to its initial (empty) state
     */
    public void resetHand() {
        numCards = 0;
        cards = new Card[MAX_CARDS];
    }

    /**
     * @return the number of cards currently in this hand
     */
    public int getNumCards() {
        return numCards;
    }

    /**
     * Fetches the card in a given position in the hand without removing it.
     *
     * @param k the position to fetch the card from
     * @return the card from the given position in the hand
     * -OR- an invalid card if that position is invalid or unpopulated
     */
    Card inspectCard(int k) {
        //if(index is less than the accessible and greater than the index)
        if (0 <= k && k <= numCards) {
            return new Card(cards[k].getValue(), cards[k].getSuit());
        }

        return new Card(null, Card.Suit.spades); //creates a card that will not work so error flag returns true
    }


    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "{ ", " }");
        if (numCards <= 0) {
            return joiner.add("empty").toString();
        }
        for (int i = 0; i < numCards; i++) {
            joiner.add(cards[i].toString());
        }
        return joiner.toString();

    }

    /**
     * Plays a given card from this Hand
     *
     * @param cardIndex the index of the card in this hand to play
     * @return the Card that was played or an invalid card upon failure
     */
    public Card playCard(int cardIndex) {
        if (numCards == 0) {
            //Creates a card that does not work
            return new Card(null, Card.Suit.spades);
        }
        //Decreases numCards.
        Card card = cards[cardIndex];

        numCards--;
        for (int i = cardIndex; i < numCards; i++) {
            cards[i] = cards[i + 1];
        }

        cards[numCards] = null;

        return card;
    }

    /**
     * Sorts this hands cards by value and suit
     */
    void sort() {
        Card.arraySort(cards, numCards);
    }
}


public class Assig6 {

    public static void main(String[] args) {
        Game game = new Game();
        GameView view = new GameView();

        GameController controller = new GameController(game, view);
        controller.startGame();
    }

}

/**
 * A pack of cards
 */
class Pack {
    private static final int DEFAULT_COUNT = 56;
    private static final Card[] STANDARD = Pack.generateStandard();
    private static final Card[] MASTER = Pack.generateMaster();
    // e.g. pinochle doesn't use 2-8 of any suit
    private static final Card.FaceValue[] DEFAULT_UNUSED_VALUES = new Card.FaceValue[0];
    public final int count = MASTER.length;

    /**
     * Populates the reusable STANDARD pack for decks
     * only if it is empty.
     */
    private static Card[] generateStandard() {

        Card[] pack = new Card[DEFAULT_COUNT];
        int c = 0;

        for (Card.FaceValue value : Card.FaceValue.values()) {
            for (Card.Suit suit : Card.Suit.values()) {
                pack[c] = new Card(value, suit);
                c++;
            }
        }
        return pack;
    }

    /**
     * Creates a master pack to clone into new packs
     *
     * @param unusedValues a sequence of unused values in this pack
     *                     for example in pinochle the values 2-8 are not used
     * @return the master pack
     */
    private static Card[] generateMaster(Card.FaceValue[] unusedValues) {

        // don't include unused cards in master pack
        Card[] master = new Card[STANDARD.length - (unusedValues.length * Card.Suit.values().length)];
        int masterSlot = 0;
        for (int i = 0; i < STANDARD.length; i++) {
            boolean isUsed = true;
            Card toCheck = STANDARD[i];
            for (Card.FaceValue v : unusedValues) {
                if (toCheck.getValue().equals(v)) {
                    isUsed = false;
                }
            }
            if (isUsed) {
                master[masterSlot] = toCheck;
                masterSlot += 1;
            }
        }

        return master;
    }


    /**
     * Creates a default master pack
     *
     * @return the master pack
     */
    private static Card[] generateMaster() {
        Card.FaceValue[] unused = {Card.FaceValue.X};
        return generateMaster(unused);
    }

    /**
     * @return a copy of the master pack
     */
    public static Card[] getMaster() {
        return MASTER.clone();
    }

    /**
     * @return a copy of the standard pack
     */
    public static Card[] getStandard() {
        return STANDARD.clone();
    }

    /**
     * @return count of the Pack
     */
    public int getCount() {
        return count;
    }
}


/**
 * A timer for the game
 */
class Timer extends Thread {
    private final GameController controller;
    private int timePlayed;
    private boolean isRunning;

    /**
     * Creates a new game timer
     *
     * @param controller the game controller for this timer
     */
    public Timer(GameController controller) {
        this.controller = controller;

    }

    /**
     * sets isRunning of the Timer
     *
     * @param running true if this timer should be running
     */
    public void setRunning(boolean running) {
        isRunning = running;
    }

    /**
     * Starts this timer
     */
    public void start() {
        isRunning = true;
        Thread timerThread = new Thread(this);
        timerThread.start();
    }

    /**
     * sets this timer to idle for a number of milliseconds
     *
     * @param milliseconds the time to idle in milliseconds
     * @throws InterruptedException when thread interrupted
     */
    public void doNothing(int milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (isRunning) {
                    controller.changeTimerDisplay(timePlayed);
                    doNothing(1000);
                    timePlayed++;
                } else {
                    doNothing(0);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

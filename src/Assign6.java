//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.StringJoiner;
//
//public class Assign6 {
//    static int NUM_CARDS_PER_HAND = 7;
//    static int NUM_PLAYERS = 2;
//    static JLabel[] botLabels = new JLabel[NUM_CARDS_PER_HAND];
//    static JButton[] userLabels = new JButton[NUM_CARDS_PER_HAND];
//    static JLabel[] playedCardLabels = new JLabel[NUM_PLAYERS];
//    static JLabel[] playLabelText = new JLabel[NUM_PLAYERS];
//    static Card[] unusedCardsPerPack;
//    static private CardGameFramework highCardGame;
//    static private CardTable myCardTable;
//    static private int playerWinCount = 0;
//    static private int computerWinCount = 0;
//    static private int tieCount = 0;
//
//    /**
//     * Clears the panels in the CardTable
//     *
//     * @return true if successful
//     */
//    private static boolean removeAll() {
//        myCardTable.pnlComputerHand.removeAll();
//        myCardTable.pnlHumanHand.removeAll();
//        myCardTable.pnlPlayArea.removeAll();
//        return true;
//    }
//
//    /**
//     * Validate the panels in the CardTable
//     *
//     * @return true if successful
//     */
//    private static boolean validateAll() {
//        myCardTable.pnlPlayArea.validate();
//        myCardTable.pnlComputerHand.validate();
//        myCardTable.pnlHumanHand.validate();
//        myCardTable.validate();
//        return true;
//    }
//
//    /**
//     * Adds labels for CardTable's players
//     *
//     * @return true if successful
//     */
//    private static boolean addLabelsForPlayers() {
//        userLabels = populateButtons(highCardGame.getHand(1));
//        botLabels = populateLabels(highCardGame.getHand(0), false);
//
//        for (JButton button : userLabels) {
//            myCardTable.pnlHumanHand.add(button);
//        }
//
//        for (JLabel label : botLabels) {
//            myCardTable.pnlComputerHand.add(label);
//        }
//
//        return true;
//
//    }
//
//    /**
//     * Compates the player's card against the computers card
//     *
//     * @param playerCard the player's card to evaluate
//     * @param botCard    the computer's card to evaluate
//     * @return the comparison result
//     */
//    public static int compare(Card playerCard, Card botCard) {
//        return playerCard.compareTo(botCard);
//    }
//
//    /**
//     * The main loop for this Card Game
//     *
//     * @param playerChoice   the player's chosen card
//     * @param computerChoice the computer's chosen card
//     */
//    private static void displayGame(Card playerChoice, Card computerChoice) {
//        String playerPrompt = "Click on a card below to choose";
//        String computerPrompt = "Computer says 'Please choose the highest value' to win";
//        String tieCountPrompt = "";
//
//        // clear everything
//        removeAll();
//
//        Hand playHand = new Hand();
//        if (playerChoice != null && computerChoice != null) {
//            playHand.takeCard(computerChoice);
//            playHand.takeCard(playerChoice);
//            playedCardLabels = populateLabels(playHand, true);
//
//
//            if (compare(playerChoice, computerChoice) >= 1) {
//                playerWinCount++;
//            } else if (compare(playerChoice, computerChoice) == 0) {
//                tieCount++;
//            } else if (compare(playerChoice, computerChoice) <= -1) {
//                computerWinCount++;
//            }
//
//
//            playerPrompt = "[Status]User Wins: " + playerWinCount;
//            computerPrompt = "[Status]Computer Wins: " + computerWinCount;
//            tieCountPrompt = " [Ties]: " + tieCount;
//        }
//
//
//        addLabelsForPlayers();
//
//        // At the start of this we don't need any
//        if (playedCardLabels[0] != null || playedCardLabels[1] != null) {
//            myCardTable.pnlPlayArea.add(playedCardLabels[0]);
//            myCardTable.pnlPlayArea.add(playedCardLabels[1]);
//        }
//
//        myCardTable.pnlPlayArea.add(new JLabel(computerPrompt, 0));
//        myCardTable.pnlPlayArea.add(new JLabel(playerPrompt + "\n" + tieCountPrompt, 0));
//
//
//        validateAll();
//        // If something goes wrong we know because this will return false
//
//    }
//
//    public static void main(String[] args) {
//        int numPacksPerDeck = 1;
//        int numJokersPerPack = 0;
//        int numUnusedCardsPerPack = 0;
//        unusedCardsPerPack = null;
//
//        highCardGame = new CardGameFramework(
//                numPacksPerDeck, numJokersPerPack, numUnusedCardsPerPack,
//                unusedCardsPerPack, NUM_PLAYERS, NUM_CARDS_PER_HAND);
//
//        // You deal() from it (one statement).
//        highCardGame.deal();
//        highCardGame.sortHands();
//
//        // establish main frame in which program will run
//        myCardTable = new CardTable("High-Card Table - Phase 3", NUM_CARDS_PER_HAND, NUM_PLAYERS);
//        myCardTable.setSize(800, 600);
//        myCardTable.setLocationRelativeTo(null);
//        myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        myCardTable.setVisible(true);
//        GUICard.loadCardIcons();
//
//        displayGame(null, null); // Start off with nothing selected
//        myCardTable.setVisible(true);
//
//    }
//
//    /**
//     * populates UI labels for a given hand
//     *
//     * @param hand     the hand to display
//     * @param isFaceUp whether the cards are face up or face down in the UI
//     * @return an array of labels to place into a UI container
//     */
//    static JLabel[] populateLabels(Hand hand, boolean isFaceUp) {
//        JLabel[] handLabels = new JLabel[hand.getNumCards()];
//
//
//        for (int i = 0; i < hand.getNumCards(); i++) {
//            JLabel label;
//            if (isFaceUp) {
//                label = new JLabel(GUICard.getIcon(hand.inspectCard(i)));
//            } else {
//                label = new JLabel(GUICard.getBackCardIcon());
//            }
//            handLabels[i] = label;
//        }
//        return handLabels;
//    }
//    /**
//     * populates UI buttons for a given hand
//     *
//     * @param hand the hand to display
//     * @return an array of buttons to place into a UI container.
//     */
//    static JButton[] populateButtons(Hand hand) {
//        JButton[] buttons = new JButton[hand.getNumCards()];
//
//        for (int i = 0; i < hand.getNumCards(); i++) {
//            Card card = hand.inspectCard(i);
//            JButton button = new JButton(GUICard.getIcon(card));
//            button.addActionListener(new HumanHandListener());
//            button.setActionCommand(String.valueOf(i));
//            buttons[i] = button;
//        }
//
//        return buttons;
//    }
//
//    /**
//     * An event listener for cards played from a player's hand
//     */
//    private static class HumanHandListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent cardClicked) {
//            int humanIndex = Integer.parseInt(cardClicked.getActionCommand());
//
//            // cards are ordered by value
//            Card humanCard = highCardGame.getHand(1).playCard(humanIndex);
//
//
//            Card botCard = highCardGame.getHand(0).playCard(humanIndex);
//
//            // If the bot has a card higher, should we do something?
//            displayGame(humanCard, botCard);
//            // Do a Refresh
//            myCardTable.repaint();
//        }
//    }
//}
//
///**
// * Provided, minor modification to support FaceValue enum instead of array
// */
//class CardGameFramework {
//    private static final int MAX_PLAYERS = 50;
//
//    private int numPlayers;
//    private int numPacks; // # standard 52-card packs per deck
//    // ignoring jokers or unused cards
//    private int numJokersPerPack; // if 2 per pack & 3 packs per deck, get 6
//    private int numUnusedCardsPerPack; // # cards removed from each pack
//    private int numCardsPerHand; // # cards to deal each player
//    private Deck deck; // holds the initial full deck and gets
//    // smaller (usually) during play
//    private Hand[] hand; // one Hand for each player
//    private Card[] unusedCardsPerPack; // an array holding the cards not used
//    // in the game. e.g. pinochle does not
//    // use cards 2-8 of any suit
//
//    public CardGameFramework(int numPacks, int numJokersPerPack, int numUnusedCardsPerPack, Card[] unusedCardsPerPack,
//                             int numPlayers, int numCardsPerHand) {
//        int k;
//
//        // filter bad values
//        if (numPacks < 1 || numPacks > 6)
//            numPacks = 1;
//        if (numJokersPerPack < 0 || numJokersPerPack > 4)
//            numJokersPerPack = 0;
//        if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) // > 1 card
//            numUnusedCardsPerPack = 0;
//        if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
//            numPlayers = 4;
//        // one of many ways to assure at least one full deal to all players
//        if (numCardsPerHand < 1 || numCardsPerHand > numPacks * (52 - numUnusedCardsPerPack) / numPlayers)
//            numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;
//
//        // allocate
//        this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
//        this.hand = new Hand[numPlayers];
//        for (k = 0; k < numPlayers; k++)
//            this.hand[k] = new Hand();
//        deck = new Deck(numPacks);
//
//        // assign to members
//        this.numPacks = numPacks;
//        this.numJokersPerPack = numJokersPerPack;
//        this.numUnusedCardsPerPack = numUnusedCardsPerPack;
//        this.numPlayers = numPlayers;
//        this.numCardsPerHand = numCardsPerHand;
//        for (k = 0; k < numUnusedCardsPerPack; k++)
//            this.unusedCardsPerPack[k] = unusedCardsPerPack[k];
//
//        // prepare deck and shuffle
//        newGame();
//    }
//
//    // constructor overload/default for game like bridge
//    public CardGameFramework() {
//        this(1, 0, 0, null, 4, 13);
//    }
//
//    public Hand getHand(int k) {
//        // hands start from 0 like arrays
//
//        // on error return automatic empty hand
//        if (k < 0 || k >= numPlayers)
//            return new Hand();
//
//        return hand[k];
//    }
//
//    public Card getCardFromDeck() {
//        return deck.dealCard();
//    }
//
//    public int getNumCardsRemainingInDeck() {
//        return deck.getNumCards();
//    }
//
//    public void newGame() {
//        int k, j;
//
//        // clear the hands
//        for (k = 0; k < numPlayers; k++)
//            hand[k].resetHand();
//
//        // restock the deck
//        deck.init(numPacks);
//
//        // remove unused cards
//        for (k = 0; k < numUnusedCardsPerPack; k++)
//            deck.removeCard(unusedCardsPerPack[k]);
//
//        // add jokers
//        for (k = 0; k < numPacks; k++)
//            for (j = 0; j < numJokersPerPack; j++)
//                deck.addCard(new Card(Card.FaceValue.X, Card.Suit.values()[j]));
//
//        // shuffle the cards
//        deck.shuffle();
//    }
//
//    public boolean deal() {
//        // returns false if not enough cards, but deals what it can
//        int k, j;
//        boolean enoughCards;
//
//        // clear all hands
//        for (j = 0; j < numPlayers; j++)
//            hand[j].resetHand();
//
//        enoughCards = true;
//        for (k = 0; k < numCardsPerHand && enoughCards; k++) {
//            for (j = 0; j < numPlayers; j++)
//                if (deck.getNumCards() > 0)
//                    hand[j].takeCard(deck.dealCard());
//                else {
//                    enoughCards = false;
//                    break;
//                }
//        }
//
//        return enoughCards;
//    }
//
//    void sortHands() {
//        int k;
//
//        for (k = 0; k < numPlayers; k++)
//            hand[k].sort();
//    }
//
//    Card playCard(int playerIndex, int cardIndex) {
//        // returns bad card if either argument is bad
//        if (playerIndex < 0 || playerIndex > numPlayers - 1 || cardIndex < 0 || cardIndex > numCardsPerHand - 1) {
//            // Creates a card that does not work
//            return new Card(null, Card.Suit.spades);
//        }
//
//        // return the card played
//        return hand[playerIndex].playCard(cardIndex);
//
//    }
//
//    boolean takeCard(int playerIndex) {
//        // returns false if either argument is bad
//        if (playerIndex < 0 || playerIndex > numPlayers - 1)
//            return false;
//
//        // Are there enough Cards?
//        if (deck.getNumCards() <= 0)
//            return false;
//
//        return hand[playerIndex].takeCard(deck.dealCard());
//    }
//
//}
//
///**
// * Models a card table
// */
//class CardTable extends JFrame {
//    static int MAX_CARDS_PER_HAND = 56;
//    static int MAX_PLAYERS = 2;
//    public JPanel pnlComputerHand;
//    public JPanel pnlHumanHand;
//    public JPanel pnlPlayArea;
//    private int numCardsPerHand;
//    private int numPlayers;
//
//    /**
//     * Arranges panels for the card table
//     *
//     * @param title           the name of the game played on this table
//     * @param numCardsPerHand the max number of per player hand
//     * @param numPlayers      the number of players for this game
//     */
//    public CardTable(String title, int numCardsPerHand, int numPlayers) {
//        super();
//
//        if (numPlayers > MAX_PLAYERS) {
//            numPlayers = MAX_PLAYERS;
//        }
//
//        if (numCardsPerHand > MAX_CARDS_PER_HAND) {
//            numCardsPerHand = MAX_CARDS_PER_HAND;
//        }
//
//        this.numPlayers = numPlayers;
//        this.numCardsPerHand = numCardsPerHand;
//
//        setSize(1150, 650);
//        setTitle(title);
//
//        setLayout(new BorderLayout());
//
//        pnlComputerHand = new JPanel();
//        pnlComputerHand.setLayout(new GridLayout(1, numCardsPerHand));
//        add(pnlComputerHand, BorderLayout.NORTH);
//
//        pnlPlayArea = new JPanel();
//        pnlPlayArea.setLayout(new GridLayout(2, numPlayers));
//        add(pnlPlayArea, BorderLayout.CENTER);
//
//        pnlHumanHand = new JPanel();
//        pnlHumanHand.setLayout(new GridLayout(1, numCardsPerHand));
//        add(pnlHumanHand, BorderLayout.SOUTH);
//    }
//
//    /**
//     * @return gets the number of players in this game
//     */
//    public int getNumPlayers() {
//        return numPlayers;
//    }
//
//    /**
//     * @return gets the maximum number of cards in each hand
//     */
//    public int getNumCardsPerHand() {
//        return numCardsPerHand;
//    }
//
//}
//
///**
// * Models an individual card within the UI
// */
//class GUICard {
//    static final String imagesDir = "images/";
//    static boolean iconsLoaded = false;
//    private static Icon[][] iconCards = new ImageIcon[14][4]; // 14 = A through K plus joker
//    private static Icon iconBack;
//
//    /**
//     * Populates the static iconCards array
//     */
//    static void loadCardIcons() {
//        if (iconsLoaded) {
//            return;
//        }
//        for (Card.FaceValue value : Card.FaceValue.values()) {
//            for (Card.Suit suit : Card.Suit.values()) {
//                int vID = value.ordinal();
//                int sID = suit.ordinal();
//
//                iconCards[vID][sID] = new ImageIcon(
//                        imagesDir + value.toString() + suit.toString() + ".gif");
//            }
//        }
//        iconBack = new ImageIcon(imagesDir + "BK.gif");//set the icon back
//        iconsLoaded = true;
//    }
//
//
//    /**
//     * retrieves a given card's icon
//     *
//     * @param card the card to retrieve
//     * @return the corresponding UI icon for that card
//     */
//    static public Icon getIcon(Card card) {
//        loadCardIcons();
//        return iconCards[Card.valueAsInt(card)][Card.suitAsInt(card)];
//    }
//
//    /**
//     * retrieves the icon for the backside of cards
//     *
//     * @return the backside UI icon
//     */
//    static public Icon getBackCardIcon() {
//        loadCardIcons();
//        return iconBack;
//    }
//
//
//}
//
//
///**
// * Represents a single playing card with a suit and value
// */
//class Card implements Comparable<Card> {
//    // superseded by FaceValue enum
//    public static char[] valuRanks = {
//            'A',
//            '2', '3', '4', '5', '6', '7', '8', '9',
//            'T',
//            'J', // Jack
//            'Q', // Queen
//            'K', // King
//            'X'  // Joker
//    };
//
//    private FaceValue value;
//    private Suit suit;
//    private boolean errorFlag;
//
//
//    /**
//     * Create a default card, an Ace of Spades
//     */
//    public Card() {
//        this(FaceValue.A, Suit.spades);
//    }
//
//    /**
//     * Create a card with a given value and suit
//     *
//     * @param value the card's value
//     * @param suit  the card's suit
//     */
//    public Card(FaceValue value, Suit suit) {
//        set(value, suit);
//
//    }
//
//    /**
//     * Evaluates whether a given value/suit combination results in a valid card
//     *
//     * @param value the value to check
//     * @param suit  the suit to check
//     * @return true if the combination of value/suit is valid for a card
//     */
//    private static boolean isValid(char value, Suit suit) {
//        try {
//            FaceValue.valueOf(value);
//        } catch (IllegalArgumentException ex) {
//            return false;
//        }
//        return suit != null;
//    }
//
//    /**
//     * Checks a given face value and suit for validity
//     *
//     * @param value the value to check
//     * @param suit  the suit to check
//     * @return true if valid
//     */
//    private static boolean isValid(FaceValue value, Suit suit) {
//        return value != null && suit != null;
//    }
//
//
//    /**
//     * Returns an int given a card value
//     * turns "A", "2", "3", ... "Q", "K", "X" into 0 - 13
//     *
//     * @param card the card to check
//     * @return cardValue
//     */
//    static int valueAsInt(Card card) {
//        return card.value.ordinal();
//    }
//
//    /**
//     * Sorts a given array of cards, first by values then by suit
//     *
//     * @param cards     the array to sort
//     * @param arraySize the size of the array to sort
//     */
//    static void arraySort(Card[] cards, int arraySize) {
//        for (int i = 0; i < arraySize - 1; i++) {
//            for (int j = 0; j < arraySize - i - 1; j++) {
//                Card card1 = cards[j];
//                Card card2 = cards[j + 1];
//                if (card1.getValue() == card2.getValue()) {
//                    if (suitAsInt(card1) > suitAsInt(card2)) {
//                        cards[j + 1] = card1;
//                        cards[j] = card2;
//                    }
//                }
//
//                for (FaceValue c : FaceValue.values()) {
//                    if (c == card1.getValue()) {
//                        break;
//                    }
//                    if (c == card2.getValue()) {
//                        cards[j + 1] = card1;
//                        cards[j] = card2;
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * Returns a int given a card suit
//     *
//     * @param card the card with a suit to check
//     * @return cardSuit if j is valid or `false` in case of error message
//     */
//    static int suitAsInt(Card card) {
//        return card.suit.ordinal();
//    }
//
//    /**
//     * Assigns new values for this card's value and suit
//     *
//     * @param value the new value for this card
//     * @param suit  the new suit for this card
//     * @return true if the set operation was successful
//     */
//    public boolean set(FaceValue value, Suit suit) {
//        if (isValid(value, suit)) {
//            this.value = value;
//            this.suit = suit;
//            this.errorFlag = false;
//        } else {
//            this.errorFlag = true;
//
//        }
//        return this.errorFlag;
//    }
//
//    /**
//     * @return the Card's suit
//     */
//    public Suit getSuit() {
//        return suit;
//    }
//
//    /**
//     * @return the Card's errorFlag
//     */
//    public boolean getErrorFlag() {
//        return errorFlag;
//    }
//
//    /**
//     * @return the Card's value
//     */
//    public FaceValue getValue() {
//        return value;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof Card) {
//            Card other = (Card) obj;
//            return this.equals(other);
//        }
//        return false;
//    }
//
//    /**
//     * Checks whether this card is equivalent to another card (has the same suit and value)
//     *
//     * @param card the other card to evaluate against
//     * @return true if the two cards are equivalent
//     */
//    public boolean equals(Card card) {
//        // errorFlag (invalid) cards can be checked also (not sure if that is OK)
//        // they are technically evaluable, but not useful for the app
//        return card.getSuit() == this.getSuit() &&
//                card.getValue() == this.getValue() &&
//                card.getErrorFlag() == this.getErrorFlag();
//    }
//
//    @Override
//    public String toString() {
//        if (this.getErrorFlag()) {
//            return "\uFFFD\uFFFD"; // �� -- invalid card
//        }
//
//        return "" + this.getValue() + this.getSuit().toUnicode();
//    }
//
//    @Override
//    public int compareTo(Card o) {
//        int valueCompared = this.getValue().compareTo(o.getValue());
//
//        // value tie-breaker
//        if (valueCompared == 0) {
//            return this.getSuit().compareTo(o.getSuit());
//        }
//        return valueCompared;
//    }
//
//    /**
//     * Represents a Playing Card Suit
//     */
//    enum Suit {
//        clubs, diamonds, hearts, spades;
//
//        @Override
//        public String toString() {
//            return this.name().substring(0, 1).toUpperCase();
//        }
//
//        /**
//         * @return the corresponding Unicode character for a given suit
//         */
//        public char toUnicode() {
//            switch (this) {
//                case clubs:
//                    return '\u2663'; // ♣
//                case diamonds:
//                    return '\u2666'; // ♦
//                case hearts:
//                    return '\u2665'; // ♥
//                case spades:
//                    return '\u2660'; // ♠
//                default:
//                    return '\uFFFD'; // � -- should never happen
//            }
//        }
//    }
//
//    /**
//     * Represents a Playing Card face value
//     */
//    enum FaceValue {
//        A,  // Ace
//        _2, // Numeric
//        _3,
//        _4,
//        _5,
//        _6,
//        _7,
//        _8,
//        _9,
//        T, // 10
//        J, // Jack
//        Q, // Queen
//        K, // King
//        X; // Joker
//
//        /**
//         * Converts a character to its corresponding FaceValue
//         *
//         * @param character the character to convert into a FaceValue
//         * @return the corresponding FaceValue for this character
//         * @throws IllegalArgumentException when character has no corresponding FaceValue
//         */
//        public static FaceValue valueOf(char character) throws IllegalArgumentException {
//            if (Character.isDigit(character)) {
//                return FaceValue.valueOf("_" + character);
//            }
//            return FaceValue.valueOf("" + character);
//        }
//
//        @Override
//        public String toString() {
//            return this.name().substring(this.name().length() - 1);
//        }
//
//    }
//
//
//}
//
//
///**
// * Represents a hand of playing cards held by a single player
// * It can hold several cards
// */
//class Hand {
//    public static int MAX_CARDS = 50; // no 'monster arrays'
//    private Card[] myCards; //---------/ also called myArray in assignment desc
//    private int numCards; //-----------/ count of cards
//
//    /**
//     * Creates an empty hand
//     */
//    public Hand() {
//        this.myCards = new Card[MAX_CARDS];
//        this.numCards = 0;
//    }
//
//    /**
//     * Adds a card to the hand, usually from another play area, like a deck.
//     *
//     * @param card the card to add
//     * @return true if card successfully taken
//     */
//    public boolean takeCard(Card card) {
//        if (numCards < MAX_CARDS) {
//            Card takenCard = new Card(card.getValue(), card.getSuit());
//            myCards[numCards++] = takenCard; //copies card to myCards
//            return true;//return true if success
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * Resets this hand to its initial (empty) state
//     */
//    public void resetHand() {
//        numCards = 0;
//        myCards = new Card[MAX_CARDS];
//    }
//
//    /**
//     * @return the number of cards currently in this hand
//     */
//    public int getNumCards() {
//        return numCards;
//    }
//
//    /**
//     * Fetches the card in a given position in the hand without removing it.
//     *
//     * @param k the position to fetch the card from
//     * @return the card from the given position in the hand
//     * -OR- an invalid card if that position is invalid or unpopulated
//     */
//    Card inspectCard(int k) {
//        //if(index is less than the accessible and greater than the index)
//        if (0 <= k && k <= numCards) {
//            return new Card(myCards[k].getValue(), myCards[k].getSuit());
//        }
//
//        return new Card(null, Card.Suit.spades); //creates a card that will not work so error flag returns true
//    }
//
//
//    @Override
//    public String toString() {
//        StringJoiner joiner = new StringJoiner(", ", "{ ", " }");
//        if (numCards <= 0) {
//            return joiner.add("empty").toString();
//        }
//        for (int i = 0; i < numCards; i++) {
//            joiner.add(myCards[i].toString());
//        }
//        return joiner.toString();
//
//    }
//
//    /**
//     * Plays a given card from this Hand
//     *
//     * @param cardIndex the index of the card in this hand to play
//     * @return the Card that was played or an invalid card upon failure
//     */
//    public Card playCard(int cardIndex) {
//        if (numCards == 0) //error
//        {
//            //Creates a card that does not work
//            return new Card(null, Card.Suit.spades);
//        }
//        //Decreases numCards.
//        Card card = myCards[cardIndex];
//
//        numCards--;
//        for (int i = cardIndex; i < numCards; i++) {
//            myCards[i] = myCards[i + 1];
//        }
//
//        myCards[numCards] = null;
//
//        return card;
//    }
//
//    /**
//     * Sorts this hands cards by value and suit
//     */
//    void sort() {
//        Card.arraySort(myCards, numCards);
//    }
//}
//
//
///**
// * Represents the source of playing cards in a game
// */
//class Deck 
//{
//    private static final int MAX_PACKS = 6;
//    private static final int CARDS_PER_PACK = 56;
//    public static final int MAX_CARDS = MAX_PACKS * CARDS_PER_PACK;
//    private static Card[] masterpack;
//
//    private Card[] cards;
//    private int numPacks;
//    private int topCard;
//
//    /**
//     * Creates a new deck using a given number of packs
//     *
//     * @param numPacks the number of packs within this deck
//     */
//    public Deck(int numPacks) {
//        this.init(numPacks);
//    }
//
//    /**
//     * Creates a new deck with a single pack
//     */
//    public Deck() {
//        this.init();
//    }
//
//    /**
//     * Populates the reusable master pack for decks
//     * only if it is empty.
//     */
//    private static void allocateMasterPack() {
//        if (Deck.masterpack != null) {
//            return;
//        }
//        Deck.masterpack = new Card[CARDS_PER_PACK];
//        int c = 0;
//
//        for (Card.FaceValue value : Card.FaceValue.values()) {
//            for (Card.Suit suit : Card.Suit.values()) {
//                masterpack[c] = new Card(value, suit);
//                c++;
//            }
//        }
//    }
//
//    /**
//     * Refreshes this deck, discarding all current cards (if any)
//     * and populating it with fresh packs.
//     *
//     * @param numPacks the number of packs to refresh with
//     */
//    public void init(int numPacks) {
//        // init master pack if not yet populated
//        allocateMasterPack();
//
//        // enforce pack limit
//        if (numPacks > MAX_PACKS) {
//            numPacks = MAX_PACKS;
//            System.out.printf("Maximum number of packs exceeded, set to maximum: %d%n", numPacks);
//        }
//
//        this.numPacks = numPacks;
//
//        int numCards = numPacks * CARDS_PER_PACK;
//        this.cards = new Card[numCards];
//
//
//        // for the desired number of packs, copy the master pack into packs
//        for (int i = 0; i < numPacks; i++) {
//            System.arraycopy(Deck.masterpack, 0,
//                    this.cards, i * CARDS_PER_PACK,
//                    Deck.masterpack.length);
//        }
//
//        // set the position of the top card
//        this.topCard = numCards - 1; // zero-indexed
//    }
//
//    /**
//     * Refreshes this deck, discarding all current cards (if any)
//     * and populating it with a fresh pack.
//     */
//    public void init() { //reinitializes an existing Deck object with one pack
//        this.init(1);
//    }
//
//    /**
//     * Removes the top card of the deck and returns it
//     *
//     * @return the top card from the deck
//     */
//    public Card dealCard() { //returns the top card of the deck and removes it
//        Card dealtCard = cards[topCard];
//        cards[topCard] = null;
//        topCard--;
//        return dealtCard;
//    }
//
//    /**
//     * Fetches the top card from this deck without removing it
//     *
//     * @return the top card in this deck
//     */
//    public int getTopCard() { //returns the topCard integer
//        return this.topCard;
//    }
//
//    /**
//     * Fetches the card at a given position within the deck
//     * -OR- an invalid card if that position is not populated
//     * or the position is otherwise invalid
//     * does not remove the card from the deck
//     *
//     * @param k the position of the card in the deck to inspect
//     * @return the card at the given position, or and invalid card if not found
//     */
//    public Card inspectCard(int k) { //takes an integer and accesses the deck at that index and returns a card object
//        if (k >= 0 && k <= topCard) {
//            return cards[k];
//        } else return new Card(null, Card.Suit.diamonds); //returns a card with errorFlag if index is out of range
//    }
//
//    /**
//     * Exchanges the card in one position with the card in another position
//     *
//     * @param cardA the position of a card to swap
//     * @param cardB the position of another card to swap
//     */
//    private void swap(int cardA, int cardB) { //helper function for shuffle, takes two ints and swaps the cards at those indexes
//        if (cardA == cardB) {
//            return;
//        }
//
//        Card tempCard = cards[cardA];
//        cards[cardA] = cards[cardB];
//        cards[cardB] = tempCard;
//    }
//
//    /**
//     * Randomizes the order of the cards within this deck
//     */
//    public void shuffle() { //
//        int numCards = this.topCard + 1;
//        int shuffleSteps = numCards * 25;
//        for (int i = 0; i < shuffleSteps; i++) {
//            int cardA = (int) (Math.random() * numCards);
//            int cardB = (int) (Math.random() * numCards);
//            swap(cardA, cardB);
//        }
//    }
//
//    /**
//     * @return the number of cards in this deck
//     */
//    int getNumCards() {
//        return cards.length;
//    }
//
//    /**
//     * Adds a given card to this deck
//     *
//     * @param card the card to add
//     * @return true if successful
//     */
//    boolean addCard(Card card) {
//        cards[topCard + 1] = card;
//        topCard++;
//        return cards[topCard] == card;
//    }
//
//    /**
//     * Removes a given card from this deck
//     *
//     * @param card the card to remove
//     * @return true if successful
//     */
//    boolean removeCard(Card card) {
//        boolean success = false;
//        for (Card c : cards) {
//            if (c == card) {
//                c = cards[topCard];
//                cards[topCard] = null;
//                topCard--;
//                success = true;
//            }
//        }
//        return success;
//    }
//
//    /**
//     * Sorts the cards in this deck by face value and suit
//     */
//    void sort() {
//        Card.arraySort(cards, topCard);
//    }
//}
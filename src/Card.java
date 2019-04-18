/**
 * represents a playing card
 */
public class Card implements Comparable<Card> {
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

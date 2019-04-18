/**
 * Represents the source of playing cards in a game
 */
public class Deck {
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

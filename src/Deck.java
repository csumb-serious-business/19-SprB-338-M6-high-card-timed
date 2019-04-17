/**
 * Represents the source of playing cards in a game
 */
class Deck {
    private static final int MAX_PACKS = 6;
    private static final int CARDS_PER_PACK = 56;
    private static final Card[] masterpack = generateMasterpack();

    private Card[] cards;
    private int numPacks;
    private int topCard;

    /**
     * Creates a new deck using a given number of packs
     *
     * @param numPacks the number of packs within this deck
     */
    public Deck(int numPacks) {
        this.init(numPacks);
    }

    /**
     * Creates a new deck with a single pack
     */
    public Deck() {
        this.init();
    }

    /**
     * Populates the reusable master pack for decks
     * only if it is empty.
     */
    private static Card[] generateMasterpack() {

        Card[] pack = new Card[CARDS_PER_PACK];
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
     * @return numPacks of the Deck
     */
    public int getNumPacks() {
        return numPacks;
    }

    /**
     * Refreshes this deck, discarding all current cards (if any)
     * and populating it with fresh packs.
     *
     * @param numPacks the number of packs to refresh with
     */
    public void init(int numPacks) {
        // enforce pack limit
        if (numPacks > MAX_PACKS) {
            numPacks = MAX_PACKS;
            System.out.printf("Maximum number of packs exceeded, set to maximum: %d%n", numPacks);
        }

        this.numPacks = numPacks;

        int numCards = numPacks * CARDS_PER_PACK;
        this.cards = new Card[numCards];


        // for the desired number of packs, copy the master pack into packs
        for (int i = 0; i < numPacks; i++) {
            System.arraycopy(Deck.masterpack, 0,
                    this.cards, i * CARDS_PER_PACK,
                    Deck.masterpack.length);
        }

        // set the position of the top card
        this.topCard = numCards - 1; // zero-indexed
    }

    /**
     * Refreshes this deck, discarding all current cards (if any)
     * and populating it with a fresh pack.
     */
    public void init() { //reinitializes an existing Deck object with one pack
        this.init(1);
    }

    /**
     * Removes the top card of the deck and returns it
     *
     * @return the top card from the deck
     */
    public Card dealCard() { //returns the top card of the deck and removes it
        Card dealtCard = cards[topCard];
        cards[topCard] = null;
        topCard--;
        return dealtCard;
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
                c = cards[topCard];
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

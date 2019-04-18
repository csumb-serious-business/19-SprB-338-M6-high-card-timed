import java.util.StringJoiner;

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

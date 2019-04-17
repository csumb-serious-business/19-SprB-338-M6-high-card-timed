/**
 * todo: add desc
 *
 * @author todo
 */
public class Pack {
    private static final int DEFAULT_COUNT = 56;
    public static final Card[] STANDARD = Pack.generateStandard();
    private static final int MIN_JOKERS = 0;
    private static final int MAX_JOKERS = 4;
    private static final int DEFAULT_JOKERS = MIN_JOKERS;
    // e.g. pinochle doesn't use 2-8 of any suit
    private static final Card[] DEFAULT_UNUSED_CARDS = new Card[0];
    public static final Card[] MASTER = Pack.generateMaster();
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

    private static Card[] generateMaster(int jokerCount, Card[] unusedCards) {

        // don't include unused cards in master pack
        Card[] master = new Card[STANDARD.length - unusedCards.length];
        int masterSlot = 0;
        for (int i = 0; i < STANDARD.length; i++) {
            boolean isUsed = true;
            Card toCheck = STANDARD[i];
            for (Card u : unusedCards) {
                if (toCheck.equals(u)) {
                    isUsed = false;
                    break;
                }
            }
            if (isUsed) {
                master[masterSlot] = toCheck;
                masterSlot += 1;
            }
        }

        // add the required number of jokers
        if (MIN_JOKERS <= jokerCount && jokerCount <= MAX_JOKERS) {
            jokerCount = MIN_JOKERS;
        }

        for (int i = 0; i < jokerCount; i++) {
            master[masterSlot] = new Card(Card.FaceValue.X, Card.Suit.values()[i]);
            masterSlot++;
        }

        return master;
    }

    private static Card[] generateMaster() {
        return generateMaster(DEFAULT_JOKERS, DEFAULT_UNUSED_CARDS);
    }

    /**
     * @return count of the Pack
     */
    public int getCount() {
        return count;
    }
}

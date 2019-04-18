/**
 * todo: add desc
 *
 * @author todo
 */
public class Pack {
    private static final int DEFAULT_COUNT = 56;
    public static final Card[] STANDARD = Pack.generateStandard();
    public static final Card[] MASTER = Pack.generateMaster();
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


    private static Card[] generateMaster() {
        Card.FaceValue[] unused = {Card.FaceValue.X};
        return generateMaster(unused);
    }

    /**
     * @return count of the Pack
     */
    public int getCount() {
        return count;
    }
}

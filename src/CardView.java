import javax.swing.*;

/**
 * todo: add desc
 *
 * @author todo
 */
class CardView {
    private static final String imagesDir = "images/";
    private static boolean iconsLoaded = false;
    private static Icon[][] iconCards = new ImageIcon[14][4]; // 14 = A through K plus joker
    private static Icon iconBack;

    public CardView() {
        loadCardIcons();
    }

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
    public Icon getBackCardIcon() {
        return iconBack;
    }

    /**
     * retrieves a given card's icon
     *
     * @param card the card to retrieve
     * @return the corresponding UI icon for that card
     */
    public Icon getIcon(Card card) {
        return iconCards[Card.valueAsInt(card)][Card.suitAsInt(card)];
    }
}

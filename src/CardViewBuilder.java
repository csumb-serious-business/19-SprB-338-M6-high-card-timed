import javax.swing.*;

/**
 * builds UI icons for individual cards
 */
public class CardViewBuilder {
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

import java.util.StringJoiner;

public class TimedController {

    TimedModel cardModel;
    TimedViewer cardView;

    static int NUM_CARDS_PER_HAND = 7;
    static int NUM_PLAYERS = 2;

    public TimedController(TimedModel cModel, TimedViewer cView) {
        cardModel = cModel;
        cardView = cView;
    }

    public void doMainGame() {
        int numPacksPerDeck = 1;
        int numJokersPerPack = 0;
        int numUnusedCardsPerPack = 0;
        Card[] unusedCardsPerPack = null;

        CardGameFramework timedCardGame = this.cardModel.highCardGame;
        // shuffle and deal into the hands.
        timedCardGame.deal();

        cardView.setModel(timedCardGame); //Pass the model data to the view
        //TODO add timer here
        cardView.displayGame(null, null); // Start off with nothing selected
        cardView.addLabelsForPlayers(); //Do this for the first GO
        cardView.myCardTable.setVisible(true);
    }
}




public class TimedModel {
	
	   static int NUM_CARDS_PER_HAND = TimedController.NUM_CARDS_PER_HAND;
	   static int NUM_PLAYERS = TimedController.NUM_PLAYERS;
	    int numPacksPerDeck = 1;
	    int numJokersPerPack = 0;
	    int numUnusedCardsPerPack = 0;
	    static Card[] unusedCardsPerPack = null;
	  
	   
	   public static CardGameFramework  highCardGame; //Our Model, Maybe have more accessors.
	   
	 public TimedModel()
	   {
       highCardGame = new CardGameFramework(
			       numPacksPerDeck, numJokersPerPack, numUnusedCardsPerPack,
			       unusedCardsPerPack, NUM_PLAYERS, NUM_CARDS_PER_HAND);
	   }
}

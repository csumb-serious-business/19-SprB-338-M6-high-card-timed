import com.sun.jdi.event.ExceptionEvent;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;
import javax.swing.border.Border;

public class TimedViewer {
	static int NUM_CARDS_PER_HAND = 7;
	static int NUM_PLAYERS = 2;
	static JLabel[] botLabels = new JLabel[NUM_CARDS_PER_HAND];
	static JButton[] userLabels = new JButton[NUM_CARDS_PER_HAND];
	static JLabel[] playedCardLabels = new JLabel[NUM_PLAYERS];
	static JLabel[] playLabelText = new JLabel[NUM_PLAYERS];
	static Card[] unusedCardsPerPack;
	static JButton timerStop;
	static JTextField timer;

	static JButton[] leftPanel;//includes cannot play and start game
	static JButton cannotPlayButton;
	static JButton startGameButton;

	static public CardGameFramework highCardGame;
	static public CardTable myCardTable;

	private Card leftCard;
	private Card rightCard;

	//	static private int playerWinCount = 0;
	//	static private int computerWinCount = 0;
	//	static private int tieCount = 0;

	static private int playerCannotPlayCount=0;
	static private int computerCannotPlayCount=0;

	private static boolean playerTurn;
	private static boolean computerTurn;


	public TimedViewer(){

		myCardTable = new CardTable("Timed Card Game", NUM_CARDS_PER_HAND, NUM_PLAYERS);
		myCardTable.setSize(800, 600);
		myCardTable.setLocationRelativeTo(null);
		myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		myCardTable.setVisible(true);
		GUICard.loadCardIcons();


	}

	/**
	 * Clears the panels in the CardTable
	 *
	 * @return true if successful
	 */
	private static boolean removeAll() {
		myCardTable.pnlComputerHand.removeAll();
		myCardTable.pnlHumanHand.removeAll();
		myCardTable.pnlPlayArea.removeAll();

		//myCardTable.pnlCannotPlay.removeAll();
		return true;
	}

	/**
	 * Validate the panels in the CardTable
	 *
	 * @return true if successful
	 */
	private static boolean validateAll() {
		myCardTable.pnlPlayArea.validate();
		myCardTable.pnlComputerHand.validate();
		myCardTable.pnlHumanHand.validate();
		myCardTable.pnlCannotPlay.validate();
		myCardTable.validate();
		return true;
	}

	/**
	 * Adds labels for CardTable's players
	 *
	 * @return true if successful
	 */
	private static boolean addLabelsForPlayers(CardGameFramework highCardGame) {
		userLabels = populateButtons(highCardGame.getHand(1));
		botLabels = populateLabels(highCardGame.getHand(0), false);

		for (JButton button : userLabels) {
			myCardTable.pnlHumanHand.add(button);
		}

		for (JLabel label : botLabels) {
			myCardTable.pnlComputerHand.add(label);
		}
		return true;

	}

	/**
	 * Compates the player's card against the computers card
	 *
	 * @param playerCard the player's card to evaluate
	 * @param botCard    the computer's card to evaluate
	 * @return the comparison result
	 */
	public static int compare(Card playerCard, Card botCard) {
		return playerCard.compareTo(botCard);
	}

	/**
	 * populates UI labels for a given hand
	 *
	 * @param hand     the hand to display
	 * @param isFaceUp whether the cards are face up or face down in the UI
	 * @return an array of labels to place into a UI container
	 */
	static JLabel[] populateLabels(Hand hand, boolean isFaceUp) {
		JLabel[] handLabels = new JLabel[hand.getNumCards()];


		for (int i = 0; i < hand.getNumCards(); i++) {
			JLabel label;
			if (isFaceUp) {
				label = new JLabel(GUICard.getIcon(hand.inspectCard(i)));
			} else {
				label = new JLabel(GUICard.getBackCardIcon());
			}
			handLabels[i] = label;
		}
		return handLabels;
	}

	/**
	 * populates UI buttons for a given hand
	 *
	 * @param hand the hand to display
	 * @return an array of buttons to place into a UI container.
	 */
	static JButton[] populateButtons(Hand hand) {
		JButton[] buttons = new JButton[hand.getNumCards()];

		for (int i = 0; i < hand.getNumCards(); i++) {
			Card card = hand.inspectCard(i);
			JButton button = new JButton(GUICard.getIcon(card));
			button.addActionListener(new HumanHandListener());
			button.setActionCommand(String.valueOf(i));
			buttons[i] = button;
		}

		return buttons;
	}

	private static class TimerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent stopClicked) {
			if (stopClicked.getActionCommand() == "stop") {
				timerStop.setText("start");
			}
			else {
				timerStop.setText("stop");
			}
		}

	}
	private static class CannotPlayListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent cannotPlayClicked) {
			if(cannotPlayClicked.getActionCommand()=="Cannot Play") {
				playerCannotPlay();
			}
		}
	}
	//TODO Connect this to startGame function
	private static class StartGameListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent startClicked) {
			if (startClicked.getActionCommand() == "Start Game") {
				Card card0=playedCardLabels[0];
				Card card1=playedCardLabels[1];
				startGame(Card card1,Card card2);
			}
		}
	}
	private static class HumanHandListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent cardClicked) {
			int humanIndex = Integer.parseInt(cardClicked.getActionCommand());

			// cards are ordered by value
			Card humanCard = highCardGame.getHand(1).playCard(humanIndex);


			Card botCard = highCardGame.getHand(0).playCard(humanIndex);

			// If the bot has a card higher, should we do something?
			displayGame(humanCard, botCard);
			// Do a Refresh
			myCardTable.repaint();
		}
	}
	//TODO This needs to put cards out first
	//But also needs to make sure they stay null for displayGame
	//or something
	public static void startGame(JLabel card0,JLabel card1) {
		card0=playedCardLabels[0];
		card1=playedCardLabels[1];
		myCardTable.pnlPlayArea.add(card0);//start throwing cards out
		myCardTable.pnlPlayArea.add(card1);	
		playedCardLabels[0]=null;
		playedCardLabels[1]=null;
		displayGame(null,null);
		
	}
	/**
	 * The main loop for this Card Game
	 *
	 * @param playerChoice   the player's chosen card
	 * @param computerChoice the computer's chosen card
	 */
	public static void displayGame(Card playerChoice, Card computerChoice) {
		String playerPrompt = "Test";
		String computerPrompt = "Test1";
		String tieCountPrompt = "";

		// clear everything
		removeAll();

		Hand playHand = new Hand();
		if (playerChoice != null && computerChoice != null) {
			playHand.takeCard(computerChoice);
			playHand.takeCard(playerChoice);
			playedCardLabels = populateLabels(playHand, true);

			//TODO Rules:
			//			if (compare(playerChoice, computerChoice) >= 1) {
			//				playerWinCount++;
			//			} else if (compare(playerChoice, computerChoice) == 0) {
			//				tieCount++;
			//			} else if (compare(playerChoice, computerChoice) <= -1) {
			//				computerWinCount++;
			//			}

			playerPrompt = "[Status]User Cannot Plays: " + playerCannotPlayCount;
			computerPrompt = "[Status]Computer Cannot Plays: " + computerCannotPlayCount;
		}


		// At the start of this we don't need any
		//		if (playedCardLabels[0] != null || playedCardLabels[1] != null) {
		//			myCardTable.pnlPlayArea.add(playedCardLabels[0]);
		//			myCardTable.pnlPlayArea.add(playedCardLabels[1]);
		//			addLabelsForPlayers();
		//		}
		//TODO: Add cards to table first but cant because of null check
		if (playedCardLabels[0] != null || playedCardLabels[1] != null) {
			myCardTable.pnlPlayArea.add(playedCardLabels[0]);//start throwing cards out
			myCardTable.pnlPlayArea.add(playedCardLabels[1]);
			addLabelsForPlayers();
		}

		myCardTable.pnlPlayArea.add(new JLabel(computerPrompt, 0));
		myCardTable.pnlPlayArea.add(new JLabel(playerPrompt + "\n" + tieCountPrompt, 0));

		validateAll();
		// If something goes wrong we know because this will return false

	}
	public void setModel(CardGameFramework timedCardGame) {
		this.highCardGame = timedCardGame;	
	}

	/**
//   * Adds labels for CardTable's players
//   *
//   * @return true if successful
//   */
	public static boolean addLabelsForPlayers() {
		userLabels = populateButtons(highCardGame.getHand(1));
		botLabels = populateLabels(highCardGame.getHand(0), false);

		for (JButton button : userLabels) {
			myCardTable.pnlHumanHand.add(button);
		}

		for (JLabel label : botLabels) {
			myCardTable.pnlComputerHand.add(label);
		}

		return true;

	}

	public static boolean addLabelsForTimer() {
		timerStop = new JButton("stop");
		timerStop.addActionListener(new TimerListener());
		timer = new JTextField("0",6);
		timer.setEditable(false);
		myCardTable.pnlTimer.add(timer);
		myCardTable.pnlTimer.add(timerStop);
		return true;
	}

	public static boolean changeTimerDisplay(int time) {
		timer.setText(Integer.toString(time));
		timer.repaint();
		return true;
	}

	public static int playerCannotPlay() {
		return playerCannotPlayCount++;

	}
	public static int computerCannotPlay() {
		return computerCannotPlayCount++;
	}
	public static boolean addLabelsForCannotPlay() {
		cannotPlayButton=new JButton("Cannot Play");
		myCardTable.pnlCannotPlay.add(cannotPlayButton);
		cannotPlayButton.addActionListener(new CannotPlayListener());
		//leftPanel[0]=cannotPlayButton;
		return true;
	}
	public static boolean addLabelsForStartGame() {
		startGameButton=new JButton("Start Game");
		myCardTable.pnlPlayArea.add(startGameButton);
		startGameButton.addActionListener(new StartGameListener());
		leftPanel[1]=cannotPlayButton;
		return true;
	}
}

class GUICard 
{
	static final String imagesDir = "images/";
	static boolean iconsLoaded = false;
	private static Icon[][] iconCards = new ImageIcon[14][4]; // 14 = A through K plus joker
	private static Icon iconBack;

	/**
	 * Populates the static iconCards array
	 */
	static void loadCardIcons() {
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
		iconBack = new ImageIcon(imagesDir + "BK.gif");//set the icon back
		iconsLoaded = true;
	}


	/**
	 * retrieves a given card's icon
	 *
	 * @param card the card to retrieve
	 * @return the corresponding UI icon for that card
	 */
	static public Icon getIcon(Card card) {
		loadCardIcons();
		return iconCards[Card.valueAsInt(card)][Card.suitAsInt(card)];
	}

	/**
	 * retrieves the icon for the backside of cards
	 *
	 * @return the backside UI icon
	 */
	static public Icon getBackCardIcon() {
		loadCardIcons();
		return iconBack;
	}
}

class CardTable extends JFrame 
{
	static int MAX_CARDS_PER_HAND = 56;
	static int MAX_PLAYERS = 2;
	public JPanel pnlComputerHand;
	public JPanel pnlHumanHand;
	public JPanel pnlPlayArea;
	public JPanel pnlTimer;

	public JPanel pnlCannotPlay;

	private int numCardsPerHand;
	private int numPlayers;

	/**
	 * Arranges panels for the card table
	 *
	 * @param title           the name of the game played on this table
	 * @param numCardsPerHand the max number of per player hand
	 * @param numPlayers      the number of players for this game
	 */
	public CardTable(String title, int numCardsPerHand, int numPlayers) {
		super();

		if (numPlayers > MAX_PLAYERS) {
			numPlayers = MAX_PLAYERS;
		}

		if (numCardsPerHand > MAX_CARDS_PER_HAND) {
			numCardsPerHand = MAX_CARDS_PER_HAND;
		}

		this.numPlayers = numPlayers;
		this.numCardsPerHand = numCardsPerHand;

		setSize(1150, 650);
		setTitle(title);

		setLayout(new BorderLayout());

		pnlComputerHand = new JPanel();
		pnlComputerHand.setLayout(new GridLayout(1, numCardsPerHand));
		add(pnlComputerHand, BorderLayout.NORTH);

		pnlPlayArea = new JPanel();
		pnlPlayArea.setLayout(new GridLayout(2, numPlayers));
		add(pnlPlayArea, BorderLayout.CENTER);

		pnlHumanHand = new JPanel();
		pnlHumanHand.setLayout(new GridLayout(1, numCardsPerHand));
		add(pnlHumanHand, BorderLayout.SOUTH);

		pnlTimer = new JPanel();
		pnlTimer.setLayout(new FlowLayout());
		add(pnlTimer, BorderLayout.EAST);

		pnlCannotPlay=new JPanel();
		pnlCannotPlay.setLayout(new FlowLayout());
		add(pnlCannotPlay, BorderLayout.WEST);
	}

	/**
	 * @return gets the number of players in this game
	 */
	public int getNumPlayers() {
		return numPlayers;
	}

	/**
	 * @return gets the maximum number of cards in each hand
	 */
	public int getNumCardsPerHand() {
		return numCardsPerHand;
	}

}


/**
 * Represents the source of playing cards in a game
 */
class Deck 
{
	private static final int MAX_PACKS = 6;
	private static final int CARDS_PER_PACK = 56;
	public static final int MAX_CARDS = MAX_PACKS * CARDS_PER_PACK;
	private static Card[] masterpack;

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
	private static void allocateMasterPack() {
		if (Deck.masterpack != null) {
			return;
		}
		Deck.masterpack = new Card[CARDS_PER_PACK];
		int c = 0;

		for (Card.FaceValue value : Card.FaceValue.values()) {
			for (Card.Suit suit : Card.Suit.values()) {
				masterpack[c] = new Card(value, suit);
				c++;
			}
		}
	}

	/**
	 * Refreshes this deck, discarding all current cards (if any)
	 * and populating it with fresh packs.
	 *
	 * @param numPacks the number of packs to refresh with
	 */
	public void init(int numPacks) {
		// init master pack if not yet populated
		allocateMasterPack();

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


class Card implements Comparable<Card> {
	// superseded by FaceValue enum
	public static char[] valuRanks = {
			'A',
			'2', '3', '4', '5', '6', '7', '8', '9',
			'T',
			'J', // Jack
			'Q', // Queen
			'K', // King
			'X'  // Joker
	};

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
	 * Evaluates whether a given value/suit combination results in a valid card
	 *
	 * @param value the value to check
	 * @param suit  the suit to check
	 * @return true if the combination of value/suit is valid for a card
	 */
	private static boolean isValid(char value, Suit suit) {
		try {
			FaceValue.valueOf(value);
		} catch (IllegalArgumentException ex) {
			return false;
		}
		return suit != null;
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
	 * Represents a Playing Card Suit
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
		 * Converts a character to its corresponding FaceValue
		 *
		 * @param character the character to convert into a FaceValue
		 * @return the corresponding FaceValue for this character
		 * @throws IllegalArgumentException when character has no corresponding FaceValue
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

	}

	class CardTable extends JFrame {
		static final int MAX_CARDS_PER_HAND = 56;
		static final  int MAX_PLAYERS = 2;
		public JPanel pnlComputerHand;
		public JPanel pnlHumanHand;
		public JPanel pnlPlayArea;

		public JPanel pnlCannotPlayArea;

		private int numCardsPerHand;
		private int numPlayers;

		private int numPlayerCannotPlays;


		/**
		 * Arranges panels for the card table
		 *
		 * @param title           the name of the game played on this table
		 * @param numCardsPerHand the max number of per player hand
		 * @param numPlayers      the number of players for this game
		 */
		public CardTable(String title, int numCardsPerHand, int numPlayers) {
			super();

			if (numPlayers > MAX_PLAYERS) {
				numPlayers = MAX_PLAYERS;
			}

			if (numCardsPerHand > MAX_CARDS_PER_HAND) {
				numCardsPerHand = MAX_CARDS_PER_HAND;
			}

			this.numPlayers = numPlayers;
			this.numCardsPerHand = numCardsPerHand;

			setSize(1150, 650);
			setTitle(title);

			setLayout(new BorderLayout());

			pnlComputerHand = new JPanel();
			pnlComputerHand.setLayout(new GridLayout(1, numCardsPerHand));
			add(pnlComputerHand, BorderLayout.NORTH);

			pnlPlayArea = new JPanel();
			pnlPlayArea.setLayout(new GridLayout(2, numPlayers));
			add(pnlPlayArea, BorderLayout.CENTER);

			pnlHumanHand = new JPanel();
			pnlHumanHand.setLayout(new GridLayout(1, numCardsPerHand));
			add(pnlHumanHand, BorderLayout.SOUTH);

			//TODO This needs to include startGame
			pnlCannotPlayArea=new JPanel();
			pnlCannotPlayArea.setLayout(new FlowLayout());
			add(pnlCannotPlayArea,BorderLayout.WEST);
		}

		/**
		 * @return gets the number of players in this game
		 */
		public int getNumPlayers() {
			return numPlayers;
		}

		/**
		 * @return gets the maximum number of cards in each hand
		 */
		public int getNumCardsPerHand() {
			return numCardsPerHand;
		}
	}
}



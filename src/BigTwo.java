import java.util.ArrayList;

/**
 * The BigTwo class implements the CardGame interface. It is used to model a Big Two card game.
 * @author wali
 */
public class BigTwo implements CardGame {
	private final static int NUM_OF_PLAYERS = 4;
	
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int currentIdx;
	private int lastHandIdx;
	boolean firstturn;
	private BigTwoTable table;
	
	/**
	 * a constructor for creating a Big Two card game. Creates 4 players and adds them to the player list. Also creates a table (i.e., a BigTwoTable object) which builds the GUI for the game and handles users actions.
	 */
	BigTwo(){
		this.playerList = new ArrayList<CardGamePlayer>();
		for (int i = 0; i < NUM_OF_PLAYERS; i++) {
			this.playerList.add(new CardGamePlayer());
		}
		handsOnTable = new ArrayList<Hand>();
		this.table = new BigTwoTable(this);
	}
	
	/**
	 * Returns the number of players in this card game.
	 * 
	 * @return the number of players in this card game
	 */
	public int getNumOfPlayers() {
		return playerList.size();
	}
	
	/**
	 * a method for retrieving the deck of cards being used.
	 * @return a Deck object with the cards being used
	 */
	public Deck getDeck() {
		return this.deck;
	}
	
	/**
	 * a method for retrieving the list of players.
	 * @return A java ArrayList of type CardGamePlayer with current list of players
	 */
	public ArrayList<CardGamePlayer> getPlayerList(){
		return this.playerList;
	}
	
	/**
	 * a method for retrieving the list of hands played on the table.
	 * @return A java ArrayList of type Hand with all hands that have been played
	 */
	public ArrayList<Hand> getHandsOnTable(){
		return this.handsOnTable;
	}
	
	/**
	 * a method for retrieving the index of the current player.
	 * @return index (integer) of the current active player.
	 */
	public int getCurrentIdx() {
		return this.currentIdx;
	}
	
	/**
	 * a method for starting the game with a (shuffled) deck of
cards supplied as the argument. It implements the Big Two game logics.
	 * @param deck A deck of cards to distribute and start with
	 */
	public void start(Deck deck) {
		for(int i = 0; i < this.getNumOfPlayers(); i++) {
			this.getPlayerList().get(i).removeAllCards();
		}
		this.getHandsOnTable().clear();
		for (int i = 0; i < deck.size(); i++) {
			this.getPlayerList().get(i%getNumOfPlayers()).addCard(deck.getCard(i));
			if (deck.getCard(i).getRank() == 2 && deck.getCard(i).getSuit() == 0) {
				currentIdx = i % getNumOfPlayers();
				table.setActivePlayer(this.getCurrentIdx());
			}
		}
		firstturn = true;
		table.reset();
		table.repaint();
		table.printMsg(this.getPlayerList().get(this.getCurrentIdx()).getName()+"'s turn:\n");
	}
	
	/**
	 * Makes a move by the player.
	 * 
	 * @param playerID
	 *            the playerID of the player who makes the move
	 * @param cardIdx
	 *            the list of the indices of the cards selected by the player
	 */
	public void makeMove(int playerID, int[] cardIdx) {
		checkMove(playerID,cardIdx);
	}

	/**
	 * Checks the move made by the player.
	 * 
	 * @param playerID
	 *            the playerID of the player who makes the move
	 * @param cardIdx
	 *            the list of the indices of the cards selected by the player
	 */
	public void checkMove(int playerID, int[] cardIdx) {
		Hand attempt = cardIdx != null ? composeHand(this.getPlayerList().get(playerID),this.getPlayerList().get(playerID).play(cardIdx)) : null;
		if (cardIdx == null && (playerID == lastHandIdx || firstturn)) {
			table.printMsg("{pass}");
			table.printMsg(" <== Not a legal move!!!\n");
		} else if (cardIdx == null){
			table.printMsg("{pass}\n");
			currentIdx = (playerID + 1) % getNumOfPlayers();
			table.setActivePlayer(this.getCurrentIdx());
			firstturn = false;
			table.printMsg(this.getPlayerList().get(this.getCurrentIdx()).getName()+"'s turn:\n");
		} else if (attempt == null) {
			table.print(this.getPlayerList().get(playerID).play(cardIdx));
			table.printMsg(" <== Not a legal move!!!\n");
		} else if (firstturn && !attempt.contains(new BigTwoCard(0,2))) {
			table.print(attempt);
			table.printMsg(" <== Not a legal move!!!\n");
		} else if ((!this.getHandsOnTable().isEmpty() && attempt!=null && playerID != lastHandIdx) ? (!(attempt.beats(this.getHandsOnTable().get(this.getHandsOnTable().size() - 1))) || attempt.size() != this.getHandsOnTable().get(this.getHandsOnTable().size() - 1).size()) : false) {
			table.print(attempt);
			table.printMsg(" <== Not a legal move!!!\n");
		} else {
			this.getPlayerList().get(playerID).removeCards(this.getPlayerList().get(playerID).play(cardIdx));
			attempt.sort();
			this.getHandsOnTable().add(attempt);
			lastHandIdx = playerID;
			table.printMsg("{"+attempt.getType()+"} ");
			table.print(attempt);
			table.printMsg("\n");
			currentIdx = (playerID + 1) % getNumOfPlayers();
			table.setActivePlayer(this.getCurrentIdx());
			firstturn = false;
			table.printMsg(this.getPlayerList().get(this.getCurrentIdx()).getName()+"'s turn:\n");
		}
		table.resetSelected();
		table.repaint();
		
		if (endOfGame()) {
			table.printMsg("Game ends\n");
			for (int i = 0; i < this.getNumOfPlayers(); i++) {
				if (this.getPlayerList().get(i).getNumOfCards() == 0) {
					table.printMsg(this.getPlayerList().get(i).getName() + " wins the game.\n");
				} else {
					table.printMsg(this.getPlayerList().get(i).getName() + " has " + this.getPlayerList().get(i).getNumOfCards() + " cards in hand.\n");
				}
			}
			table.disable();
		}
	}

	/**
	 * Checks for end of game.
	 * 
	 * @return true if the game ends; false otherwise
	 */
	public boolean endOfGame() {
		for (int i = 0; i < getNumOfPlayers(); i++) {
			if (this.getPlayerList().get(i).getCardsInHand().isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * a method for returning a valid hand from the specified list of cards of the player. Returns null is no valid hand can be composed from the specified list of cards.
	 * @param player a player of type CardGamePlayer to associate the cards with
	 * @param cards a CardList to check for a valid hand
	 * @return a valid hand if it exists or null
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		Hand temp = new StraightFlush(player,cards);
		if (temp.isValid()) {
			return temp;
		}
		temp = new Quad(player,cards);
		if (temp.isValid()) {
			return temp;
		}
		temp = new Quad(player,cards);
		if (temp.isValid()) {
			return temp;
		}
		temp = new FullHouse(player,cards);
		if (temp.isValid()) {
			return temp;
		}
		temp = new Flush(player,cards);
		if (temp.isValid()) {
			return temp;
		}
		temp = new Straight(player,cards);
		if (temp.isValid()) {
			return temp;
		}
		temp = new Triple(player,cards);
		if (temp.isValid()) {
			return temp;
		}
		temp = new Pair(player,cards);
		if (temp.isValid()) {
			return temp;
		}
		temp = new Single(player,cards);
		if (temp.isValid()) {
			return temp;
		}
		return null;

	}
	
	/**
	 * a method for starting a Big Two card game. It should create a Big Two card game, create and shuffle a deck of cards, and start the game with the deck of cards.
	 * @param args Arguments for the main function
	 */
	public static void main(String[] args) {
		BigTwo cardGame = new BigTwo();
		cardGame.deck = new BigTwoDeck();
		cardGame.deck.initialize();
		cardGame.deck.shuffle();
		cardGame.start((BigTwoDeck)cardGame.getDeck());
	}
}


/**
 * Models a Single hand. This hand consists of only one single card. The only card in a single is referred to as the top card of this single. A single with a higher rank beats a single with a lower rank. For singles with the same rank, the one with a higher suit beats the one with a lower suit.
 * @author wali
 */
public class Single extends Hand {
	/**
	 * a constructor for building a Single hand with the specified player and list of cards.
	 * @param player A specified player of time CardGamePlayer to associate the hand with.
	 * @param cards A CardList to make the hand from.
	 */
	Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for checking if this is a valid Single hand.
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		this.sort();
		if (this.size() == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying that this is a Single hand
	 */
	public String getType() {
		return "Single";
	}
}

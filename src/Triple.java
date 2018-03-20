
/**
 * Models a Triple hand. This hand consists of three cards with the same rank. The card with the highest suit in a triple is referred to as the top card of this triple. A triple with a higher rank beats a triple with a lower rank.
 * @author wali
 */
public class Triple extends Hand {
	/**
	 * a constructor for building a Triple hand with the specified player and list of cards.
	 * @param player A specified player of time CardGamePlayer to associate the hand with.
	 * @param cards A CardList to make the hand from.
	 */
	Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for checking if this is a valid Triple hand.
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		this.sort();
		if (this.size() == 3) {
			if (this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(0).getRank() == this.getCard(2).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying that this is a Triple hand
	 */
	public String getType() {
		return "Triple";
	}
}

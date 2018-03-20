
/**
 * Models a Straight hand. This hand consists of five cards with consecutive ranks. For the sake of simplicity, 2 and A can only form a straight with K but not with 3. The card with the highest rank in a straight is referred to as the top card of this straight. A straight having a top card with a higher rank beats a straight having a top card with a lower rank. For straights having top cards with the same rank, the one having a top card with a higher suit beats the one having a top card with a lower suit.
 * @author wali
 */
public class Straight extends Hand {
	/**
	 * a constructor for building a Straight hand with the specified player and list of cards.
	 * @param player A specified player of time CardGamePlayer to associate the hand with.
	 * @param cards A CardList to make the hand from.
	 */
	Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for checking if this is a valid Straight hand.
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		this.sort();
		if (this.size() == 5) {
			for (int i = 0; i < 4; i++) {
				if ((((this.getCard(i).getRank() - 2) % 13) + 13) % 13 != (((((this.getCard(i+1).getRank() - 2) % 13) + 13) % 13) - 1)) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying that this is a Straight hand
	 */
	public String getType() {
		return "Straight";
	}
}

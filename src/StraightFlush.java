
/**
 * Models a Straight Flush hand. This hand consists of five cards with the same suit. The card with the highest rank in a flush is referred to as the top card of this flush. A flush always beats any straights. A flush with a higher suit beats a flush with a lower suit. For flushes with the same suit, the one having a top card with a higher rank beats the one having a top card with a lower rank.
 * @author wali
 */
public class StraightFlush extends Hand {
	/**
	 * a constructor for building a StraightFlush hand with the specified player and list of cards.
	 * @param player A specified player of time CardGamePlayer to associate the hand with.
	 * @param cards A CardList to make the hand from.
	 */
	StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for checking if this is a valid Straight Flush hand.
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		this.sort();
		if (this.size() == 5) {
			for (int i = 0; i < 4; i++) {
				if (this.getCard(i).getSuit() != this.getCard(i+1).getSuit() || (((this.getCard(i).getRank() - 2) % 13) + 13) % 13 != (((((this.getCard(i+1).getRank() - 2) % 13) + 13) % 13) - 1)) {
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
	 * @return a string specifying that is a Straight Flush hand.
	 */
	public String getType() {
		return "StraightFlush";
	}
}

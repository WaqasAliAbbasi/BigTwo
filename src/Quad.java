
/**
 * Models a Quad hand. This hand consists of five cards, with four having the same rank. The card in the quadruplet with the highest suit in a quad is referred to as the top card of this quad. A quad always beats any straights, flushes and full houses. A quad having a top card with a higher rank beats a quad having a top card with a lower rank.
 * @author wali
 */
public class Quad extends Hand {
	/**
	 * a constructor for building a Quad hand with the specified player and list of cards.
	 * @param player A specified player of time CardGamePlayer to associate the hand with.
	 * @param cards A CardList to make the hand from.
	 */
	Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for retrieving the top card of Quad hand.
	 * @return Get top card of the hand of type Card. The card in the quadruplet with the highest suit in a quad is referred to as the top card of this quad.
	 */
	public Card getTopCard() {
		this.sort();
		int[][] ranks = new int[13][3];
		for (int i = 0; i < this.size(); i++) {
			ranks[this.getCard(i).getRank()][0]++;
			if (this.getCard(i).getSuit() > ranks[this.getCard(i).getRank()][1]) {
				ranks[this.getCard(i).getRank()][1] = this.getCard(i).getSuit();
				ranks[this.getCard(i).getRank()][2] = i;
			}
		}
		for (int i = 0; i < 13; i++) {
			if (ranks[i][0] == 4) {
				return this.getCard(ranks[i][2]);
			}
		}
		return this.getCard(this.size() - 1);
	}
	
	/**
	 * a method for checking if this is a valid Quad hand.
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		this.sort();
		if (this.size() == 5) {
			int[] ranks = new int[13];
			for (int i = 0; i < 5; i++) {
				ranks[this.getCard(i).getRank()]++;
			}
			for (int i = 0; i < 13; i++) {
				if (ranks[i] == 4) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying that this is a Quad hand
	 */
	public String getType() {
		return "Quad";
	}

}

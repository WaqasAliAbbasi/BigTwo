
/**
 * Models a Full House hand. This hand consists of five cards, with two having the same rank and three having another same rank. The card in the triplet with the highest suit in a full house is referred to as the top card of this full house. A full house always beats any straights and flushes. A full house having a top card with a higher rank beats a full house having a top card with a lower rank.
 * @author wali
 */
public class FullHouse extends Hand {

	/**
	 * a constructor for building a Full House hand with the specified player and list of cards.
	 * @param player A specified player of time CardGamePlayer to associate the hand with.
	 * @param cards A CardList to make the hand from.
	 */
	FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for retrieving the top card of Full House hand.
	 * @return Get top card of the hand of type Card. The card in the triplet with the highest suit in a full house is referred to as the top card of this full house.
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
			if (ranks[i][0] == 3) {
				return this.getCard(ranks[i][2]);
			}
		}
		return this.getCard(this.size() - 1);
	}
	
	/**
	 * a method for checking if this is a valid Full House hand.
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		this.sort();
		if (this.size() == 5) {
			int[] ranks = new int[13];
			for (int i = 0; i < 5; i++) {
				ranks[this.getCard(i).getRank()]++;
			}
			boolean twosame = false;
			boolean threesame = false;
			for (int i = 0; i < 13; i++) {
				if (ranks[i] == 2) {
					twosame = true;
				} else if (ranks[i] == 3) {
					threesame = true;
				}
				if (twosame && threesame) {
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
	 * @return a string specifying that this is a Full House hand
	 */
	public String getType() {
		return "FullHouse";
	}

}


/**
 * The BigTwoCard class is a subclass of the Card class, and is used to model a card used in a Big Two card game. It overrides the compareTo() method it inherited from the Card class to reflect the ordering of cards used in a Big Two card game.
 * @author wali
 */
public class BigTwoCard extends Card {
	/**
	 * a constructor for building a card with the specified suit and rank. It calls the parent constructer with the same parameters.
	 * @param suit suit is an integer between 0 and 3
	 * @param rank rank is an integer between 0 and 12
	 */
	BigTwoCard(int suit, int rank){
		super(suit,rank);
	}
	
	/**
	 * a method for comparing this card with the specified card for order. Returns a negative integer, zero, or a positive integer as this card is less than, equal to, or greater than the specified card. Implements the Big Two rules specifically.
	 * @param card The card to compare with
	 * @return 1 if higher, 0 if same, -1 if lower (integers)
	 */
	public int compareTo(Card card) {
		if ((((this.rank - 2) % 13) + 13) % 13 > (((card.rank - 2) % 13) + 13) % 13) {
			return 1;
		} else if ((((this.rank - 2) % 13) + 13) % 13 < (((card.rank - 2) % 13) + 13) % 13) {
			return -1;
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
	}
}

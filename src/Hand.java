
/**
 * The Hand class is a subclass of the CardList class, and is used to model a hand of cards. It has a private instance variable for storing the player who plays this hand. It also has methods for getting the player of this hand, checking if it is a valid hand, getting the type of this hand, getting the top card of this hand, and checking if it beats a specified hand.
 * @author wali
 */
public abstract class Hand extends CardList {
	/**
	 * a constructor for building a hand with the specified player and list of cards.
	 * @param player A specified player of time CardGamePlayer to associate the hand with.
	 * @param cards A CardList to make the hand from.
	 */
	Hand(CardGamePlayer player, CardList cards){
		this.player = player;
		for (int i = 0; i < cards.size(); i++) {
			this.addCard(cards.getCard(i));
		}
	}
	
	private CardGamePlayer player;
	
	/**
	 * a method for retrieving the player of this hand.
	 * @return Get player of the hand of type CardGamePlayer
	 */
	public CardGamePlayer getPlayer() {
		return this.player;
	}
	
	/**
	 * a method for retrieving the top card of this hand.
	 * @return Get top card of the hand of type Card
	 */
	public Card getTopCard() {
		this.sort();
		return this.getCard(this.size()-1);
	}
	
	/**
	 * a method for checking if this hand beats a specified hand.
	 * @param hand A hand to check with
	 * @return true if beats, false otherwise
	 */
	public boolean beats(Hand hand) {
		if (this.getType() == "StraightFlush") {
			if (hand.getType() == "StraightFlush") {
				if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
					return true; 
				} else {
					return false; 
				}
			} else {
				return true;
			}
		} else if (this.getType() == "Quad") {
			if (hand.getType() == "Quad") {
				if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
					return true; 
				} else {
					return false;
				}
			} else {
				return true;
			}
		} else if (this.getType() == "FullHouse") {
			if (hand.getType() == "FullHouse") {
				if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
					return true; 
				} else {
					return false;
				}
			} else {
				return true;
			}
		} else if (this.getType() == "Flush") {
			if (hand.getType() == "Flush") {
				if (this.getTopCard().getSuit() == hand.getTopCard().getSuit()) {
					if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
						return true; 
					} else {
						return false;
					} 
				} else {
					return this.getTopCard().getSuit() > hand.getTopCard().getSuit(); 
				}
			} else {
				return true;
			}
		} else if (this.getType() == "Straight") {
			if (hand.getType() == "Straight") {
				if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
					return true; 
				} else {
					return false;
				}
			} else {
				return true;
			}
		}  else if (this.getType() == "Triple") {
			if (hand.getType() == "Triple") {
				if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
					return true; 
				} else {
					return false;
				}
			} else {
				return true;
			}
		}  else if (this.getType() == "Pair") {
			if (hand.getType() == "Pair") {
				if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
					return true; 
				} else {
					return false;
				}
			} else {
				return true;
			}
		} else {
			if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
				return true; 
			} else {
				return false;
			}
		}
	}
	
	/**
	 * a method for checking if this is a valid hand.
	 * @return true if valid, false otherwise
	 */
	public abstract boolean isValid();
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying the type of this hand
	 */
	public abstract String getType();
}

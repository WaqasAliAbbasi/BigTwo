import java.util.ArrayList;

import javax.swing.JOptionPane;

import java.net.*;
import java.io.*;

public class BigTwoClient implements CardGame, NetworkGame{
	private final static int NUM_OF_PLAYERS = 4;
	
	BigTwoClient(){
		this.playerList = new ArrayList<CardGamePlayer>();
		for (int i = 0; i < NUM_OF_PLAYERS; i++) {
			this.playerList.add(new CardGamePlayer());
		}
		this.numOfPlayers = this.playerList.size();
		this.handsOnTable = new ArrayList<Hand>();
		this.table = new BigTwoTable(this);
		
		String name = JOptionPane.showInputDialog("Enter Your Name: ");
		if (name != null) {
			this.setPlayerName(name);
			this.setServerIP("127.0.0.1");
			this.setServerPort(2396);
			this.makeConnection();
			table.disable();
		} else {
			this.table.printMsg("Please enter your name.\n");
			this.table.disable();
		}
	}
	
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int playerID;
	private int lastHandIdx;
	private boolean firstturn;
	private String playerName;
	private String serverIP;
	private int serverPort;
	private Socket sock;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private int currentIdx;
	private BigTwoTable table;
	
	@Override
	public int getPlayerID() {
		return this.playerID;
	}

	@Override
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	public boolean isConnected() {
		if (sock.isClosed()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public String getPlayerName() {
		return this.playerName;
	}

	@Override
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	@Override
	public String getServerIP() {
		return this.serverIP;
	}

	@Override
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	@Override
	public int getServerPort() {
		return this.serverPort;
	}

	@Override
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	@Override
	public void makeConnection() {
		if (sock == null ? true : !sock.isConnected() ) {
			try {
				sock = new Socket(this.getServerIP(),this.getServerPort());
				oos = new ObjectOutputStream(sock.getOutputStream());
				Thread thread = new Thread(new ServerHandler());
				thread.start();
				
				CardGameMessage join = new CardGameMessage(CardGameMessage.JOIN,-1,this.getPlayerName());
				sendMessage(join);
				
				CardGameMessage ready = new CardGameMessage(CardGameMessage.READY,-1,null);
				sendMessage(ready);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void parseMessage(GameMessage message) {
		if (message.getType() == CardGameMessage.MOVE) {
			checkMove(message.getPlayerID(),(int[]) message.getData());
		} else if (message.getType() == CardGameMessage.PLAYER_LIST) {
			this.setPlayerID(message.getPlayerID());
			for (int i = 0; i < this.getNumOfPlayers(); i++) {
				if (((String[]) message.getData())[i] != null){
					this.getPlayerList().get(i).setName(((String[]) message.getData())[i]);
				}
			}
			this.table.repaint();
		} else if (message.getType() == CardGameMessage.FULL) {
			this.table.printMsg("The server is full and the game cannot be joined.\n");
			try {
				this.sock.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (message.getType() == CardGameMessage.QUIT) {
			this.getPlayerList().get(message.getPlayerID()).setName("");
			if (!this.endOfGame()) {
				this.table.disable();
				CardGameMessage ready = new CardGameMessage(CardGameMessage.READY,-1,null);
				sendMessage(ready);
			}
		} else if (message.getType() == CardGameMessage.JOIN) {
			this.getPlayerList().get(message.getPlayerID()).setName((String)message.getData());
			this.table.repaint();
		} else if (message.getType() == CardGameMessage.READY) {
			this.table.printMsg("Player " + message.getPlayerID() + " is ready.\n");
		} else if (message.getType() == CardGameMessage.START) {
			deck = (Deck) message.getData();
			this.start(deck);
		} else if (message.getType() == CardGameMessage.MSG) {
			this.table.printChatMsg((String) message.getData());
		}
	}

	@Override
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getNumOfPlayers() {
		return this.numOfPlayers;
	}

	@Override
	public Deck getDeck() {
		return this.deck;
	}

	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		return this.playerList;
	}

	@Override
	public ArrayList<Hand> getHandsOnTable() {
		return this.handsOnTable;
	}

	@Override
	public int getCurrentIdx() {
		return this.currentIdx;
	}

	@Override
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
		table.printMsg("All players are ready. Game starts.\n");
		table.printMsg(this.getPlayerList().get(this.getCurrentIdx()).getName()+"'s turn:\n");
		table.repaint();
	}

	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		CardGameMessage move = new CardGameMessage(CardGameMessage.MOVE,-1,cardIdx);
		sendMessage(move);
	}

	@Override
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
			String message = "Game ends\n";
			for (int i = 0; i < this.getNumOfPlayers(); i++) {
				if (this.getPlayerList().get(i).getNumOfCards() == 0) {
					message += this.getPlayerList().get(i).getName() + " wins the game.\n";
				} else {
					message += this.getPlayerList().get(i).getName() + " has " + this.getPlayerList().get(i).getNumOfCards() + " cards in hand.\n";
				}
			}
			table.disable();
			JOptionPane.showMessageDialog(null, message);
			CardGameMessage ready = new CardGameMessage(CardGameMessage.READY,-1,null);
			sendMessage(ready);
			
		}
	}

	@Override
	public boolean endOfGame() {
		for (int i = 0; i < getNumOfPlayers(); i++) {
			if (this.getPlayerList().get(i).getCardsInHand().isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	class ServerHandler implements Runnable {
		@Override
		public void run() {	
			try {
				ois = new ObjectInputStream(sock.getInputStream());
				CardGameMessage message;
				// reads incoming messages from the server
				while (!sock.isClosed()) {
					if ((message = (CardGameMessage) ois.readObject()) != null) {
						parseMessage(message);
					}
				} // close while
				ois.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		}
		
	}
	
	public static void main(String[] args) {
		BigTwoClient client = new BigTwoClient();
	}
	
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
}

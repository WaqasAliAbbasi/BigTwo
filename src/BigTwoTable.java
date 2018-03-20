import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

/**
 * The BigTwoTable class implements the CardGameTable interface. It is used to build a GUI for the Big Two card game and handle all user actions.
 * @author wali
 */
public class BigTwoTable implements CardGameTable {
	/**
	 * a constructor for creating a BigTwoTable. The parameter game is a reference to a card game associates with this table.
	 * @param game A Card Game of BigTwo type to play through this GUI
	 */
	BigTwoTable(CardGame game){
		this.game = (BigTwoClient) game;
		this.selected = new boolean[13];
		loadImages();
		guiSetup();
	}
	
	private BigTwoClient game;
	private boolean[] selected;
	private int activePlayer;
	private JFrame frame;
	private JPanel bigTwoPanel;
	private JButton playButton;
	private JButton passButton;
	private JMenu menu;
	private JTextArea msgArea;
	private JTextArea chatArea;
	private JTextField chatTypeArea;
	private Image[][] cardImages;
	private Image cardBackImage;
	private Image[] avatars;
	private boolean allowClick;
	
	private void guiSetup() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 800);
		frame.setResizable(false);
		
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setPreferredSize(new Dimension(700,800));
	    frame.add(bigTwoPanel,BorderLayout.WEST);
		
	    JMenuBar menuBar = new JMenuBar();
	    menu = new JMenu("Game");
	    menuBar.add(menu);
	    JMenuItem menuItem1 = new JMenuItem("Connect");
	    menuItem1.addActionListener(new ConnectMenuItemListener());
	    menu.add(menuItem1);
	    
	    JMenuItem menuItem2 = new JMenuItem("Quit");
	    menuItem2.addActionListener(new QuitMenuItemListener());
	    menu.add(menuItem2);
	    frame.add(menuBar, BorderLayout.NORTH);
	    
	    JPanel messages = new JPanel();
	    messages.setLayout(new BoxLayout(messages, BoxLayout.PAGE_AXIS));
	    
	    msgArea = new JTextArea(20,24);
	    msgArea.setEnabled(false);
	    DefaultCaret caret = (DefaultCaret) msgArea.getCaret();
	    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setViewportView(msgArea);
	    messages.add(scrollPane);
	    
	    chatArea = new JTextArea(21,24);
	    chatArea.setEnabled(false);;
	    DefaultCaret caretChat = (DefaultCaret) chatArea.getCaret();
	    caretChat.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	    JScrollPane scrollPaneChat = new JScrollPane();
	    scrollPaneChat.setViewportView(chatArea);
	    messages.add(scrollPaneChat);
	    
	    JPanel chat = new JPanel();
	    chat.setLayout(new FlowLayout());
	    chat.add(new JLabel("Message:"));
	    chatTypeArea = new JTextField();
	    chatTypeArea.getDocument().putProperty("filterNewlines", Boolean.TRUE);
	    chatTypeArea.addActionListener(new EnterListener());
	    chatTypeArea.setPreferredSize( new Dimension( 200, 24 ) );
	    chat.add(chatTypeArea);
	    messages.add(chat);
	    
	    frame.add(messages, BorderLayout.EAST);
	    
	    JPanel buttons = new JPanel();
	    playButton = new JButton("Play");
	    playButton.addActionListener(new PlayButtonListener());
	    passButton = new JButton("Pass");
	    passButton.addActionListener(new PassButtonListener());
	    buttons.add(playButton);
	    buttons.add(passButton);
	    frame.add(buttons, BorderLayout.SOUTH);
	    
	    frame.setVisible(true);
	}
	
	private void loadImages() {
		avatars = new Image[4];
		avatars[0] = new ImageIcon("assets/avatars/batman_72.png").getImage();
		avatars[1] = new ImageIcon("assets/avatars/flash_72.png").getImage();
		avatars[2] = new ImageIcon("assets/avatars/superman_72.png").getImage();
		avatars[3] = new ImageIcon("assets/avatars/wonder_woman_72.png").getImage();
		
		cardBackImage = new ImageIcon("assets/cards/b.gif").getImage();
		
		cardImages = new Image[4][13];
		cardImages[0][0] = new ImageIcon("assets/cards/ad.gif").getImage();
		cardImages[1][0] = new ImageIcon("assets/cards/ac.gif").getImage();
		cardImages[2][0] = new ImageIcon("assets/cards/ah.gif").getImage();
		cardImages[3][0] = new ImageIcon("assets/cards/as.gif").getImage();
		
		cardImages[0][1] = new ImageIcon("assets/cards/2d.gif").getImage();
		cardImages[1][1] = new ImageIcon("assets/cards/2c.gif").getImage();
		cardImages[2][1] = new ImageIcon("assets/cards/2h.gif").getImage();
		cardImages[3][1] = new ImageIcon("assets/cards/2s.gif").getImage();
		
		cardImages[0][2] = new ImageIcon("assets/cards/3d.gif").getImage();
		cardImages[1][2] = new ImageIcon("assets/cards/3c.gif").getImage();
		cardImages[2][2] = new ImageIcon("assets/cards/3h.gif").getImage();
		cardImages[3][2] = new ImageIcon("assets/cards/3s.gif").getImage();
		
		cardImages[0][3] = new ImageIcon("assets/cards/4d.gif").getImage();
		cardImages[1][3] = new ImageIcon("assets/cards/4c.gif").getImage();
		cardImages[2][3] = new ImageIcon("assets/cards/4h.gif").getImage();
		cardImages[3][3] = new ImageIcon("assets/cards/4s.gif").getImage();
		
		cardImages[0][4] = new ImageIcon("assets/cards/5d.gif").getImage();
		cardImages[1][4] = new ImageIcon("assets/cards/5c.gif").getImage();
		cardImages[2][4] = new ImageIcon("assets/cards/5h.gif").getImage();
		cardImages[3][4] = new ImageIcon("assets/cards/5s.gif").getImage();
		
		cardImages[0][5] = new ImageIcon("assets/cards/6d.gif").getImage();
		cardImages[1][5] = new ImageIcon("assets/cards/6c.gif").getImage();
		cardImages[2][5] = new ImageIcon("assets/cards/6h.gif").getImage();
		cardImages[3][5] = new ImageIcon("assets/cards/6s.gif").getImage();
		
		cardImages[0][6] = new ImageIcon("assets/cards/7d.gif").getImage();
		cardImages[1][6] = new ImageIcon("assets/cards/7c.gif").getImage();
		cardImages[2][6] = new ImageIcon("assets/cards/7h.gif").getImage();
		cardImages[3][6] = new ImageIcon("assets/cards/7s.gif").getImage();
		
		cardImages[0][7] = new ImageIcon("assets/cards/8d.gif").getImage();
		cardImages[1][7] = new ImageIcon("assets/cards/8c.gif").getImage();
		cardImages[2][7] = new ImageIcon("assets/cards/8h.gif").getImage();
		cardImages[3][7] = new ImageIcon("assets/cards/8s.gif").getImage();
		
		cardImages[0][8] = new ImageIcon("assets/cards/9d.gif").getImage();
		cardImages[1][8] = new ImageIcon("assets/cards/9c.gif").getImage();
		cardImages[2][8] = new ImageIcon("assets/cards/9h.gif").getImage();
		cardImages[3][8] = new ImageIcon("assets/cards/9s.gif").getImage();
		
		cardImages[0][9] = new ImageIcon("assets/cards/td.gif").getImage();
		cardImages[1][9] = new ImageIcon("assets/cards/tc.gif").getImage();
		cardImages[2][9] = new ImageIcon("assets/cards/th.gif").getImage();
		cardImages[3][9] = new ImageIcon("assets/cards/ts.gif").getImage();
		
		cardImages[0][10] = new ImageIcon("assets/cards/jd.gif").getImage();
		cardImages[1][10] = new ImageIcon("assets/cards/jc.gif").getImage();
		cardImages[2][10] = new ImageIcon("assets/cards/jh.gif").getImage();
		cardImages[3][10] = new ImageIcon("assets/cards/js.gif").getImage();
		
		cardImages[0][11] = new ImageIcon("assets/cards/qd.gif").getImage();
		cardImages[1][11] = new ImageIcon("assets/cards/qc.gif").getImage();
		cardImages[2][11] = new ImageIcon("assets/cards/qh.gif").getImage();
		cardImages[3][11] = new ImageIcon("assets/cards/qs.gif").getImage();
		
		cardImages[0][12] = new ImageIcon("assets/cards/kd.gif").getImage();
		cardImages[1][12] = new ImageIcon("assets/cards/kc.gif").getImage();
		cardImages[2][12] = new ImageIcon("assets/cards/kh.gif").getImage();
		cardImages[3][12] = new ImageIcon("assets/cards/ks.gif").getImage();
	}
	
	/**
	 * Sets the index of the active player (i.e., the current player).
	 * 
	 * @param activePlayer
	 *            an int value representing the index of the active player
	 */
	public void setActivePlayer(int activePlayer) {
		this.activePlayer = activePlayer;
	}

	/**
	 * Returns an array of indices of the cards selected.
	 * 
	 * @return an array of indices of the cards selected
	 */
	public int[] getSelected() {
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int i = 0; i < selected.length; i++) {
			if (selected[i] == true) {
				temp.add(i);
			}
		}
		if (temp.size() > 0) {
			int[] ints = new int[temp.size()];
			for(int i=0, len = temp.size(); i < len; i++)
			   ints[i] = temp.get(i);
			return ints;
		} else {
			return null;
		}
		
	}

	/**
	 * Resets the list of selected cards to an empty list.
	 */
	public void resetSelected() {
		this.selected = new boolean[13];
		this.repaint();
	}
	
	/**
	 * Prints the cards of CardList in a pretty way into the message area
	 * @param cards cards list of type CardList to be printed
	 */
	public void print(CardList cards) {
		boolean printFront = true;
		boolean printIndex = false;
		if (cards.size() > 0) {
			for (int i = 0; i < cards.size(); i++) {
				String string = "";
				if (printIndex) {
					string = i + " ";
				}
				if (printFront) {
					string = string + "[" + cards.getCard(i) + "]";
				} else {
					string = string + "[  ]";
				}
				if (i % 13 != 0) {
					string = " " + string;
				}
				this.printMsg(string);
			}
		} else {
			this.printMsg("[Empty]");
		}
	}

	/**
	 * Repaints the GUI.
	 */
	public void repaint() {
		frame.repaint();
	}

	/**
	 * Prints the specified string to the message area of the card game table.
	 * 
	 * @param msg
	 *            the string to be printed to the message area of the card game
	 *            table
	 */
	public void printMsg(String msg) {
		msgArea.append(msg);
	}
	
	/**
	 * Prints the specified string to the chat message area of the card game table.
	 * 
	 * @param msg
	 *            the string to be printed to the chat message area of the card game
	 *            table
	 */
	public void printChatMsg(String msg) {
		chatArea.append(msg+"\n");
	}
	
	/**
	 * Clears the message area of the card game table.
	 */
	public void clearChatMsgArea() {
		chatArea.setText("");
	}

	/**
	 * Clears the message area of the card game table.
	 */
	public void clearMsgArea() {
		msgArea.setText("");
	}

	/**
	 * Resets the GUI.
	 */
	public void reset() {
		resetSelected();
		clearMsgArea();
		enable();
	}

	/**
	 * Enables user interactions.
	 */
	public void enable() {
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		allowClick = true;
	}

	/**
	 * Disables user interactions.
	 */
	public void disable() {
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		allowClick = false;
		
	}
	
	/**
	 * an inner class that extends the JPanel class and implements the MouseListener interface. Overrides the paintComponent() method inherited from the JPanel class to draw the card game table. Implements the mouseClicked() method from the MouseListener interface to handle mouse click events.
	 * @author wali
	 */
	class BigTwoPanel extends JPanel implements MouseListener{
		
		/**
		 * BigTwoPanel default constructor which adds the Mouse Listener and sets background of the card table.
		 */
		BigTwoPanel(){
			this.addMouseListener(this);
	        setBackground(new Color(7,99,36)); 
		}

		/**
		 * Draws the avatars, text and cards on card table
		 * @param g Provided by system to allow drawing
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.WHITE);
			for (int i = 0; i < game.getNumOfPlayers(); i++) {
				if (i == game.getPlayerID()) {
					if (i == game.getCurrentIdx()) {
						g.setColor(Color.BLUE);
					} else {
						g.setColor(Color.YELLOW);
					}
					g.drawString(game.getPlayerList().get(i).getName()+" (You)",15,15+i*130);
					g.setColor(Color.WHITE);
				} else if (i == game.getCurrentIdx()) {
					g.setColor(Color.BLUE);
					g.drawString(game.getPlayerList().get(i).getName(),15,15+i*130);
					g.setColor(Color.WHITE);
				} else {
					g.drawString(game.getPlayerList().get(i).getName(),15,15+i*130);
				}
				g.drawImage(avatars[i],5,20+i*130,this);
				for (int j = 0; j < game.getPlayerList().get(i).getNumOfCards(); j++) {
					if (i == game.getPlayerID()) {
						int s = game.getPlayerList().get(i).getCardsInHand().getCard(j).getSuit();
						int r = game.getPlayerList().get(i).getCardsInHand().getCard(j).getRank();
						if (selected[j] == true) {
							g.drawImage(cardImages[s][r],80+j*cardImages[0][0].getWidth(this)/2,10+i*130-10,this);
						} else {
							g.drawImage(cardImages[s][r],80+j*cardImages[0][0].getWidth(this)/2,10+i*130,this);
						}
					} else {
						g.drawImage(cardBackImage,80+j*cardImages[0][0].getWidth(this)/2,10+i*130,this);
					}
				}
			}
			
			if (game.getHandsOnTable().size()-1 > -1) {
				Hand lasthand = game.getHandsOnTable().get(game.getHandsOnTable().size()-1);
				g.drawString("Played by "+lasthand.getPlayer().getName(), 5, 10+4*130 - 10);
				for (int i = 0; i < lasthand.size();i++) {
					int s = lasthand.getCard(i).getSuit();
					int r = lasthand.getCard(i).getRank();
					g.drawImage(cardImages[s][r],5+i*20,10+4*130,this);
				}
			}
		}

		/**
		 * Defines what happens when mouse is clicked on the card table. Only allows clicks on cards of active player. Once cards are selected, the JPanel is repainted to reflect changes.
		 * @param e Mouse event created when Mouse Clicked
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if (allowClick && activePlayer == ((BigTwoClient) game).getPlayerID()) {
				int width = cardImages[0][0].getWidth(this);
				int height = cardImages[0][0].getHeight(this);
				int num = game.getPlayerList().get(activePlayer).getNumOfCards();
				
				int minX = 80;
				int maxX = 80+(width/2)*num+width;
				int minY = 10+activePlayer*130-10;
				int maxY = 10+activePlayer*130+height;
				if (e.getX() >= minX && e.getX() <= maxX && e.getY() >= minY && e.getY() <= maxY) {	
					int card = (int)Math.ceil((e.getX()-80)/(width/2));
					card = card / num > 0 ? num - 1 : card;
					if (selected[card]) {
						if (e.getY() > (maxY - 10) && e.getX() < (80+(width/2)*card + width/2) && selected[card-1] == false) {
							if (card != 0) {
								card = card - 1;
							}
							selected[card] = true;
						} else if (e.getY() < (maxY - 10)){
							selected[card] = false;
						}
					} else if (e.getY() > (minY + 10)){
						selected[card] = true;
					} else if (selected[card - 1] && e.getX() < (80+(width/2)*card + width/2)) {
						selected[card-1] = false;
					}
					this.repaint();
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {	
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {	
		}
	}
	/**
	 * an inner class that implements the ActionListener interface. Implements the actionPerformed() method from the ActionListener interface to handle button-click events for the “Play” button. When the “Play” button is clicked, it calls the makeMove() method of CardGame object to make a move.
	 * @author wali
	 */
	class PlayButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (game.getPlayerID() == game.getCurrentIdx()) {
				if (getSelected() != null) {
					game.makeMove(activePlayer, getSelected());
				} else {
					printMsg("No Cards Selected\n");
				}
			}
		}
		
	}
	
	/**
	 * an inner class that implements the ActionListener interface. Implements the actionPerformed() method from the ActionListener interface to handle button-click events for the “Pass” button. When the “Pass” button is clicked, it calls the makeMove() method of CardGame object to make a move.
	 * @author wali
	 */
	class PassButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (game.getPlayerID() == game.getCurrentIdx()) {
				game.makeMove(activePlayer, null);
			}
		}
	}
	
	/**
	 * an inner class that implements the ActionListener interface. Implements the actionPerformed() method from the ActionListener interface to handle button-click events for the “Send” button.
	 * @author wali
	 */
	class EnterListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			CardGameMessage message = new CardGameMessage(CardGameMessage.MSG,-1,chatTypeArea.getText());
			chatTypeArea.setText("");
			game.sendMessage(message);
		}
	}
	
	/**
	 * an inner class that implements the ActionListener interface. Implements the actionPerformed() method from the ActionListener interface to handle menu-item-click events for the “Connect” menu item.
	 * @author wali
	 */
	class ConnectMenuItemListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!game.isConnected()) {
				reset();
				game.makeConnection();
			}
		}
		
	}
	
	/**
	 * an inner class that implements the ActionListener interface. Implements the actionPerformed() method from the ActionListener interface to handle menu-item-click events for the “Quit” menu item. When the “Quit” menu item is selected, it terminates application.
	 * @author wali
	 */
	class QuitMenuItemListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}

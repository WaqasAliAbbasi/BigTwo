package examples;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleChatClient {
	JTextArea incoming;
	JTextField outgoing;
	BufferedReader reader;
	PrintWriter writer;
	Socket sock;

	public static void main(String[] args) {
		SimpleChatClient client = new SimpleChatClient();
		client.go();
	}

	public void go() {
		// sets up the network connection
		try {
			sock = new Socket("127.0.0.1", 5000);
			InputStreamReader streamReader = new InputStreamReader(
					sock.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("networking established");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// builds the GUI
		JFrame frame = new JFrame("Simple Chat Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// sets up a text arera for showing incoming messages
		incoming = new JTextArea(15, 50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		// sets up a text field for getting use inputs
		outgoing = new JTextField(20);
		// sets up a "Send" button
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());

		// layouts all the components on a panel
		JPanel panel = new JPanel();
		panel.add(qScroller);
		panel.add(outgoing);
		panel.add(sendButton);
		// adds the panel to the frame
		frame.add(panel, BorderLayout.CENTER);
		// makes the frame visible
		frame.setSize(640, 320);
		frame.setVisible(true);

		// starts a new thread to receive messages from the server
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
	} // close go

	public class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				// sends the text in the text field to the server
				writer.println(outgoing.getText());
				writer.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// resets the text field
			outgoing.setText("");
			outgoing.requestFocus();
		}
	} // SendButtonListener

	public class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				// reads incoming messages from the server
				while ((message = reader.readLine()) != null) {
					System.out.println("read " + message);
					// appends the incoming message to the text area
					incoming.append(message + "\n");
				} // close while
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	} // IncomingReader
}

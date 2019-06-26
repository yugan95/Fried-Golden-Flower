import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GameClient {
    private static final int SERVER_PORT = 8888;
    private String playername = "";
    private String serverip = "";
    
    Socket socket = null;
    DataOutputStream dos = null;
    DataInputStream dis = null;
    private boolean bConnected = false;

    private static Scanner in = new Scanner(System.in);
    private static String hostInfo = null;
    Thread tRecv = new Thread(new RecvThread());

    public static void main(String[] args) {
        new GameClient().launch();
    }

    public GameClient() {
    	register("Jerry","127.0.0.1");
    }
    
    // client register.
    public void register(String playername, String serverip) {
    	this.playername = playername;
    	this.serverip = serverip;
    }
    
    public void launch() {
        connect();
        tRecv.start();

        try {
        	while (bConnected) {
                String talk = dis.readUTF();
                parseCommand(talk);
            }
        } catch (EOFException e) {
            System.out.println("server closed!");
        } catch (IOException e) {
            e.printStackTrace();
	    } finally {
	    	try {
	    		disconnect();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
    }

    //prepare ante bet
    public int getAnteBet() {
    	return 1;
    }
    
    //prepare pair plus bet
    public int getPairPlusBet() {
    	return 0;
    }
    
    //prepare follow up
    public boolean getFollowup(CardHand cards) {
    	return false;
    }
    
    // process score.
    public void processScore(GameScore score) {

    }

    // betting action.
    private String Betting() {
    	int anteBet,pairPlusBet;
    	
    	anteBet = getAnteBet();
    	pairPlusBet = getPairPlusBet();
    	
    	// bet range checked.
    	if (anteBet > 6 || anteBet < 1) anteBet = 1;
    	if (pairPlusBet > 2 || pairPlusBet < 0) pairPlusBet = 0;
    	
    	return String.format("%1d %1d", anteBet,pairPlusBet);
    }

    // follow up action.
    private String Followup(String cards) {
    	boolean follow;
    	
    	CardHand cardhand = new CardHand(cards);    	
    	follow = getFollowup(cardhand);
    	
    	return follow ? "T" : "F";
    }

    // parse command from server.
    private void parseCommand(String talk) {
    	String cmd = "";
    	
    	if (talk.length() >= 6) 
    		cmd = talk.substring(0,5);
    	
    	if (cmd.equals("READY")) {
        	System.out.println(talk);
    		sendMessage("BETS \t" + Betting());
    	}
    	else if (cmd.equals("CARDS")) {
        	System.out.println(talk);
    		sendMessage("FLWUP\t" + Followup(talk.substring(6)));
    	}
    	else if (cmd.equals("SCORE")) {
        	System.out.println(talk);
    	}
    	else if (cmd.equals("TOTAL")) {
    		GameScore score = new GameScore(talk.substring(6)); 
    		processScore(score);
    	}
    	else {
    		System.out.println(talk);
    	}
    }
    
    // send command to server.
    private void sendMessage(String message) {
        try {
        	System.out.println(message);
            dos.writeUTF(message);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // connect to server
    public void connect() {
        try {
            socket = new Socket(this.serverip, SERVER_PORT);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            bConnected = true;

            InetAddress addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress().toString();//
            String address = addr.getHostName().toString();//
            hostInfo = "LOGIN\t" + this.playername + "\t"  + ip + "\t" + address + "\tconnected.";
            System.out.println(hostInfo);
            dos.writeUTF(hostInfo);
            dos.flush();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() throws IOException {
        if(dis != null) dis.close();
        if(dos != null) dos.close();
        if(socket != null) {
        	socket.close();
        	socket = null;
        }
    }

    // receive client command.
    private class RecvThread implements Runnable {
        public void run() {
            while (bConnected) {
                try {
                    if (in.hasNext()) {
                        String talk = in.nextLine();
                        if (talk.equals("quit")) {
                            bConnected = false;
                            disconnect();
                            break;
                        }
                        //sendMessage(talk);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

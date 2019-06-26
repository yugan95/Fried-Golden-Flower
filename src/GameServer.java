import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameServer {
    private boolean started = false;
    private ServerSocket ss = null;
    private static Scanner in = new Scanner(System.in);
    private List<Client> clients = new ArrayList<Client>();
    Thread tCmd = new Thread(new CmdThread());
	private GameService service;
    
    public static void main(String[] args) {
        new GameServer().start();
    }
    
    public void start() {
    	service = new GameService(6,10000);
    	try {
            ss = new ServerSocket(8888);
            started = true;
            tCmd.start();
            System.out.println("Server is Ready!");
        } catch (BindException e) {
            System.err.println("port in use.please stop program using this port and restart.");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
        	// listen socket.
        	while(started) {
                Socket s = ss.accept();
                // get available player id.
                String playerID = Player.FreePlayID();
                Client c = new Client(s,playerID);
                new Thread(c).start();
                clients.add(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	stop();
        	System.out.println("Server stoped.");
        }
    }
    
    // stop listen socket.
    private void stop() {
        try {
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // send message to client.
    private void sendMessage(String sType) {
    	
		String balance = sType.equals("TOTAL") ? service.balanceMessage() : "";

    	for (Client client: clients) {
			if (sType.equals("READY")) {
	    		client.send("READY\t");
			}
			else if (sType.equals("CARDS")) {
				client.send("CARDS\t" + service.getCards(client.getName()));
			}
			else if (sType.equals("SCORE")) {
				client.send("SCROE\t" + service.getScore(client.getName()));
			}
			else if (sType.equals("TOTAL")) {
				client.send("TOTAL\t" + balance);
			}
		}
    }
    
    // receive client's message
    private void receivePlayerMessage(Client client,String playerID, String talk) {
    	String cmd = "";
    	
    	if (talk.length() >= 6) 
    		cmd = talk.substring(0,5);
    	System.out.println(playerID + " > " + talk);
    	
    	// player login
    	if (cmd.equals("LOGIN")) {
    		String playerName = talk.substring(6);
    		playerName = playerName.substring(0,playerName.indexOf("\t"));
    		
    		// all player login.
    		if (service.playerLogon(playerID,playerName)) {
    			if (service.hasNextGame()) {
        			service.newGame();
        			sendMessage("READY");
    			}
    		}
    	}
    	//player betting.
    	else if (cmd.equals("BETS ")) {
    		int ante = Integer.parseInt(talk.substring(6,7));
    		int pairplus = Integer.parseInt(talk.substring(8,9));
    		
    		// all player finished betting.
    		if (service.playerBetting(playerID,ante,pairplus)) {
    			service.deal();
    			sendMessage("CARDS");
    		}
    	}
    	// player follow or fold
    	else if (cmd.equals("FLWUP")) {
    		String f = talk.substring(6);
    		boolean followup = f.equals("T") ? true : false;
    		// all player finished follow/fold.
    		if (service.playerFollowup(playerID,followup)) {
    			service.determine();
    			sendMessage("SCORE");
    			sendMessage("TOTAL");
    			
    			service.scoreList();
    			// next game.
    			if (service.hasNextGame()) {
        			service.newGame();
        			sendMessage("READY");
    			}
    		}
    	}
    	// player quit.
    	else if (cmd.equals("QUIT ")) {
    		service.playerLogout(playerID);
    	}
    	else {
    		System.out.println("ERROR COMMAND>>>>>>>>" + talk);
    	}
    }

    
    private class Client implements Runnable {
        private Socket s;
        private String name;
        private DataInputStream dis = null;
        private DataOutputStream dos = null;
        private boolean bConnected = false;
        
        public Client(Socket s, String name) {
            this.s = s;
            this.name = name;
            try {
                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());
                bConnected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        public String getName() {
        	return this.name;
        }
        
        public void close() throws IOException {
        	bConnected = false;
            if(dis != null) dis.close();
            if(dos != null) dos.close();
            if(s != null) {
                s.close();
                s = null;
            }
        }
        
        public void send(String str) {
            try {
                dos.writeUTF(str);
                dos.flush();
            } catch (IOException e) {
                receivePlayerMessage(this,this.name,"QUIT \t"+this.name);
                clients.remove(this);
                System.out.println("client " + this.name + " is quit.");
            }
        }
        
        public void run() {
            try {
                while(bConnected) {
                    String str = dis.readUTF();
                    receivePlayerMessage(this,this.name,str);
                }    
            } catch (EOFException e) {
                System.out.println("client " + this.name + " closed!");
                receivePlayerMessage(this,this.name,"QUIT \t"+this.name);
                clients.remove(this);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                	close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    
    private class CmdThread implements Runnable {
        public void run() {
        	System.out.println("If you want to stop server, pls keyin 'stop'.");
            while (started) {
                try {
                    if (in.hasNext()) {
                        String talk = in.nextLine();
                        if (talk.equals("stop")) {
                        	System.out.println("Server Stopping...");
                            started = false;
                            stop();
                            break;
                        }
                        else if (talk.equals("send")) {
                        	System.out.println("Server says: Hello everyone...");
                        	for(Client x: clients) {
                        		x.send("Hello Everyone.");
                        	}
                        }
                        else if (talk.equals("list")) {
                        	service.scoreList();
                        }
                        else if (talk.equals("restart")) {
                        	System.out.println("Restart game......");
                        	service.restartGame();
                        	if (service.playersReady()) {
                    			if (service.hasNextGame()) {
                        			service.newGame();
                        			sendMessage("READY");
                    			}
                    		}
                        }
                        else if (talk.equals("reset")) {
                        	System.out.println("Reset all clients...");
                        	for(Client x: clients) {
                        		x.close();
                        	}
                        }
                        else
                        	System.out.println("unexcept command.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}
 
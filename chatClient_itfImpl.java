import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class chatClient_itfImpl extends UnicastRemoteObject implements chatClient_itf, Runnable {

	private static final long serialVersionUID = 1L;
	private chatServer_itf server_itf;
	private String clientName = null;
    private boolean active;
    Timer checkServer;
    TimerTask checkServerTask;
    
    public void startServerChecker() {
        checkServerTask = new handlerServerChecker(this.server_itf);
        checkServer = new Timer(true);
        checkServer.scheduleAtFixedRate(checkServerTask, 0, 2000);
    }
    
    public void initLogOutHandler() {
        handlerClientLogOut handlerLogOut = new handlerClientLogOut(this.server_itf, this.clientName);
        Runtime.getRuntime().addShutdownHook(new Thread(handlerLogOut));
    }

    public void restoreChat() throws RemoteException {
        for(String message : this.server_itf.restoreChatHistory()){
            System.out.println(message);
        }
    }

	protected chatClient_itfImpl(String name, chatServer_itf server_interface) throws RemoteException {
        this.clientName = name;
		this.server_itf = server_interface;
        if (this.server_itf.registerClient(name, this)){
            restoreChat();
            this.server_itf.broadcast(name + " has joined the chat room...!!!");
            System.out.println("Logged in as : " + this.clientName);
            active = true;
        } else {
            System.out.println("Clientname already exist, please input another name!");
            System.exit(1);
        }
        startServerChecker();
        initLogOutHandler();
	}

	@Override
	public void getMessage(String message) throws RemoteException {
        System.out.println(message);
	}

	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		String message;
		while (true) {
			message = scanner.nextLine();
			try {
                if(this.active==true){
                    if(message.equals("Exit")) {
                        active = false;
                        server_itf.exitClient(this.clientName);
                    } else {
                        server_itf.broadcast(clientName +" : " + message);
                    }
                }
                else if (active==false && message.equals("Join")){
                    active = true;
                    server_itf.rejoinClient(this.clientName, this);
                }
			} catch (RemoteException e) {
				System.out.println("Network Error");
			}
		}
	}
}

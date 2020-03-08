import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

public class chatClient_itfImpl implements chatClient_itf {

	private chatServer_itf server_itf;
	private String clientName = null;
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

	public chatClient_itfImpl(String name, chatServer_itf server_interface) throws RemoteException {
        this.clientName = name;
        this.server_itf = server_interface;
	}



	@Override
	public void getMessage(String message) throws RemoteException {
        System.out.println(message);
    }
    
    @Override
	public void sendMessage(String message) throws RemoteException {
        try {
            boolean checkActive = this.server_itf.checkClientActive(this.clientName);
            if(message.equals("Exit")) {
                server_itf.exitClient(this.clientName);
            } else if (checkActive==false && message.equals("Join")){
                server_itf.rejoinClient(this.clientName, this);
            } else if (checkActive) {
                server_itf.broadcast(clientName +" : " + message);
            }
        } catch (RemoteException e) {
            System.out.println("Network Error");
        }
    }
    
    @Override
	public void register() throws RemoteException {
        if (!this.server_itf.registerClient(this.clientName, this)) 
        { System.exit(1); } 
        startServerChecker();
        initLogOutHandler();
    }
}

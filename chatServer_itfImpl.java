import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

public class chatServer_itfImpl implements chatServer_itf {

	private ArrayList<String> clientNames;
	HashMap<String, chatClient_itf> clientmaps;
	private ArrayList<String> chatHistory;
	TimerTask handlerScheduleHistory;
	Timer timerHandlerScheduleHistory;

	private void initServerShutdownHandler() {
		handlerServerShutdown handlerServer = new handlerServerShutdown(this.chatHistory);
		Runtime.getRuntime().addShutdownHook(new Thread(handlerServer));
	}

	public void startHistorySaver() {
        handlerScheduleHistory = new handlerScheduleHistory(this.chatHistory);
        timerHandlerScheduleHistory = new Timer(true);
        timerHandlerScheduleHistory.scheduleAtFixedRate(handlerScheduleHistory, 0, 20000);
    }

	private void restoreFromFile() {
		try (Stream<String> stream = Files.lines(Paths.get("history.txt"))) {
			stream.forEach(chatHistory::add);
		} catch (Exception e) {}
	}

	private void restoreToClient(chatClient_itf client) throws RemoteException {
		for(String value : chatHistory){
			client.getMessage(value);
		}
	}

	protected chatServer_itfImpl() throws RemoteException, FileNotFoundException, UnsupportedEncodingException {
        clientmaps = new HashMap<>();
		clientNames = new ArrayList<String>();
		chatHistory = new ArrayList<String>();
		initServerShutdownHandler();
		restoreFromFile();
		startHistorySaver();
	}

	@Override
	public boolean registerClient(String name, chatClient_itf client) throws RemoteException {
        if(!clientNames.contains(name)) {
            this.clientmaps.put(name, client);
			this.clientNames.add(name);
			restoreToClient(client);
			broadcast(name + " has joined the chat room...!!!");
			client.getMessage("Logged in as : " + name);
			System.out.println(name + " joined");
            return true;
		}
		else {
			client.getMessage("Clientname already exist, please input another name!");
            return false;
        }
	}
	
	@Override
	public boolean checkClientActive(String name) throws RemoteException { 
		if (clientmaps.get(name)!=null){
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void exitClientLogOut(String name) throws RemoteException {
		this.clientNames.remove(name);
		this.clientmaps.remove(name);
		this.broadcast(name +" logged out from the chat room...!!!");
		System.out.println(name + " logged out");
	}

    @Override
	public void exitClient(String name) throws RemoteException {
		this.clientmaps.remove(name);
		broadcast(name +" left the chatroom...!!!");
		System.out.println(name + " exited");
	}

	@Override
	public void rejoinClient(String name, chatClient_itf client) throws RemoteException {
		this.clientmaps.put(name, client);
		broadcast(name +" has re-joined the chatroom...!!!");
		System.out.println(name + " rejoined");
	}

	@Override
	public void broadcast(String message) throws RemoteException {
		chatHistory.add(message);
		for (chatClient_itf value : clientmaps.values()) {
			value.getMessage(message);
		}
	}

	@Override
	public boolean checkAlive() throws RemoteException {
		return true;
	}

	@Override
	public void sendPM(String from, String message) throws RemoteException {
		String arr[] = message.split(" ", 2);
		if(this.clientmaps.get(arr[0])!=null){
			this.clientmaps.get(arr[0]).getMessage("PM from "+from+" : "+arr[1]);
		}
		else{
			this.clientmaps.get(from).getMessage("Client not found...!!!");
		}
	}
}

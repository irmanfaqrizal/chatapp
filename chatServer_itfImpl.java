import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class chatServer_itfImpl extends UnicastRemoteObject implements chatServer_itf {

	private static final long serialVersionUID = 1L;
	private ArrayList<String> clientNames;
	HashMap<String, chatClient_itf> clientmaps;
	private ArrayList<String> chatHistory;

	private void initServerShutdownHandler() {
		handlerServerShutdown handlerServer = new handlerServerShutdown(this.chatHistory);
		Runtime.getRuntime().addShutdownHook(new Thread(handlerServer));
	}

	private void restoreFromFile() {
		try (Stream<String> stream = Files.lines(Paths.get("history.txt"))) {
			stream.forEach(chatHistory::add);
		} catch (Exception e) {}
	}

	protected chatServer_itfImpl() throws RemoteException, FileNotFoundException, UnsupportedEncodingException {
        clientmaps = new HashMap<>();
		clientNames = new ArrayList<String>();
		chatHistory = new ArrayList<String>();
		initServerShutdownHandler();
		restoreFromFile();
	}

	@Override
	public boolean registerClient(String name, chatClient_itf client) throws RemoteException {
        if(!clientNames.contains(name)) {
            this.clientmaps.put(name, client);
			this.clientNames.add(name);
			System.out.println(name + " joined");
            return true;
		}
		else {
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
	public List <String> restoreChatHistory() throws RemoteException {
		return chatHistory;
	}
}

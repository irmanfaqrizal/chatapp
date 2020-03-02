import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class chatServer_itfImpl extends UnicastRemoteObject implements chatServer_itf {

	private static final long serialVersionUID = 1L;
	private ArrayList<chatClient_itf> clients;

	protected chatServer_itfImpl() throws RemoteException {
		clients = new ArrayList<chatClient_itf>();
	}

	@Override
	public synchronized void registerClient(chatClient_itf client) throws RemoteException {
		this.clients.add(client);
	}

	@Override
	public synchronized void broadcast(String message) throws RemoteException {
		for(int i = 0; i < clients.size(); i++) {
			clients.get(i).getMessage(message);
		}
	}
}

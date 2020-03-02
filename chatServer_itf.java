import java.rmi.Remote;
import java.rmi.RemoteException;

public interface chatServer_itf extends Remote{
	void registerClient(chatClient_itf client) throws RemoteException;
	void broadcast(String message) throws RemoteException;
}

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface chatClient_itf extends Remote{
	void getMessage(String message) throws RemoteException;
	void sendMessage(String message) throws RemoteException;
	void register() throws RemoteException;
}

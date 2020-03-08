import java.rmi.Remote;
import java.rmi.RemoteException;

public interface chatServer_itf extends Remote{
    boolean checkAlive() throws RemoteException;
    boolean registerClient(String name, chatClient_itf client) throws RemoteException;
    boolean checkClientActive(String name) throws RemoteException;
    void exitClientLogOut(String name) throws RemoteException;
    void exitClient(String name) throws RemoteException;
    void rejoinClient(String name, chatClient_itf client) throws RemoteException;
    void broadcast(String message) throws RemoteException;
    void sendPM(String from, String message) throws RemoteException;
}

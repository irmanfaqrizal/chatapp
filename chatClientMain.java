import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class chatClientMain {
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
        
        if (args.length < 1) {
            System.out.println("Usage: java chatClientMain <client name>");
            return;
        }
        try {
            Registry registry = LocateRegistry.getRegistry("localhost"); 
            chatServer_itf server_itf = (chatServer_itf) registry.lookup("Server");
            new Thread(new chatClient_itfImpl(args[0], server_itf)).start();
        } catch (Exception e) {
            System.out.println("Connection Failed...!!!");
            System.exit(0);
        }
	}
}

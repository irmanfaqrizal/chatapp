import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class chatServerMain {
	public static void main(String[] args) throws RemoteException, MalformedURLException {
		Registry registry= LocateRegistry.getRegistry(); 
        registry.rebind("Server", new chatServer_itfImpl());
        System.out.println("Server Ready");
	}
}
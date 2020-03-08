import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class chatServerMain {
	public static void main(String[] args)
			throws RemoteException, MalformedURLException, FileNotFoundException, UnsupportedEncodingException {
		try {
			Registry registry= LocateRegistry.getRegistry(); 
			registry.rebind("Server", new chatServer_itfImpl());
			System.out.println("Server Ready");
		} catch (Exception e) {
			System.out.println("Failed to run the server...!!!");
			System.exit(0);
		}
	}
}

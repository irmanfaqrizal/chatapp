import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class chatServerMain {
	public static void main(String[] args)
			throws RemoteException, MalformedURLException, FileNotFoundException, UnsupportedEncodingException {
		try {
			Registry registry= LocateRegistry.getRegistry();
			chatServer_itfImpl cs = new chatServer_itfImpl();
			chatServer_itf cs_stub = (chatServer_itf) UnicastRemoteObject.exportObject(cs, 0);
			registry.rebind("Server", cs_stub);
			System.out.println("Server Ready");
		} catch (Exception e) {
			System.out.println("Failed to run the server...!!!");
			System.exit(0);
		}
	}
}

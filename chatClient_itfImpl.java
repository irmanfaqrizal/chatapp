import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class chatClient_itfImpl extends UnicastRemoteObject implements chatClient_itf, Runnable {

	private static final long serialVersionUID = 1L;
	private chatServer_itf server_itf;
	private String clientName = null;
	
	protected chatClient_itfImpl(String name, chatServer_itf server_interface) throws RemoteException {
		this.clientName = name;
		this.server_itf = server_interface;
		server_interface.registerClient(this);
	}

	@Override
	public void getMessage(String message) throws RemoteException {
		System.out.println(message);	
	}

	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		String message;
		while (true) {
			message = scanner.nextLine();
			try {
				server_itf.broadcast(clientName +":" + message);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
}

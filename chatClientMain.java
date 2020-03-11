import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;


public class chatClientMain {
    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {

        if (args.length < 1) {
            System.out.println("Usage: java chatClientMain <client name> <location>");
            return;
        }
        try {
            // get server interface
            int port = 8080;
            if(args.length > 1 && args[1].matches("-?\\d+") && Integer.parseInt(args[1]) > 1024) {
                port = Integer.parseInt(args[1]);
	        }   
            Registry registry = LocateRegistry.getRegistry(port);
            chatServer_itf server_itf = (chatServer_itf) registry.lookup("Server");

            // client interface and call register() to register the client
            chatClient_itfImpl cc = new chatClient_itfImpl(args[0], server_itf);
            chatClient_itf c_stub = (chatClient_itf) UnicastRemoteObject.exportObject(cc, 0);
            c_stub.register();

            // run scanner to handle client inputs
            Scanner scanner = new Scanner(System.in);
            String message;
            while (true) {
                message = scanner.nextLine();
                c_stub.sendMessage(message);
            }
        } catch (Exception e) {
            System.out.println("Failed to Connect : " + e);
            System.exit(0);
        }
    }
}

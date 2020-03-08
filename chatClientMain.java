import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;


public class chatClientMain {
    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {

        if (args.length < 2) {
            System.out.println("Usage: java chatClientMain <location> <client name>");
            return;
        }
        try {
            // get server interface
            Registry registry = LocateRegistry.getRegistry(args[0]);
            chatServer_itf server_itf = (chatServer_itf) registry.lookup("Server");

            // client interface and call register() to register the client
            chatClient_itfImpl cc = new chatClient_itfImpl(args[1], server_itf);
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
            System.out.println(e);
            System.exit(0);
        }
    }
}

import java.rmi.RemoteException;
import java.util.TimerTask;

public class handlerServerChecker extends TimerTask {

    private chatServer_itf server;

    public handlerServerChecker (chatServer_itf server_itf) {
        this.server = server_itf;
    }

    @Override
    public void run() {
        try {
            this.server.checkAlive();
        } catch (RemoteException e) {
            System.out.println("Server is Down...!!!");
            System.exit(1);
        }
    }
}
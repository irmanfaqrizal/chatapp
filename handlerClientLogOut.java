import java.rmi.RemoteException;

public class handlerClientLogOut implements Runnable {

    private chatServer_itf server_itf;
    private String clientName;

    public handlerClientLogOut(chatServer_itf server, String name) {
        server_itf = server;
        this.clientName = name;
    }

    @Override
    public void run() {
        try {
            this.server_itf.exitClientLogOut(this.clientName);
        } catch (RemoteException e) {}
    }
}

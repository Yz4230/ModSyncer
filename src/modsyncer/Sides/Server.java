package modsyncer.Sides;

import javafx.scene.control.ProgressBar;
import modsyncer.threads.ServerThread;

public class Server implements Side {
    private int port;
    private ServerThread thread;

    @Override
    public void sync(ProgressBar progressBar) {

    }

    @Override
    public void parseAddress(String parseTarget) {
        port = Integer.parseInt(parseTarget);
    }
}

package modsyncer.Sides;

import javafx.scene.control.ProgressBar;
import modsyncer.threads.ClientThread;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client implements Side {
    private int port;
    private String serverIp;
    private ClientThread thread;

    @Override
    public void sync(ProgressBar progressBar) {
    }

    @Override
    public void parseAddress(String parseTarget) {
        Pattern pattern = Pattern.compile("((?:\\d{1,3})\\.(?:\\d{1,3})\\.(?:\\d{1,3})\\.(?:\\d{1,3})):(\\d{1,5})");
        Matcher matcher = pattern.matcher(parseTarget);
        if (matcher.find()) {
            serverIp = matcher.group(1);
            port = Integer.parseInt(matcher.group(2));
        }
    }
}

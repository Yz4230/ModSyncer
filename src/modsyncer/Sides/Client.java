package modsyncer.Sides;

import modsyncer.threads.client.ClientThread;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client implements Side {
    private int port;
    private String serverIp;
    private ClientThread thread;

    @Override
    public void sync() {
        this.thread = new ClientThread(this.serverIp, this.port);
        this.thread.start();
    }

    @Override
    public void terminate() {
    }

    @Override
    public void parseAddress(String parseTarget) {
        Pattern pattern = Pattern.compile("((?:(?:\\d{1,3})\\.?){4}):(\\d{1,5})");
        Matcher matcher = pattern.matcher(parseTarget);
        if (matcher.find()) {
            this.serverIp = matcher.group(1);
            this.port = Integer.parseInt(matcher.group(2));
        }
    }
}

package modsyncer.Sides;

import modsyncer.threads.server.ServerThread;

public class Server implements Side {
    private int port;
    private ServerThread thread;
    private boolean running = false;

    @Override
    public void sync() {
        this.running = true;
        this.thread = new ServerThread(this.port);
        this.thread.start();
    }

    @Override
    public void terminate() {
        this.running = false;
        this.thread.interrupt();
    }

    @Override
    public void parseAddress(String parseTarget) {
        this.port = Integer.parseInt(parseTarget);
    }

    public boolean isRunning() {
        return this.running;
    }
}

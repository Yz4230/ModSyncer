package modsyncer.Sides;

interface Side {
    void parseAddress(String parseTarget);
    void sync();
    void terminate();
}

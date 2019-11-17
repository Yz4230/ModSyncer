package modsyncer.Sides;

import javafx.scene.control.ProgressBar;

interface Side {
    void parseAddress(String parseTarget);
    void sync(ProgressBar progressBar);
}

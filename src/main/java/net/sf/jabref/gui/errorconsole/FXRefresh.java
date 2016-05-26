package net.sf.jabref.gui.errorconsole;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class FXRefresh {

    private Timeline timeline;


    public void startrefreshErrorGUI() {
        timeline = new Timeline(new KeyFrame(
                Duration.millis(2500),
                ae -> System.out.println("TEST")));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void stoprefreshErrorGUI() {
        timeline.stop();
    }
}

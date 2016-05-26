package net.sf.jabref.gui.errorconsole;

import net.sf.jabref.Globals;
import net.sf.jabref.logic.l10n.Localization;
import net.sf.jabref.logic.logging.GuiAppender;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ErrorConsoleViewModel {

    private Timeline timeline;
    private final ReadOnlyStringWrapper logTabTextArea = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper exceptionTabTextArea = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper outputTabTextArea = new ReadOnlyStringWrapper();

    // public FXRefresh refreshErrorGUI;

    @FXML
    private Button closeButton;

    @FXML
    private void initialize() {

        logTabTextArea.set(Globals.streamEavesdropper.getOutput());
        exceptionTabTextArea.set(Globals.streamEavesdropper.getErrorMessages());
        outputTabTextArea.set(GuiAppender.CACHE.get());

        if (getExceptionTabTextArea().isEmpty()) {
            exceptionTabTextArea.set(
                    Localization.lang("No exceptions have occurred."));
        }

        //refreshErrorGUI.startrefreshErrorGUI();

        //refreshGUI();

        timeline = new Timeline(new KeyFrame(
                Duration.millis(2500),
                ae -> refreshGUI()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    public ReadOnlyStringProperty logTabTextAreaProperty() {
        return logTabTextArea;
    }

    public String getExceptionTabTextArea() {
        return exceptionTabTextArea.get();
    }

    public String getLogTabTextArea() {
        return logTabTextArea.get();
    }

    public ReadOnlyStringProperty exceptionTabTextAreaProperty() {
        return exceptionTabTextArea;
    }


    public ReadOnlyStringProperty outputTabTextAreaProperty() {
        return outputTabTextArea;
    }

    public String getOutputTabTextArea() {
        return outputTabTextArea.get();
    }

    public void refreshGUI() {
        /*Thread t = new Thread(() -> {
            while (true) {
                Platform.runLater(() -> System.out.println("TEST"));
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException ex) {
                    break;
                }
            }
        });

        t.setName("Runnable Time Updater");
        t.setDaemon(true);
        t.start();*/
        System.out.println(true);
       /* logTabTextArea.set(Globals.streamEavesdropper.getOutput());
        exceptionTabTextArea.set(Globals.streamEavesdropper.getErrorMessages());
        outputTabTextArea.set(GuiAppender.CACHE.get());

        if (getExceptionTabTextArea().isEmpty()) {
            exceptionTabTextArea.set(
                    Localization.lang("No exceptions have occurred."));
        }*/
    }

    @FXML
    private void closeErrorDialog() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        timeline.stop();
        //refreshErrorGUI.stoprefreshErrorGUI();
        stage.close();
    }

}

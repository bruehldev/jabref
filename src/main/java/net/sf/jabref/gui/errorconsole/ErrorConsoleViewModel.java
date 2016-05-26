package net.sf.jabref.gui.errorconsole;

import java.util.Random;

import net.sf.jabref.Globals;
import net.sf.jabref.logic.l10n.Localization;
import net.sf.jabref.logic.logging.GuiAppender;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ErrorConsoleViewModel {

    //maybe make it editable as a preference?
    private static final long REFRESH_RATE = 1000;

    private Thread refreshThread = new Thread(() -> {
        try {
            while (true) {
                refreshGUI();
                Thread.sleep(REFRESH_RATE);
            }
        } catch (InterruptedException e) {}
    });

    private final ReadOnlyStringWrapper logTabTextArea = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper exceptionTabTextArea = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper outputTabTextArea = new ReadOnlyStringWrapper();

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
        refreshThread.start();
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
        System.out.println(new Random().nextInt());  //TODO delete test dummy

        logTabTextArea.set(Globals.streamEavesdropper.getOutput());
        exceptionTabTextArea.set(Globals.streamEavesdropper.getErrorMessages());
        outputTabTextArea.set(GuiAppender.CACHE.get());

        if (getExceptionTabTextArea().isEmpty()) {
            exceptionTabTextArea.set(
                    Localization.lang("No exceptions have occurred."));
        }
    }

    @FXML
    private void closeErrorDialog() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        refreshThread.interrupt();
        stage.close();
    }

}

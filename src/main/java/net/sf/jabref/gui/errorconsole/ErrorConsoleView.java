package net.sf.jabref.gui.errorconsole;

import net.sf.jabref.gui.FXAlert;
import net.sf.jabref.logic.l10n.Localization;

import com.airhacks.afterburner.views.FXMLView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;

public class ErrorConsoleView extends FXMLView {

    public ErrorConsoleView() {
        super();
        bundle = Localization.getMessages();
    }

    public void show() {
        FXAlert errorConsole = new FXAlert(AlertType.ERROR, Localization.lang("Program output"));

        // create extra Error-Icon for Dialogpane in JavaFX
        Label img = new Label();
        img.getStyleClass().addAll("alert", "error", "dialog-pane");
        img.setScaleX(1.5);
        img.setScaleY(1.5);
        DialogPane pane = (DialogPane) this.getView();
        pane.setGraphic(img);
        errorConsole.setDialogPane(pane);
        errorConsole.setResizable(true);

        errorConsole.show();
    }
}

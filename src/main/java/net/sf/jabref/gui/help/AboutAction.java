package net.sf.jabref.gui.help;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.Icon;

import net.sf.jabref.gui.actions.MnemonicAwareAction;
import javafx.application.Platform;

public class AboutAction extends MnemonicAwareAction {

    public AboutAction(String title, String tooltip, Icon iconFile) {
        super(iconFile);
        putValue(Action.NAME, title);
        putValue(Action.SHORT_DESCRIPTION, tooltip);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Platform.runLater(() -> new AboutDialogView().show());
    }
}

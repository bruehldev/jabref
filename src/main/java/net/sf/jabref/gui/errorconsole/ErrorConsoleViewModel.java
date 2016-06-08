/*  Copyright (C) 2016 JabRef contributors.
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/
package net.sf.jabref.gui.errorconsole;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefGUI;
import net.sf.jabref.gui.ClipBoardManager;
import net.sf.jabref.logic.l10n.Localization;
import net.sf.jabref.logic.logging.ObservableCache;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class ErrorConsoleViewModel {

    private final ListProperty<String> logTabTextArea = new SimpleListProperty<>();
    private final ListProperty<String> exceptionTabTextArea = new SimpleListProperty<>();
    private final ListProperty<String> outputTabTextArea = new SimpleListProperty<>();

    //private final ListProperty<String> allMessage = new SimpleListProperty<>(FXCollections.observableArrayList());

    @FXML
    private Button closeButton;
    @FXML
    private Button copyLogButton;
    @FXML
    private ListView<String> output;

    @FXML
    private void initialize() {
        logTabTextArea.set(ObservableCache.INSTANCE.getCacheContent());
        exceptionTabTextArea.set(Globals.streamEavesdropper.getErrStream().getStreamContent());
        outputTabTextArea.set(Globals.streamEavesdropper.getOutStream().getStreamContent());

      /*  ObservableCache.INSTANCE.getCacheContent().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                allMessage.addAll(c.getAddedSubList());
            }
        });

        Globals.streamEavesdropper.getErrStream().getStreamContent().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                allMessage.addAll(c.getAddedSubList());
            }
        });

        Globals.streamEavesdropper.getOutStream().getStreamContent().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                allMessage.addAll(c.getAddedSubList());
            }
        });*/

        // handler for listCell appearance (example for exception Cell)
        /*output.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> listView) {
                return new ListCell<String>() {
                    @Override
                    public void updateItem(String string, boolean empty) {
                        super.updateItem(string, empty);
                        if (string != null) {
                            setText(string);
                            if (string.startsWith("\t") || string.startsWith("java.lang.")) {
                                getStyleClass().add("exception");
                            }
                        }
                    }
                };
            }
        });*/
    }

    public ListProperty<String> logTabTextAreaProperty() {
        return logTabTextArea;
    }

    public ObservableList<String> getLogTabTextArea() {
        return logTabTextArea.get();
    }

    public ListProperty<String> exceptionTabTextAreaProperty() {
        return exceptionTabTextArea;
    }

    public ObservableList<String> getExceptionTabTextArea() {
        return exceptionTabTextArea.get();
    }

    public ListProperty<String> outputTabTextAreaProperty() {
        return outputTabTextArea;
    }

    public ObservableList<String> getOutputTabTextArea() {
        return outputTabTextArea.get();
    }

/*    public ListProperty<String> allMessageProperty() {
        return allMessage;
    }

    public ObservableList<String> getAllMessage() {
        return allMessage.get();
    }*/

    @FXML
    private void copyLogErrorDialog() {
        String logContentCopy = "";
        for (String message : logTabTextArea.get()) {
            logContentCopy += message + System.lineSeparator();
        }
        new ClipBoardManager().setClipboardContents(logContentCopy);

        JabRefGUI.getMainFrame().output(Localization.lang("Log is copied"));
        // GuiAppender.CACHE.add("Test CACHE");
    }

    @FXML
    private void closeErrorDialog() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        //refreshThread.interrupt();
        stage.close();
    }

}

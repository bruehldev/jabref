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

import java.io.IOException;

import net.sf.jabref.JabRefGUI;
import net.sf.jabref.gui.ClipBoardManager;
import net.sf.jabref.logic.l10n.Localization;
import net.sf.jabref.logic.logging.GuiAppender;
import net.sf.jabref.logic.logging.ObservableMessages;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ErrorConsoleViewModel {

    @FXML
    private Button closeButton;
    @FXML
    private Button copyLogButton;
    @FXML
    private Button testOutput;
    @FXML
    private ToggleButton developerButton;
    @FXML
    private ListView<String> allMessage;

    private BooleanProperty developerInformation = new SimpleBooleanProperty();

    @FXML
    private void initialize() {

        ButtonBar.setButtonData(developerButton, ButtonBar.ButtonData.LEFT);
        allMessage.itemsProperty().bind(ObservableMessages.INSTANCE.messagesPropety());
        developerInformation.bind(developerButton.selectedProperty());
        developerInformation.addListener((observable, oldValue, newValue) -> allMessage.refresh());



        // handler for listCell appearance (example for exception Cell)
        allMessage.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> listView) {
                return new ListCell<String>() {
                    @Override
                    public void updateItem(String string, boolean empty) {
                        super.updateItem(string, empty);
                        if (string != null) {
                            setText(string);
                            if (string.startsWith("\t") || string.startsWith("java.")) {
                                getStyleClass().clear();
                                if (developerInformation.getValue()) {
                                    getStyleClass().add("exception-active");
                                } else {
                                    getStyleClass().add("exception-inactive");
                                }
                            } else if (string.substring(0, 2).matches("[0-9][0-9]")) {
                                getStyleClass().clear();
                                getStyleClass().add("log");
                            } else {
                                getStyleClass().clear();
                                getStyleClass().add("output");
                            }
                        }
                        allMessage.refresh();
                    }
                };
            }
        });
    }

    @FXML
    private void testOutputOnClicked() {
        GuiAppender.CACHE.add("Test CACHE");
        System.out.println("TESTING");
        try {
            throw new IOException();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void copyLogErrorDialog() {
        String logContentCopy = "";
        for (String message : GuiAppender.CACHE.get()) {
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

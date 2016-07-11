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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import javafx.util.Callback;

import net.sf.jabref.JabRefGUI;
import net.sf.jabref.gui.ClipBoardManager;
import net.sf.jabref.logic.error.MessagePriority;
import net.sf.jabref.logic.error.ObservableMessageWithPriority;
import net.sf.jabref.logic.l10n.Localization;
import net.sf.jabref.logic.logging.GuiAppender;
import net.sf.jabref.logic.logging.ObservableMessages;

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
    private ListView<ObservableMessageWithPriority> allMessage;

    private BooleanProperty developerInformation = new SimpleBooleanProperty();

    @FXML
    private void initialize() {

        ButtonBar.setButtonData(developerButton, ButtonBar.ButtonData.LEFT);
        ObservableList<ObservableMessageWithPriority> masterData = ObservableMessages.INSTANCE.messagesPropety();

        /*
        That is a ObservableList, which filter the showed message.
        At the begin it should show only the Log Entries
         */
        FilteredList<ObservableMessageWithPriority> filteredList = new FilteredList<>(masterData, t -> !t.isFilteredProperty().get());
        allMessage.setItems(filteredList);

        /* binding the property of the developer button
        if developer button disable, then it should show only entries of log (what in the begin happen)
        if developer button enable, then it should show all entries
        */
        developerInformation.bind(developerButton.selectedProperty());
        developerInformation.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                masterData.forEach(message -> message.setIsFiltered(false));

            } else {
                masterData.forEach(message -> message.setIsFiltered(message.getPriority() != MessagePriority.LOW));
            }
        });

        // handler for listCell appearance (example for exception Cell)
        allMessage.setCellFactory(new Callback<ListView<ObservableMessageWithPriority>, ListCell<ObservableMessageWithPriority>>() {
            @Override
            public ListCell<ObservableMessageWithPriority> call(ListView<ObservableMessageWithPriority> listView) {
                return new ListCell<ObservableMessageWithPriority>() {
                    @Override
                    public void updateItem(ObservableMessageWithPriority omp, boolean empty) {
                        super.updateItem(omp, empty);
                        if (omp != null) {
                            setText(omp.getMessage());
                            getStyleClass().clear();
                            if (omp.getPriority() == MessagePriority.HIGH) {
                                if (developerInformation.getValue()) {
                                    getStyleClass().add("exception");
                                }
                            } else if (omp.getPriority() == MessagePriority.MEDIUM) {
                                if (developerInformation.getValue()) {
                                    getStyleClass().add("output");
                                }
                            } else {
                                getStyleClass().add("log");
                            }
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });
    }

    //  TEST BUTTON TO TEST THE ENTRIES
    @FXML
    private void testOutputOnClicked() {
        GuiAppender.CACHE.add("Test CACHE");
        System.out.println("TESTING");
        try {
            throw new IOException();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //allMessage.refresh();

    }

    // handler for copy of Log Entry in the List by click of Copy Log Button
    @FXML
    private void copyLogErrorDialog() {
        ObservableList<ObservableMessageWithPriority> masterData = ObservableMessages.INSTANCE.messagesPropety();
        masterData.forEach(message -> message.setIsFiltered(message.getPriority() != MessagePriority.LOW));
        FilteredList<ObservableMessageWithPriority> filteredLowPriorityList = new FilteredList<>(masterData, t -> !t.isFilteredProperty().get());
        String logContentCopy = "";

        for (ObservableMessageWithPriority message : filteredLowPriorityList) {
            logContentCopy += message.getMessage() + System.lineSeparator();
        }
        new ClipBoardManager().setClipboardContents(logContentCopy);
        JabRefGUI.getMainFrame().output(Localization.lang("Log is copied"));
    }

    // handler for close of error console
    @FXML
    private void closeErrorDialog() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

}

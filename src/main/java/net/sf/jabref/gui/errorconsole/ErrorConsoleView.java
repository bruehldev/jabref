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

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import net.sf.jabref.gui.FXAlert;
import net.sf.jabref.gui.FXDialogs;
import net.sf.jabref.logic.error.ObservableMessageWithPriority;
import net.sf.jabref.logic.l10n.Localization;

import com.airhacks.afterburner.views.FXMLView;

public class ErrorConsoleView extends FXMLView {

    private final ErrorConsoleViewModel errorViewModel = new ErrorConsoleViewModel();

    @FXML
    private Button closeButton;
    @FXML
    private Button copyLogButton;
    @FXML
    private Button createIssueButton;
    @FXML
    private ToggleButton developerButton;
    @FXML
    private ListView<ObservableMessageWithPriority> allMessage;

    public ErrorConsoleView() {
        super();
        bundle = Localization.getMessages();
    }

    public void show() {
        FXAlert errorConsole = new FXAlert(AlertType.ERROR, Localization.lang("Developer information"), false);

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

    @FXML
    private void initialize() {

        ButtonBar.setButtonData(developerButton, ButtonBar.ButtonData.LEFT);
        ButtonBar.setButtonData(createIssueButton, ButtonBar.ButtonData.LEFT);
        errorViewModel.setUpListView(allMessage, developerButton);
    }

    @FXML
    private void copyLogButton() {
        errorViewModel.copyLog();
    }

    @FXML
    private void createIssueButton() {

        GridPane content = createDialogContent("", "");

        DialogPane pane = new DialogPane();
        pane.setContent(content);
        pane.setMinWidth(500);
        pane.setMinHeight(300);


        Optional<ButtonType> result = FXDialogs.showCustomDialogAndWait("Create Issue", pane, ButtonType.OK, ButtonType.CANCEL);
        result.ifPresent((response -> {
            if (response == ButtonType.OK) {

                String titleTextfield = ((TextField) content.getChildren().get(0)).getText();
                String descriptionTextArea = ((TextArea) content.getChildren().get(1)).getText();

                errorViewModel.createIssue(titleTextfield, descriptionTextArea);
            }
        }));
    }

    // handler for close of error console
    @FXML
    private void closeErrorDialog() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private GridPane createDialogContent(String title, String descriptions) {
        TextArea descriptionField = new TextArea(descriptions);
        descriptionField.setPromptText("Error Description");

        TextField titleField = new TextField(title);
        titleField.setPromptText("Title");
        titleField.requestFocus();

        titleField.setMaxWidth(Double.MAX_VALUE);
        titleField.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(titleField, Priority.ALWAYS);
        GridPane.setHgrow(titleField, Priority.ALWAYS);

        descriptionField.setMaxWidth(Double.MAX_VALUE);
        descriptionField.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(descriptionField, Priority.ALWAYS);
        GridPane.setHgrow(descriptionField, Priority.ALWAYS);

        GridPane content = new GridPane();
        content.setVgap(5);
        content.setMaxWidth(Double.MAX_VALUE);
        content.add(titleField, 0, 0);
        content.add(descriptionField, 0, 1);
        return content;
    }
}

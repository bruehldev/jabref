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
import net.sf.jabref.logic.l10n.Localization;
import net.sf.jabref.logic.logging.GuiAppender;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ErrorConsoleViewModel {

    //maybe make it editable as a preference?
    private static final long REFRESH_RATE = 1000;

    private Thread refreshThread = new Thread(() -> {
        boolean injectedCloseButton = false;
        try {
            while (true) {
                Thread.sleep(REFRESH_RATE);

                // IMO this is a really, really bad way to do this, but a saw no other way since the stage is
                // initialized after the initialize()-Method and the closeErrorDialog() (more closely this thread)
                // cannot be easily accessed from outside this ViewModel
                // TODO find a better way
                if (!injectedCloseButton) {
                    Stage stage = (Stage) this.closeButton.getScene().getWindow();
                    stage.setOnCloseRequest(event -> closeErrorDialog());
                    injectedCloseButton = true;
                }
                /////////////////////////////////////////////

                //refreshGUI();
            }
        } catch (InterruptedException ignored) {
        }
    });

    private final ListProperty<String> logTabTextArea = new SimpleListProperty<>();
    private final ReadOnlyStringWrapper exceptionTabTextArea = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper outputTabTextArea = new ReadOnlyStringWrapper();
    ObservableList<String> observableList = Globals.streamEavesdropper.getOutStream().getStreamContent();

    @FXML
    private Button closeButton;

    @FXML
    private void initialize() {
        logTabTextArea.set(Globals.streamEavesdropper.getOutStream().getStreamContent());
        exceptionTabTextArea.set(Globals.streamEavesdropper.getErrorMessages());
        outputTabTextArea.set(GuiAppender.CACHE.get());

        if (getExceptionTabTextArea().isEmpty()) {
            exceptionTabTextArea.set(Localization.lang("No exceptions have occurred."));
        }


        //refreshThread.start();
        /*observableList.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                refreshGUI();
            }
        });
        observableList.add(Globals.streamEavesdropper.getOutput());
        observableList.add(Globals.streamEavesdropper.getErrorMessages());
        observableList.add(GuiAppender.CACHE.get());
        */

        //refreshGUI();

    }

    public ListProperty<String> logTabTextAreaProperty() {
        return logTabTextArea;
    }

    public String getExceptionTabTextArea() {
        return exceptionTabTextArea.get();
    }

    public ObservableList<String> getLogTabTextArea() {
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

    /*public void refreshGUI() {
        System.out.println(new Random().nextInt());  //TODO delete test dummy

        logTabTextArea.set(Globals.streamEavesdropper.getOutput());
        exceptionTabTextArea.set(Globals.streamEavesdropper.getErrorMessages());
        outputTabTextArea.set(GuiAppender.CACHE.get());

        if (getExceptionTabTextArea().isEmpty()) {
            exceptionTabTextArea.set(Localization.lang("No exceptions have occurred."));
        }
    }*/

    @FXML
    private void closeErrorDialog() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        //refreshThread.interrupt();
        stage.close();
    }

}

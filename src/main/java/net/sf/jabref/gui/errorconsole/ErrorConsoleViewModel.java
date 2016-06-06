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
import net.sf.jabref.logic.logging.GuiAppender;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ErrorConsoleViewModel {

    private final ListProperty<String> logTabTextArea = new SimpleListProperty<>();
    private final ListProperty<String> exceptionTabTextArea = new SimpleListProperty<>();
    private final ListProperty<String> outputTabTextArea = new SimpleListProperty<>();

    @FXML
    private Button closeButton;
    private Button testButton;

    @FXML
    private void initialize() {
        logTabTextArea.set(GuiAppender.CACHE.getCacheContent());
        exceptionTabTextArea.set(Globals.streamEavesdropper.getErrStream().getStreamContent());
        outputTabTextArea.set(Globals.streamEavesdropper.getOutStream().getStreamContent());
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

    @FXML
    private void testErrorDialog() {
        System.out.print("test");
        try {
            throw new Exception("error");
        } catch (Exception e) {
            e.printStackTrace();
        }
        GuiAppender.CACHE.add("TEST CACHE");
    }

    @FXML
    private void closeErrorDialog() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        //refreshThread.interrupt();
        stage.close();
    }

}

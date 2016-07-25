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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.util.Callback;

import net.sf.jabref.JabRefGUI;
import net.sf.jabref.gui.ClipBoardManager;
import net.sf.jabref.logic.error.MessagePriority;
import net.sf.jabref.logic.error.ObservableMessageWithPriority;
import net.sf.jabref.logic.l10n.Localization;
import net.sf.jabref.logic.logging.ObservableMessages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ErrorConsoleViewModel {

    private BooleanProperty developerInformation = new SimpleBooleanProperty();

    private final Log logger = LogFactory.getLog(ErrorConsoleViewModel.class);
    private final String firstPartIssueLink = "https://github.com/JabRef/jabref/issues/new";

    /**
     * Create allMessage ListView, which shows the filtered entries (default at first only the Log entries),
     * when the ToggleButton "developerButton" is disable.
     * If ToggleButton "developerButton" is enable, then it should show all entries
     * @param allMessage
     * @param developerButton (default is disable at start)
     */

    public void setUpListView(ListView allMessage, ToggleButton developerButton) {
        ObservableList<ObservableMessageWithPriority> masterData = ObservableMessages.INSTANCE.messagesPropety();

        FilteredList<ObservableMessageWithPriority> filteredList = new FilteredList<>(masterData, t -> !t.isFilteredProperty().get());
        allMessage.setItems(filteredList);

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

    // handler for copy of Log Entry in the List by click of Copy Log Button
    public void copyLog() {
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


    // handler for create Issues on GitHub by click of Create Issue Button
    public void createIssue(String title, String description) {
//        try {
//            String issueTitleTEST = ("&title=" + URLEncoder.encode(title, "UTF-8"));
//            byte[] postData = issueTitleTEST.getBytes(StandardCharsets.UTF_8);
//            int postDataLength = postData.length;
//            URL url = new URL(firstPartIssueLink);
//            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
//
//            con.setDoOutput( true );
//            con.setInstanceFollowRedirects( false );
//
//            con.setRequestMethod("GET");
//            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            con.setRequestProperty("charset", "utf-8");
//            con.setRequestProperty("Content-length", Integer.toString( postDataLength ));
//            con.setUseCaches(false);
//
//            DataOutputStream writer = new DataOutputStream(con.getOutputStream());
//            writer.write(postData);
//            writer.flush();
//            writer.close();
//
//
//            int responseCode = con.getResponseCode();
//            System.out.println("\nSending 'POST' request to URL : " + firstPartIssueLink);
//            System.out.println("Post parameters : " + postData.toString());
//            System.out.println("Response Code : " + responseCode);
//
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(con.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//
//            //print result
//            System.out.println(response.toString());
//
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        ObservableList<ObservableMessageWithPriority> masterData = ObservableMessages.INSTANCE.messagesPropety();

        String listViewContentCopy = "";
        for (ObservableMessageWithPriority message : masterData) {
            listViewContentCopy += message.getMessage() + "\n";
        }

        String issueDetails = ("<details>\n" + "<summary>" + "Details Info:" + "</summary>\n```\n" + listViewContentCopy + "\n```\n</details>");

        try {
            String issueTitle = ("?title=" + URLEncoder.encode(title, "UTF-8"));
            String issueDescription = ("&body=" + URLEncoder.encode(description, "UTF-8"));
            String issueDetailsEncoded = URLEncoder.encode(issueDetails, "UTF-8");
            String link = firstPartIssueLink + issueTitle + issueDescription + issueDetailsEncoded;

            java.awt.Desktop.getDesktop().browse(new URI(link));

//            JabRefDesktop.openBrowser(link);
//            JabRefDesktop.openBrowser(firstPartIssueLink + issueTitle + issueDescription);
        } catch (IOException e) {
            JabRefGUI.getMainFrame().output(Localization.lang("Error") + ": " + e.getLocalizedMessage());
            logger.debug("Could not open default browser.", e);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}

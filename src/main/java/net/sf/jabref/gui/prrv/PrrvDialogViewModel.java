package net.sf.jabref.gui.prrv;

import java.io.IOException;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefGUI;
import net.sf.jabref.gui.ClipBoardManager;
import net.sf.jabref.gui.desktop.JabRefDesktop;
import net.sf.jabref.gui.help.AboutDialogViewModel;
import net.sf.jabref.logic.l10n.Localization;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PrrvDialogViewModel {

    private final Log logger = LogFactory.getLog(AboutDialogViewModel.class);
    private final ReadOnlyStringWrapper website = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper websiteLibraries = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper websiteGithub = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper license = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper heading = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper years = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper authors = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper developers = new ReadOnlyStringWrapper();

    @FXML
    private Button closeButton;

    @FXML
    private void initialize() {

        heading.set("JabRef " + Globals.BUILD_INFO.getVersion());

        years.set(String.format("2003-%s", Globals.BUILD_INFO.getYear()));

        website.set("http://www.jabref.org");

        license.set("GNU General Public License v2 or later");

        developers.set(Globals.BUILD_INFO.getDevelopers());

        authors.set(Globals.BUILD_INFO.getAuthors());

        websiteLibraries.set("https://github.com/JabRef/jabref/blob/master/external-libraries.txt");

        websiteGithub.set("https://github.com/JabRef/jabref");
    }

    public ReadOnlyStringProperty websiteProperty() {
        return website.getReadOnlyProperty();
    }

    public String getWebsite() {
        return website.get();
    }

    public ReadOnlyStringProperty websiteLibrariesProperty() {
        return websiteLibraries.getReadOnlyProperty();
    }

    public String getWebsiteLibraries() {
        return websiteLibraries.get();
    }

    public ReadOnlyStringProperty websiteGithubProperty() {
        return websiteGithub.getReadOnlyProperty();
    }

    public String getWebsiteGithub() {
        return websiteGithub.get();
    }

    public ReadOnlyStringProperty licenseProperty() {
        return license.getReadOnlyProperty();
    }

    public String getLicense() {
        return license.get();
    }

    public ReadOnlyStringProperty authorsProperty() {
        return authors.getReadOnlyProperty();
    }

    public String getAuthors() {
        return authors.get();
    }

    public ReadOnlyStringProperty developersProperty() {
        return developers.getReadOnlyProperty();
    }

    public String getDevelopers() {
        return developers.get();
    }

    public ReadOnlyStringProperty headingProperty() {
        return heading.getReadOnlyProperty();
    }

    public String getHeading() {
        return heading.get();
    }

    public ReadOnlyStringProperty yearsProperty() {
        return years.getReadOnlyProperty();
    }

    public String getYears() {
        return years.get();
    }

    @FXML
    private void closeAboutDialog() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void copyVersionToClipboard() {
        new ClipBoardManager().setClipboardContents(Globals.BUILD_INFO.getVersion().getFullVersion());
        String message = String.format("%s - %s", Localization.lang("Copied version information to clipboard"), Globals.BUILD_INFO.getVersion());
        JabRefGUI.getMainFrame().output(message);
    }

    @FXML
    private void openJabrefWebsite() {
        try {
            JabRefDesktop.openBrowser(website.get());
        } catch (IOException e) {
            JabRefGUI.getMainFrame().output(Localization.lang("Error") + ": " + e.getLocalizedMessage());
            logger.debug("Could not open default browser.", e);
        }
    }

    @FXML
    private void openExternalLibrariesWebsite() {
        try {
            JabRefDesktop.openBrowser(websiteLibraries.get());
        } catch (IOException e) {
            JabRefGUI.getMainFrame().output(Localization.lang("Error") + ": " + e.getLocalizedMessage());
            logger.debug("Could not open default browser.", e);
        }
    }

    @FXML
    private void openGithub() {
        try {
            JabRefDesktop.openBrowser(websiteGithub.get());
        } catch (IOException e) {
            JabRefGUI.getMainFrame().output(Localization.lang("Error") + ": " + e.getLocalizedMessage());
            logger.debug("Could not open default browser.", e);
        }
    }

}

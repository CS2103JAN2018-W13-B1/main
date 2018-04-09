//@@author AJZ1995
package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.Person;
import seedu.address.ui.PersonCard;

/**
 *  Displays the contact details of a selected person
 */
public class PersonPanel extends UiPart<Region> {

    private static final String FXML = "PersonPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private VBox panel;
    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane groups;
    @FXML
    private FlowPane preferences;

    public PersonPanel() {
        super(FXML);
        registerAsAnEventHandler(this);
    }

    @Subscribe
    private void loadPersonPage(Person person) {
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().toString());
        address.setText(person.getAddress().toString());
        email.setText(person.getEmail().toString());
        person.getGroupTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(PersonCard.getGroupTagColorStyleFor(tag.tagName));
            groups.getChildren().add(tagLabel);
        });
        person.getPreferenceTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(PersonCard.getGroupTagColorStyleFor(tag.tagName));
            preferences.getChildren().add(tagLabel);
        });
    }

    @Subscribe
    public void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonPage(event.getNewSelection().person);
    }
}

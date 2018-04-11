package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.ReadOnlyCalendarManager;

/** Indicates the CalendarManager in the model has changed*/
//@@author SuxianAlicia
public class CalendarManagerChangedEvent extends BaseEvent {

    public final ReadOnlyCalendarManager data;

    public CalendarManagerChangedEvent(ReadOnlyCalendarManager data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of calendar entries " + data.getCalendarEntryList().size();
    }

}

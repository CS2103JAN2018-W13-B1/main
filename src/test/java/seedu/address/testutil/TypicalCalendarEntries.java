package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.CalendarManager;
import seedu.address.model.event.CalendarEntry;
import seedu.address.model.event.exceptions.DuplicateCalendarEntryException;

/**
 * A utility class containing a list of {@code CalendarEntry} objects to be used in tests.
 */
public class TypicalCalendarEntries {
    public static final CalendarEntry MEETING_BOSS = new CalendarEventBuilder()
            .withEventTitle("Meeting with boss")
            .withStartDate("06-06-2018")
            .withEndDate("06-06-2018")
            .withStartTime("10:00")
            .withEndTime("12:00").build();

    public static final CalendarEntry GET_STOCKS = new CalendarEventBuilder()
            .withEventTitle("Get stocks from supplier")
            .withStartDate("01-07-2018")
            .withEndDate("01-07-2018")
            .withStartTime("08:00")
            .withEndTime("12:30").build();

    public static final CalendarEntry ROAD_SHOW = new CalendarEventBuilder()
            .withEventTitle("Road Show at Orchard")
            .withStartDate("02-05-2018")
            .withEndDate("06-05-2018")
            .withStartTime("09:00")
            .withEndTime("19:00").build();

    public static final CalendarEntry WORKSHOP = new CalendarEventBuilder()
            .withEventTitle("Workshop")
            .withStartDate("28-05-2018")
            .withEndDate("29-05-2018")
            .withStartTime("10:00")
            .withEndTime("15:00").build();

    private TypicalCalendarEntries() {} // prevents instantiation


    public static CalendarManager getTypicalCalendarManagerWithEntries() {
        CalendarManager cm = new CalendarManager();

        for (CalendarEntry calEvent : getTypicalCalendarEvents()) {
            try {
                cm.addCalendarEntry(calEvent);
            } catch (DuplicateCalendarEntryException dcee) {
                throw new AssertionError("not possible");
            }
        }
        return cm;
    }

    public static List<CalendarEntry> getTypicalCalendarEvents() {
        return new ArrayList<>(Arrays.asList(MEETING_BOSS, GET_STOCKS, ROAD_SHOW, WORKSHOP));
    }
}
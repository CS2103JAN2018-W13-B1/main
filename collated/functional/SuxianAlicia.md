# SuxianAlicia
###### \java\seedu\address\commons\events\model\CalendarManagerChangedEvent.java
``` java
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
```
###### \java\seedu\address\commons\events\ui\CalendarEntryPanelSelectionChangedEvent.java
``` java
public class CalendarEntryPanelSelectionChangedEvent extends BaseEvent {

    private final CalendarEntryCard newSelection;

    public CalendarEntryPanelSelectionChangedEvent(CalendarEntryCard newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public CalendarEntryCard getNewSelection() {
        return newSelection;
    }
}
```
###### \java\seedu\address\commons\events\ui\DisplayCalendarEntryListEvent.java
``` java
public class DisplayCalendarEntryListEvent extends BaseEvent {
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \java\seedu\address\commons\events\ui\DisplayCalendarRequestEvent.java
``` java
public class DisplayCalendarRequestEvent extends BaseEvent {

    private final Optional<String> calendarView;

    public DisplayCalendarRequestEvent(Optional<String> calendarView) {
        if (calendarView.isPresent()) {
            this.calendarView = calendarView;
        } else {
            this.calendarView = null;
        }
    }

    public String getView() {
        return calendarView.isPresent() ? calendarView.get() : null;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \java\seedu\address\commons\events\ui\DisplayOrderListEvent.java
``` java
public class DisplayOrderListEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \java\seedu\address\commons\util\DateUtil.java
``` java
public class DateUtil {
    public static final String DATE_VALIDATION_REGEX = "\\d{2}-\\d{2}-\\d{4}"; // format
    public static final String DATE_VALIDATION_FORMAT = "dd-MM-yyyy"; // legal dates
    public static final String DATE_PATTERN = "dd-MM-yyyy";
    /**
     * Returns true if given string is a valid date.
     */
    public static boolean isValidDate(String test) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_VALIDATION_FORMAT);
        simpleDateFormat.setLenient(false);

        try {
            simpleDateFormat.parse(test);
        } catch (ParseException e) {
            return false;
        }

        return test.matches(DATE_VALIDATION_REGEX);
    }

    /**
     * Converts given string to a {@code LocalDate}.
     * @param date
     * @return
     */
    public static LocalDate convertStringToDate(String date) throws DateTimeParseException {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_PATTERN);
        LocalDate convertedDate = LocalDate.parse(date, format);

        return convertedDate;
    }

}
```
###### \java\seedu\address\commons\util\TimeUtil.java
``` java
public class TimeUtil {
    public static final String TIME_VALIDATION_REGEX = "\\d{2}:\\d{2}"; // format
    public static final String TIME_VALIDATION_FORMAT = "HH:mm"; // legal dates
    public static final String TIME_PATTERN = "HH:mm";
    /**
     * Returns true if given string is a valid time.
     */
    public static boolean isValidTime(String test) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_VALIDATION_FORMAT);
        simpleDateFormat.setLenient(false);

        try {
            simpleDateFormat.parse(test);
        } catch (ParseException e) {
            return false;
        }

        return test.matches(TIME_VALIDATION_REGEX);
    }

    /**
     * Converts given string to a {@code LocalTime}.
     */
    public static LocalTime convertStringToTime(String time) throws DateTimeParseException {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(TIME_PATTERN);
        LocalTime convertedTime = LocalTime.parse(time, format);

        return convertedTime;
    }
}
```
###### \java\seedu\address\logic\commands\AddEntryCommand.java
``` java
public class AddEntryCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "entryadd";
    public static final String COMMAND_ALIAS = "ea";
    public static final String COMMAND_SYNTAX = COMMAND_WORD + " "
            + PREFIX_ENTRY_TITLE + "ENTRY_TITLE "
            + PREFIX_START_DATE + "[START_DATE] "
            + PREFIX_END_DATE + "END_DATE "
            + PREFIX_START_TIME + "[START_TIME] "
            + PREFIX_END_TIME + "END_TIME";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an event entry to the calendar.\n"
            + "Parameters: "
            + PREFIX_ENTRY_TITLE + "ENTRY_TITLE "
            + "[" + PREFIX_START_DATE + "START_DATE] "
            + PREFIX_END_DATE + "END_DATE "
            + "[" + PREFIX_START_TIME + "START_TIME] "
            + PREFIX_END_TIME + "END_TIME\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_ENTRY_TITLE + "Meeting with Boss "
            + PREFIX_START_DATE + "05-05-2018 "
            + PREFIX_END_DATE + "05-05-2018 "
            + PREFIX_START_TIME + "10:00 "
            + PREFIX_END_TIME + "12:30";

    public static final String MESSAGE_ADD_ENTRY_SUCCESS = "Added Entry [%1$s]";
    public static final String MESSAGE_DUPLICATE_EVENT = "This entry already exists in calendar.";

    private final CalendarEntry calEntryToAdd;

    /**
     * Creates an AddEntryCommand to add specified {@code CalendarEntry}.
     */
    public AddEntryCommand(CalendarEntry calEntry) {
        requireNonNull(calEntry);
        this.calEntryToAdd = calEntry;
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.addCalendarEntry(calEntryToAdd);
            EventsCenter.getInstance().post(new DisplayCalendarRequestEvent(Optional.empty()));
            EventsCenter.getInstance().post(new DisplayCalendarEntryListEvent());
            return new CommandResult(String.format(MESSAGE_ADD_ENTRY_SUCCESS, calEntryToAdd));
        } catch (DuplicateCalendarEntryException dcee) {
            throw new CommandException(MESSAGE_DUPLICATE_EVENT);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddEntryCommand // instanceof handles nulls
                && calEntryToAdd.equals(((AddEntryCommand) other).calEntryToAdd));
    }
}
```
###### \java\seedu\address\logic\commands\DeleteEntryCommand.java
``` java
public class DeleteEntryCommand extends UndoableCommand {


    public static final String COMMAND_WORD = "entrydelete";
    public static final String COMMAND_ALIAS = "ed";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the calendar entry identified by the index number used in the entry listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_ENTRY_SUCCESS = "Deleted Calendar Entry: %1$s";

    private final Index targetIndex;

    private CalendarEntry entryToDelete;

    public DeleteEntryCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(entryToDelete);
        try {
            model.deleteCalendarEntry(entryToDelete);
        } catch (CalendarEntryNotFoundException cenfe) {
            throw new AssertionError("The target calendar entry cannot be missing");
        }

        EventsCenter.getInstance().post(new DisplayCalendarEntryListEvent());
        return new CommandResult(String.format(MESSAGE_DELETE_ENTRY_SUCCESS, entryToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<CalendarEntry> lastShownList = model.getFilteredCalendarEventList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }

        entryToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteEntryCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteEntryCommand) other).targetIndex) // state check
                && Objects.equals(this.entryToDelete, ((DeleteEntryCommand) other).entryToDelete));
    }
}
```
###### \java\seedu\address\logic\commands\DeleteGroupCommand.java
``` java
public class DeleteGroupCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "groupdelete";
    public static final String COMMAND_ALIAS = "gd";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the specified group from all persons in address book.\n"
            + "Parameters: GROUP_NAME (must be alphanumeric)\n"
            + "Example: " + COMMAND_WORD + " friends";

    public static final String MESSAGE_DELETE_GROUP_SUCCESS = "Deleted GROUP: %1$s";
    public static final String MESSAGE_GROUP_NOT_FOUND = "Group does not exist in address book.";


    private Group groupToDelete;

    public DeleteGroupCommand(Group targetGroup) {
        this.groupToDelete = targetGroup;
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(groupToDelete);
        try {
            model.deleteGroup(groupToDelete);
            return new CommandResult(String.format(MESSAGE_DELETE_GROUP_SUCCESS, groupToDelete));
        } catch (GroupNotFoundException e) {
            throw new CommandException(MESSAGE_GROUP_NOT_FOUND);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteGroupCommand // instanceof handles nulls
                && Objects.equals(this.groupToDelete, ((DeleteGroupCommand) other).groupToDelete)); // state check
    }
}
```
###### \java\seedu\address\logic\commands\DeletePreferenceCommand.java
``` java
public class DeletePreferenceCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "prefdelete";
    public static final String COMMAND_ALIAS = "pd";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the specified preference from all persons in address book.\n"
            + "Parameters: PREFERENCE_NAME (must be alphanumeric)\n"
            + "Example: " + COMMAND_WORD + " computers";

    public static final String MESSAGE_DELETE_PREFERENCE_SUCCESS = "Deleted PREFERENCE: %1$s";
    public static final String MESSAGE_PREFERENCE_NOT_FOUND = "Preference does not exist in address book.";

    private Preference prefToDelete;

    public DeletePreferenceCommand(Preference targetPref) {
        this.prefToDelete = targetPref;
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(prefToDelete);
        try {
            model.deletePreference(prefToDelete);
            return new CommandResult(String.format(MESSAGE_DELETE_PREFERENCE_SUCCESS, prefToDelete));
        } catch (PreferenceNotFoundException e) {
            throw new CommandException(MESSAGE_PREFERENCE_NOT_FOUND);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeletePreferenceCommand // instanceof handles nulls
                && Objects.equals(this.prefToDelete, ((DeletePreferenceCommand) other).prefToDelete)); // state check
    }
}
```
###### \java\seedu\address\logic\commands\FindGroupCommand.java
``` java
public class FindGroupCommand extends Command {

    public static final String COMMAND_WORD = "groupfind";
    public static final String COMMAND_ALIAS = "gf";
    public static final String COMMAND_SYNTAX = COMMAND_WORD + " "
            + "KEYWORD "
            + "[MORE KEYWORDS]";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose groups contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " friends colleagues neighbours";

    private final GroupsContainKeywordsPredicate groupsContainKeywordsPredicate;

    public FindGroupCommand(GroupsContainKeywordsPredicate predicate) {
        this.groupsContainKeywordsPredicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(groupsContainKeywordsPredicate);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindGroupCommand // instanceof handles nulls
                && this.groupsContainKeywordsPredicate.equals
                (((FindGroupCommand) other).groupsContainKeywordsPredicate)); // state check
    }
}
```
###### \java\seedu\address\logic\commands\FindPreferenceCommand.java
``` java
public class FindPreferenceCommand extends Command {
    public static final String COMMAND_WORD = "preffind";
    public static final String COMMAND_ALIAS = "pf";
    public static final String COMMAND_SYNTAX = COMMAND_WORD + " "
            + "KEYWORD "
            + "[MORE KEYWORDS]";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose preferences contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " shoes computers videoGames";

    private final PreferencesContainKeywordsPredicate preferencesContainKeywordsPredicate;

    public FindPreferenceCommand(PreferencesContainKeywordsPredicate predicate) {
        this.preferencesContainKeywordsPredicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(preferencesContainKeywordsPredicate);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindPreferenceCommand // instanceof handles nulls
                && this.preferencesContainKeywordsPredicate.equals
                (((FindPreferenceCommand) other).preferencesContainKeywordsPredicate)); // state check
    }

}
```
###### \java\seedu\address\logic\commands\ListCalendarEntryCommand.java
``` java
public class ListCalendarEntryCommand extends Command {
    public static final String COMMAND_WORD = "entrylist";
    public static final String COMMAND_ALIAS = "el";

    public static final String MESSAGE_SUCCESS = "Listed all calendar entries";


    @Override
    public CommandResult execute() {
        model.updateFilteredCalendarEventList(Model.PREDICATE_SHOW_ALL_CALENDAR_ENTRIES);
        EventsCenter.getInstance().post(new DisplayCalendarEntryListEvent());
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
```
###### \java\seedu\address\logic\commands\ListOrderCommand.java
``` java
public class ListOrderCommand extends Command {
    public static final String COMMAND_WORD = "orderlist";
    public static final String COMMAND_ALIAS = "ol";

    public static final String MESSAGE_SUCCESS = "Listed all orders";


    @Override
    public CommandResult execute() {
        model.updateFilteredOrderList(PREDICATE_SHOW_ALL_ORDERS);
        EventsCenter.getInstance().post(new DisplayOrderListEvent());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### \java\seedu\address\logic\commands\ViewCalendarCommand.java
``` java
public class ViewCalendarCommand extends Command {
    public static final String COMMAND_WORD = "calendar";
    public static final String COMMAND_ALIAS = "cal";


    public static final String COMMAND_SYNTAX = COMMAND_WORD + " "
            + "[VIEW_FORMAT]";


    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Displays Calendar in a specified format.\n"
            + "Parameters: [VIEW_FORMAT] (must be either \"day\", \"week\" or \"month\" without captions)\n"
            + "If no parameters are given or given parameter does not follow the required keywords,"
            + " calendar will display in Day-View.\n"
            + "Example: " + COMMAND_WORD + " day";

    public static final String MESSAGE_SHOW_CALENDAR_SUCCESS = "Display Calendar in %1$s-View.";


    public static final String MONTH_VIEW = "Month";
    public static final String DAY_VIEW = "Day";
    public static final String WEEK_VIEW = "Week";

    private final String view;

    public ViewCalendarCommand(String view) {
        String trimmedView = view.trim();
        requireNonNull(trimmedView);

        if (trimmedView.equalsIgnoreCase(MONTH_VIEW)) {
            this.view = MONTH_VIEW;
        } else if (trimmedView.equalsIgnoreCase(WEEK_VIEW)) {
            this.view = WEEK_VIEW;
        } else { //If user input is equal to DAY_VIEW or input does not conform to any of the required keywords
            this.view = DAY_VIEW;
        }
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(view);
        Optional<String> selectedView = Optional.of(view); //Guaranteed that view cannot be null

        EventsCenter.getInstance().post(new DisplayCalendarRequestEvent(selectedView));
        return new CommandResult(String.format(MESSAGE_SHOW_CALENDAR_SUCCESS, view));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ViewCalendarCommand); // instanceof handles nulls
    }

}
```
###### \java\seedu\address\logic\parser\AddEventCommandParser.java
``` java
public class AddEventCommandParser implements Parser<AddEntryCommand> {

    public static final String EVENT_DURATION_CONSTRAINTS =
            "Event must last at least 15 minutes if ending in same day."; //Constraint of CalendarFX entries
    public static final String STANDARD_START_TIME = "00:00"; //Start Time of event if StartTime not given
    public static final String START_AND_END_DATE_CONSTRAINTS = "Start Date cannot be later than End Date.";
    public static final String START_AND_END_TIME_CONSTRAINTS =
            "Start Time cannot be later than End Time if Event ends on same date.";

    private static final int MINIMAL_DURATION = 15; //Constraint of CalendarFX entries

    /**
     * Parses the given {@code String} of arguments in the context of the AddEntryCommand
     * and returns an AddEntryCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public AddEntryCommand parse(String userInput) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(userInput, PREFIX_ENTRY_TITLE, PREFIX_START_DATE, PREFIX_END_DATE,
                        PREFIX_START_TIME, PREFIX_END_TIME);

        if (!arePrefixesPresent(argMultimap, PREFIX_ENTRY_TITLE, PREFIX_END_DATE, PREFIX_END_TIME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEntryCommand.MESSAGE_USAGE));
        }

        try {

            EntryTitle entryTitle = ParserUtil.parseEventTitle(argMultimap.getValue(PREFIX_ENTRY_TITLE)).get();
            EndDate endDate = ParserUtil.parseEndDate(argMultimap.getValue(PREFIX_END_DATE)).get();
            StartDate startDate;

            // If no Start Date is given, Start Date will be the same date as End Date
            if (!argMultimap.getValue(PREFIX_START_DATE).isPresent()) {
                startDate = ParserUtil.parseStartDate(argMultimap.getValue(PREFIX_END_DATE)).get();
            } else {
                startDate = ParserUtil.parseStartDate(argMultimap.getValue(PREFIX_START_DATE)).get();
            }

            if (startDateLaterThanEndDate(startDate, endDate)) {
                throw new IllegalValueException(START_AND_END_DATE_CONSTRAINTS);
            }

            EndTime endTime = ParserUtil.parseEndTime(argMultimap.getValue(PREFIX_END_TIME)).get();
            StartTime startTime;

            // If no Start Time is given, Start Time will be 00:00
            if (!argMultimap.getValue(PREFIX_START_TIME).isPresent()) {
                startTime = ParserUtil.parseStartTime(STANDARD_START_TIME);
            } else {
                startTime = ParserUtil.parseStartTime(argMultimap.getValue(PREFIX_START_TIME)).get();
            }

            if (startDate.toString().equals(endDate.toString()) && startTimeLaterThanEndTime(startTime, endTime)) {
                throw new IllegalValueException(START_AND_END_TIME_CONSTRAINTS);
            }

            if (startDate.toString().equals(endDate.toString())
                    && eventIsShorterThanFifteenMinutes(startTime, endTime)) {
                throw new IllegalValueException(EVENT_DURATION_CONSTRAINTS);
            }

            CalendarEntry calendarEntry = new CalendarEntry(entryTitle, startDate, endDate, startTime, endTime);
            return new AddEntryCommand(calendarEntry);

        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Returns true if duration between start time and end time is less than 15 minutes.
     * This is a constraint that CalendarFX has. Event duration must last at least 15 minutes.
     */
    private boolean eventIsShorterThanFifteenMinutes(StartTime startTime, EndTime endTime) {
        if (MINUTES.between(startTime.getLocalTime(), endTime.getLocalTime()) < MINIMAL_DURATION) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if given start time is later than end time.
     * Start time cannot be later than End time if event ends on the same date.
     */
    private boolean startTimeLaterThanEndTime(StartTime startTime, EndTime endTime) {

        return startTime.getLocalTime().isAfter(endTime.getLocalTime());
    }

    /**
     * Returns true if given start date is later than end date.
     * Start Date cannot be later than End Date as it violates the meaning of the terms.
     */
    private static boolean startDateLaterThanEndDate(StartDate startDate, EndDate endDate) {
        return startDate.getLocalDate().isAfter(endDate.getLocalDate());
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### \java\seedu\address\logic\parser\DeleteEntryCommandParser.java
``` java
public class DeleteEntryCommandParser implements Parser<DeleteEntryCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the DeleteEntryCommand
     * and returns an DeleteEntryCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteEntryCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteEntryCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteEntryCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \java\seedu\address\logic\parser\DeleteGroupCommandParser.java
``` java
public class DeleteGroupCommandParser implements Parser<DeleteGroupCommand> {

    @Override
    public DeleteGroupCommand parse(String userInput) throws ParseException {
        try {
            Group group = ParserUtil.parseGroup(userInput);
            return new DeleteGroupCommand(group);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteGroupCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \java\seedu\address\logic\parser\DeletePreferenceCommandParser.java
``` java
public class DeletePreferenceCommandParser implements Parser<DeletePreferenceCommand> {
    @Override
    public DeletePreferenceCommand parse(String userInput) throws ParseException {
        try {
            Preference preference = ParserUtil.parsePreference(userInput);
            return new DeletePreferenceCommand(preference);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePreferenceCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \java\seedu\address\logic\parser\FindGroupCommandParser.java
``` java
public class FindGroupCommandParser implements Parser<FindGroupCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the FindGroupCommand
     * and returns an FindGroupCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindGroupCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindGroupCommand.MESSAGE_USAGE));
        }

        String[] groupKeywords = trimmedArgs.split("\\s+");

        return new FindGroupCommand(new GroupsContainKeywordsPredicate(Arrays.asList(groupKeywords)));
    }
}
```
###### \java\seedu\address\logic\parser\FindPreferenceCommandParser.java
``` java
public class FindPreferenceCommandParser implements Parser<FindPreferenceCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the FindPreferenceCommand
     * and returns an FindPreferenceCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindPreferenceCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindPreferenceCommand.MESSAGE_USAGE));
        }

        String[] preferenceKeywords = trimmedArgs.split("\\s+");

        return new FindPreferenceCommand(new PreferencesContainKeywordsPredicate(Arrays.asList(preferenceKeywords)));
    }

}
```
###### \java\seedu\address\model\CalendarManager.java
``` java
public class CalendarManager implements ReadOnlyCalendarManager {
    private final Calendar calendar;
    private final UniqueCalendarEntryList calendarEntryList;

    public CalendarManager() {
        calendarEntryList = new UniqueCalendarEntryList();
        calendar = new Calendar();
        calendar.setReadOnly(true);
        calendar.setStyle(Calendar.Style.STYLE1);
    }

    public CalendarManager(ReadOnlyCalendarManager toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * Resets the existing data of this {@code CalendarManager} with {@code newData}.
     */
    public void resetData(ReadOnlyCalendarManager newData) {
        requireNonNull(newData);

        List<CalendarEntry> calEntries = new ArrayList<>(newData.getCalendarEntryList());

        try {
            setCalendarEntries(calEntries);
        } catch (DuplicateCalendarEntryException dcee) {
            throw new AssertionError("Calendar Manager should not have duplicate calendar entries.");
        }
        updateCalendar();
    }

    /**
     * Updates Calendar with entries converted from {@code calendarEntryList}.
     */
    private void updateCalendar() {
        calendar.clear();
        calendar.addEntries(CalendarUtil.convertEntireListToEntries(calendarEntryList.asObservableList()));
    }

    /**
     * Sets {@code calendarEntryList} to match the given list of calendar entries.
     */
    private void setCalendarEntries(List<CalendarEntry> calEntries)
            throws DuplicateCalendarEntryException {
        calendarEntryList.setCalEntryList(calEntries);
    }

    @Override
    public ObservableList<CalendarEntry> getCalendarEntryList() {
        return calendarEntryList.asObservableList();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    // Managing CalendarEntries operations

    /**
     * Adds a calendar entries to list of calendar entries in calendar manager.
     * @throws DuplicateCalendarEntryException
     * if there exist an equivalent calendar entry in calendar manager.
     */
    public void addCalendarEntry(CalendarEntry toAdd) throws DuplicateCalendarEntryException {
        calendarEntryList.add(toAdd);
        updateCalendar();
    }

    /**
     * Removes an existing calendar entry in list of calendar entries and from the calendar itself.
     * @throws CalendarEntryNotFoundException
     * if given calendar entry does not exist in list of calendar entry
     */
    public void deleteCalendarEntry(CalendarEntry entryToDelete) throws CalendarEntryNotFoundException {
        if (!calendarEntryList.remove(entryToDelete)) {
            throw new CalendarEntryNotFoundException();
        } else {
            updateCalendar();
        }

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CalendarManager // instanceof handles nulls
                && this.calendarEntryList.equals(((CalendarManager) other).calendarEntryList));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(calendar, calendarEntryList);
    }


}
```
###### \java\seedu\address\model\event\CalendarEntry.java
``` java
public class CalendarEntry {

    private final EntryTitle entryTitle;
    private final StartDate startDate;
    private final EndDate endDate;
    private final StartTime startTime;
    private final EndTime endTime;


    /**
     * Every field must be present, and not null.
     */
    public CalendarEntry(EntryTitle entryTitle, StartDate startDate, EndDate endDate,
                         StartTime startTime, EndTime endTime) {
        requireAllNonNull(entryTitle, startDate, endDate, startTime, endTime);
        this.entryTitle = entryTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public EntryTitle getEntryTitle() {
        return entryTitle;
    }

    public StartDate getStartDate() {
        return startDate;
    }

    public EndDate getEndDate() {
        return endDate;
    }

    public StartTime getStartTime() {
        return startTime;
    }

    public EndTime getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof CalendarEntry)) {
            return false;
        }

        CalendarEntry otherCalEvent = (CalendarEntry) other;
        return otherCalEvent.getEntryTitle().equals(this.getEntryTitle())
                && otherCalEvent.getStartDate().equals(this.getStartDate())
                && otherCalEvent.getEndDate().equals(this.getEndDate())
                && otherCalEvent.getStartTime().equals(this.getStartTime())
                && otherCalEvent.getEndTime().equals(this.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(entryTitle, startDate, endDate, startTime, endTime);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getEntryTitle())
                .append(" Start Date: ")
                .append(getStartDate())
                .append(" End Date: ")
                .append(getEndDate())
                .append(" Start Time: ")
                .append(getStartTime())
                .append(" End Time: ")
                .append(getEndTime());
        return builder.toString();
    }
}
```
###### \java\seedu\address\model\event\EndDate.java
``` java
public class EndDate {
    public static final String MESSAGE_END_DATE_CONSTRAINTS =
            "End Date should be DD-MM-YYYY, and it should not be blank";

    private final String endDateString;
    private final LocalDate endDate;

    /**
     * Constructs {@code EndDate}.
     *
     * @param endDate Valid end date.
     */
    public EndDate(String endDate) {
        requireNonNull(endDate);
        checkArgument(isValidDate(endDate), MESSAGE_END_DATE_CONSTRAINTS);
        try {
            this.endDate = convertStringToDate(endDate);
            this.endDateString = endDate;
        } catch (DateTimeParseException dtpe) {
            throw new AssertionError("Given End date should be valid for conversion.");
        }
    }

    public LocalDate getLocalDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return endDateString;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EndDate // instanceof handles nulls
                && this.endDate.equals(((EndDate) other).endDate)); // state check
    }

    @Override
    public int hashCode() {
        return endDate.hashCode();
    }
}
```
###### \java\seedu\address\model\event\EndTime.java
``` java
public class EndTime {

    public static final String MESSAGE_END_TIME_CONSTRAINTS =
            "End Time should be HH:mm (24Hour Format), and it should not be blank";

    private final String endTimeString;
    private final LocalTime endTime;

    /**
     * Constructs {@code EndTime}.
     * @param endTime Valid end time.
     */
    public EndTime (String endTime) {
        requireNonNull(endTime);
        checkArgument(isValidTime(endTime), MESSAGE_END_TIME_CONSTRAINTS);
        try {
            this.endTime = convertStringToTime(endTime);
            this.endTimeString = endTime;
        } catch (DateTimeParseException dtpe) {
            throw new AssertionError("Given start time should be valid for conversion.");
        }
    }

    public LocalTime getLocalTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return endTimeString;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EndTime // instanceof handles nulls
                && this.endTime.equals(((EndTime) other).endTime)); // state check
    }

    @Override
    public int hashCode() {
        return endTime.hashCode();
    }
}
```
###### \java\seedu\address\model\event\EntryTitle.java
``` java
public class EntryTitle {
    public static final String MESSAGE_ENTRY_TITLE_CONSTRAINTS =
            "Event title should only contain alphanumeric characters and spaces"
                    + "and it should not be blank";

    public static final String ENTRY_TITLE_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    private final String entryTitle;

    /**
     * Constructs {@code EntryTitle}.
     *
     * @param entryTitle Valid event title.
     */
    public EntryTitle(String entryTitle) {
        requireNonNull(entryTitle);
        checkArgument(isValidEntryTitle(entryTitle), MESSAGE_ENTRY_TITLE_CONSTRAINTS);
        this.entryTitle = entryTitle;
    }

    /**
     * Returns true if a given string is a valid event title.
     */
    public static boolean isValidEntryTitle(String test) {
        return test.matches(ENTRY_TITLE_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return entryTitle;
    }

    /**
     * entryTitle matching is non case-sensitive
     */
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EntryTitle // instanceof handles nulls
                && this.entryTitle.equalsIgnoreCase(((EntryTitle) other).entryTitle)); // state check
    }

    @Override
    public int hashCode() {
        return entryTitle.hashCode();
    }
}
```
###### \java\seedu\address\model\event\exceptions\CalendarEntryNotFoundException.java
``` java
public class CalendarEntryNotFoundException extends Exception {
}
```
###### \java\seedu\address\model\event\exceptions\DuplicateCalendarEntryException.java
``` java
public class DuplicateCalendarEntryException extends DuplicateDataException {

    public DuplicateCalendarEntryException() {
        super("Operation would result in duplicate events");
    }
}
```
###### \java\seedu\address\model\event\StartDate.java
``` java
public class StartDate {
    public static final String MESSAGE_START_DATE_CONSTRAINTS =
            "Start Date should be DD-MM-YYYY, and it should not be blank";

    private final String startDateString;
    private final LocalDate startDate;

    /**
     * Constructs {@code StartDate}.
     *
     * @param startDate Valid start date.
     */
    public StartDate(String startDate) {
        requireNonNull(startDate);
        checkArgument(isValidDate(startDate), MESSAGE_START_DATE_CONSTRAINTS);
        try {
            this.startDate = convertStringToDate(startDate);
            this.startDateString = startDate;
        } catch (DateTimeParseException dtpe) {
            throw new AssertionError("Given Start date should be valid for conversion.");
        }
    }

    public LocalDate getLocalDate() {
        return startDate;
    }

    @Override
    public String toString() {
        return startDateString;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof StartDate // instanceof handles nulls
                && this.startDate.equals(((StartDate) other).startDate)); // state check
    }

    @Override
    public int hashCode() {
        return startDate.hashCode();
    }
}
```
###### \java\seedu\address\model\event\StartTime.java
``` java
public class StartTime {
    public static final String MESSAGE_START_TIME_CONSTRAINTS =
            "Start Time should be HH:mm (24Hour Format), and it should not be blank";

    private final String startTimeString;
    private final LocalTime startTime;

    /**
     * Constructs {@code StartTime}.
     *
     * @param startTime Valid start time.
     */
    public StartTime (String startTime) {
        requireNonNull(startTime);
        checkArgument(isValidTime(startTime), MESSAGE_START_TIME_CONSTRAINTS);

        try {
            this.startTime = convertStringToTime(startTime);
            this.startTimeString = startTime;
        } catch (DateTimeParseException dtpe) {
            throw new AssertionError("Given start time should be valid for conversion.");
        }
    }

    public LocalTime getLocalTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return startTimeString;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof StartTime // instanceof handles nulls
                && this.startTime.equals(((StartTime) other).startTime)); // state check
    }

    @Override
    public int hashCode() {
        return startTime.hashCode();
    }
}
```
###### \java\seedu\address\model\event\UniqueCalendarEntryList.java
``` java
public class UniqueCalendarEntryList implements Iterable<CalendarEntry> {
    private final ObservableList<CalendarEntry> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty UniqueCalendarEntryList.
     */
    public UniqueCalendarEntryList() {}

    /**
     * Creates a UniqueCalendarEntryList using given calendar events.
     * Enforces no nulls.
     */
    public UniqueCalendarEntryList(Set<CalendarEntry> calendarEntries) {
        requireAllNonNull(calendarEntries);
        internalList.addAll(calendarEntries);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Returns all calendar entries in this list as a Set.
     * This set is mutable and change-insulated against the internal list.
     */
    public Set<CalendarEntry> toSet() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return new HashSet<>(internalList);
    }

    /**
     * Replaces the CalendarEntries in internal list with those in the argument calendar entry list.
     */
    public void setCalEntryList(List<CalendarEntry> calendarEntries) throws DuplicateCalendarEntryException {
        requireAllNonNull(calendarEntries);
        final UniqueCalendarEntryList replacement = new UniqueCalendarEntryList();
        for (CalendarEntry ce: calendarEntries) {
            replacement.add(ce);
        }

        setCalendarEntries(replacement);

    }

    public void setCalendarEntries(UniqueCalendarEntryList replacement) {
        internalList.setAll(replacement.internalList);
    }

    /**
     * Ensures every calendar event in the argument list exists in this object.
     */
    public void mergeFrom(UniqueCalendarEntryList from) {
        final Set<CalendarEntry> existingEvents = this.toSet();
        from.internalList.stream()
                .filter(calEvent -> !existingEvents.contains(calEvent))
                .forEach(internalList::add);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Returns true if the list contains an equivalent {@code CalendarEntry} as the given argument.
     */
    public boolean contains(CalendarEntry toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds an CalendarEntry to the list.
     *
     * @throws DuplicateCalendarEntryException if the CalendarEntry to add
     * is a duplicate of an existing CalendarEntry in the list.
     */
    public void add(CalendarEntry toAdd) throws DuplicateCalendarEntryException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateCalendarEntryException();
        }
        internalList.add(toAdd);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Removes CalendarEntry from list if it exists.
     */
    public boolean remove(CalendarEntry toRemove) {
        requireNonNull(toRemove);
        if (contains(toRemove)) {
            internalList.remove(toRemove);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Iterator<CalendarEntry> iterator() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return internalList.iterator();
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<CalendarEntry> asObservableList() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public boolean equals(Object other) {
        assert CollectionUtil.elementsAreUnique(internalList);
        return other == this // short circuit if same object
                || (other instanceof UniqueCalendarEntryList // instanceof handles nulls
                && this.internalList.equals(((UniqueCalendarEntryList) other).internalList));
    }

    /**
     * Returns true if the element in this list is equal to the elements in {@code other}.
     * The elements do not have to be in the same order.
     */
    public boolean equalsOrderInsensitive(UniqueCalendarEntryList other) {
        assert CollectionUtil.elementsAreUnique(internalList);
        assert CollectionUtil.elementsAreUnique(other.internalList);
        return this == other || new HashSet<>(this.internalList).equals(new HashSet<>(other.internalList));
    }

    @Override
    public int hashCode() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return internalList.hashCode();
    }

}
```
###### \java\seedu\address\model\Model.java
``` java
    /**
     * Adds event to list of calendar events.
     */
    void addCalendarEntry(CalendarEntry toAdd) throws DuplicateCalendarEntryException;

    /**
     * Deletes given calendar entry from calendar.
     */
    void deleteCalendarEntry(CalendarEntry entryToDelete) throws CalendarEntryNotFoundException;

    /** Returns an unmodifiable view of the filtered order list */
    ObservableList<CalendarEntry> getFilteredCalendarEventList();

    /** Returns Calendar stored in Model. */
    Calendar getCalendar();

    /** Returns the CalendarManager */
    ReadOnlyCalendarManager getCalendarManager();

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void addCalendarEntry(CalendarEntry toAdd) throws DuplicateCalendarEntryException {
        calendarManager.addCalendarEntry(toAdd);
        updateFilteredCalendarEventList(PREDICATE_SHOW_ALL_CALENDAR_ENTRIES);
        indicateCalendarManagerChanged();
    }


    @Override
    public void deleteCalendarEntry(CalendarEntry entryToDelete) throws CalendarEntryNotFoundException {
        calendarManager.deleteCalendarEntry(entryToDelete);
        updateFilteredCalendarEventList(PREDICATE_SHOW_ALL_CALENDAR_ENTRIES);
        indicateCalendarManagerChanged();
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public ObservableList<CalendarEntry> getFilteredCalendarEventList() {
        return FXCollections.unmodifiableObservableList(filteredEvents);
    }

    @Override
    public void updateFilteredCalendarEventList(Predicate<CalendarEntry> predicate) {
        requireNonNull(predicate);
        filteredEvents.setPredicate(predicate);
    }

    @Override
    public Calendar getCalendar() {
        return calendarManager.getCalendar();
    }

```
###### \java\seedu\address\model\person\GroupsContainKeywordsPredicate.java
``` java
public class GroupsContainKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public GroupsContainKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> personGroupsMatchesKeyword(person, keyword));
    }

    /**
     * Checks if person contains group with group tag names matching given keyword.
     * Matching is case-insensitive.
     */
    private boolean personGroupsMatchesKeyword(Person person, String keyword) {
        Set<String> groupNames = person.getGroupTags().stream().map(group -> group.tagName).collect(Collectors.toSet());
        for (String groupName: groupNames) {
            if (StringUtil.containsWordIgnoreCase(groupName, keyword)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof GroupsContainKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((GroupsContainKeywordsPredicate) other).keywords)); // state check
    }
}
```
###### \java\seedu\address\model\person\PreferencesContainKeywordsPredicate.java
``` java
public class PreferencesContainKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public PreferencesContainKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> personGroupsMatchesKeyword(person, keyword));
    }

    /**
     * Checks if person contains preferences with  preference tag names matching given keyword.
     * Matching is case-insensitive.
     */
    private boolean personGroupsMatchesKeyword(Person person, String keyword) {
        Set<String> prefNames = person.getPreferenceTags().stream().map(pref -> pref.tagName)
                .collect(Collectors.toSet());

        for (String prefName: prefNames) {
            if (StringUtil.containsWordIgnoreCase(prefName, keyword)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PreferencesContainKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((PreferencesContainKeywordsPredicate) other).keywords)); // state check
    }
}
```
###### \java\seedu\address\model\ReadOnlyCalendarManager.java
``` java
public interface ReadOnlyCalendarManager {

    /**
     * Returns an unmodifiable view of the calendar entry list.
     * This list will not contain any duplicate calendar entries.
     */
    ObservableList<CalendarEntry> getCalendarEntryList();
}
```
###### \java\seedu\address\storage\CalendarManagerStorage.java
``` java
public interface CalendarManagerStorage {
    /**
     * Returns the file path of the data file.
     */
    String getCalendarManagerFilePath();

    /**
     * Returns CalendarManager data as a {@link ReadOnlyCalendarManager}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyCalendarManager> readCalendarManager() throws DataConversionException, IOException;

    /**
     * @see #getCalendarManagerFilePath()
     */
    Optional<ReadOnlyCalendarManager> readCalendarManager(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyCalendarManager} to the storage.
     * @param calendarManager cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveCalendarManager(ReadOnlyCalendarManager calendarManager) throws IOException;

    /**
     * @see #saveCalendarManager(ReadOnlyCalendarManager)
     */
    void saveCalendarManager(ReadOnlyCalendarManager calendarManager, String filePath) throws IOException;

}
```
###### \java\seedu\address\storage\StorageManager.java
``` java
    @Override
    public String getCalendarManagerFilePath() {
        return calendarManagerStorage.getCalendarManagerFilePath();
    }

    @Override
    public Optional<ReadOnlyCalendarManager> readCalendarManager() throws DataConversionException, IOException {
        return readCalendarManager(calendarManagerStorage.getCalendarManagerFilePath());
    }

    @Override
    public Optional<ReadOnlyCalendarManager> readCalendarManager(String filePath)
            throws DataConversionException, IOException {

        logger.fine("Attempting to read calendar data from file: " + filePath);
        return calendarManagerStorage.readCalendarManager(filePath);
    }

    @Override
    public void saveCalendarManager(ReadOnlyCalendarManager calendarManager) throws IOException {
        saveCalendarManager(calendarManager, calendarManagerStorage.getCalendarManagerFilePath());
    }

    @Override
    public void saveCalendarManager(ReadOnlyCalendarManager calendarManager, String filePath) throws IOException {
        logger.fine("Attempting to write to calendar data file: " + filePath);
        calendarManagerStorage.saveCalendarManager(calendarManager, filePath);
    }
```
###### \java\seedu\address\storage\StorageManager.java
``` java
    @Override
    @Subscribe
    public void handleCalendarManagerChangedEvent(CalendarManagerChangedEvent event) {
        logger.info(LogsCenter
                .getEventHandlingLogMessage(event, "Local calendar data changed, saving to file"));

        try {
            saveCalendarManager(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }
```
###### \java\seedu\address\storage\XmlAdaptedCalendarEntry.java
``` java
public class XmlAdaptedCalendarEntry {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "CalendarEntry's %s field is missing!";
    public static final String START_AND_END_DATE_CONSTRAINTS = "Start Date cannot be later than End Date.";
    public static final String START_AND_END_TIME_CONSTRAINTS =
            "Start Time cannot be later than End Time if Event ends on same date.";
    public static final String EVENT_DURATION_CONSTRAINTS =
            "Event must last at least 15 minutes if ending in same day."; //Constraint of CalendarFX entries

    private static final int MINIMAL_DURATION = 15; //Constraint of CalendarFX entries

    @XmlElement
    private String entryTitle;
    @XmlElement
    private String startDate;
    @XmlElement
    private String endDate;
    @XmlElement
    private String startTime;
    @XmlElement
    private String endTime;

    /**
     * Constructs an XmlAdaptedCalendarEntry.
     */
    public XmlAdaptedCalendarEntry() {}

    /**
     * Constructs an {@code XmlAdaptedCalendarEntry} with the given calendar event details.
     */
    public XmlAdaptedCalendarEntry(String entryTitle, String startDate, String endDate,
                                   String startTime, String endTime) {
        this.entryTitle = entryTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Converts a given Order into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedCalendarEntry
     */
    public XmlAdaptedCalendarEntry(CalendarEntry source) {
        entryTitle = source.getEntryTitle().toString();
        startDate = source.getStartDate().toString();
        endDate = source.getEndDate().toString();
        startTime = source.getStartTime().toString();
        endTime = source.getEndTime().toString();
    }

    /**
     * Converts the jaxb-friendly adapted calendar event object into the model's CalendarEntry object.
     *
     * @throws IllegalValueException if any data constraints are violated in the adapted calendar event's fields.
     */
    public CalendarEntry toModelType() throws IllegalValueException {
        if (this.entryTitle == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    EntryTitle.class.getSimpleName()));
        }
        if (!EntryTitle.isValidEntryTitle(this.entryTitle)) {
            throw new IllegalValueException(EntryTitle.MESSAGE_ENTRY_TITLE_CONSTRAINTS);
        }
        final EntryTitle entryTitle = new EntryTitle(this.entryTitle);

        if (this.startDate == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    StartDate.class.getSimpleName()));
        }

        if (!DateUtil.isValidDate(this.startDate)) {
            throw new IllegalValueException(StartDate.MESSAGE_START_DATE_CONSTRAINTS);
        }
        final StartDate startDate = new StartDate(this.startDate);

        if (this.endDate == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    EndDate.class.getSimpleName()));
        }

        if (!DateUtil.isValidDate(this.endDate)) {
            throw new IllegalValueException(EndDate.MESSAGE_END_DATE_CONSTRAINTS);
        }
        final EndDate endDate = new EndDate(this.endDate);

        if (this.startTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    StartTime.class.getSimpleName()));
        }

        if (!TimeUtil.isValidTime(this.startTime)) {
            throw new IllegalValueException(StartTime.MESSAGE_START_TIME_CONSTRAINTS);
        }
        final StartTime startTime = new StartTime(this.startTime);

        if (this.endTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    EndTime.class.getSimpleName()));
        }

        if (!TimeUtil.isValidTime(this.endTime)) {
            throw new IllegalValueException(EndTime.MESSAGE_END_TIME_CONSTRAINTS);
        }

        final EndTime endTime = new EndTime(this.endTime);

        // Exception thrown if Start Date is later than End Date
        if (startDate.getLocalDate().isAfter(endDate.getLocalDate())) {
            throw new IllegalValueException(START_AND_END_DATE_CONSTRAINTS);
        }

        // Check for cases when Start Date is equal to End Date
        if (startDate.getLocalDate().equals(endDate.getLocalDate())) {
            // Check if start time is later than end time
            if (startTime.getLocalTime().isAfter(endTime.getLocalTime())) {
                throw new IllegalValueException(START_AND_END_TIME_CONSTRAINTS);
            }

            // Check if duration of event is less than 15 minutes
            if (MINUTES.between(startTime.getLocalTime(), endTime.getLocalTime()) < MINIMAL_DURATION) {
                throw new IllegalValueException(EVENT_DURATION_CONSTRAINTS);
            }
        }


        return new CalendarEntry(entryTitle, startDate, endDate, startTime, endTime);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedCalendarEntry)) {
            return false;
        }

        XmlAdaptedCalendarEntry otherCalEvent = (XmlAdaptedCalendarEntry) other;
        return Objects.equals(entryTitle, otherCalEvent.entryTitle)
                && Objects.equals(startDate, otherCalEvent.startDate)
                && Objects.equals(endDate, otherCalEvent.endDate)
                && Objects.equals(startTime, otherCalEvent.startTime)
                && Objects.equals(endTime, otherCalEvent.endTime);
    }

}
```
###### \java\seedu\address\storage\XmlCalendarManagerStorage.java
``` java
public class XmlCalendarManagerStorage implements CalendarManagerStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlAddressBookStorage.class);

    private String filePath;

    public XmlCalendarManagerStorage(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getCalendarManagerFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyCalendarManager> readCalendarManager() throws DataConversionException, IOException {
        return readCalendarManager(filePath);
    }

    /**
     * Similar to {@link #readCalendarManager()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyCalendarManager> readCalendarManager(String filePath)
            throws DataConversionException, IOException {
        requireNonNull(filePath);

        File calendarManagerFile = new File(filePath);

        if (!calendarManagerFile.exists()) {
            logger.info("CalendarManager file "  + calendarManagerFile + " not found");
            return Optional.empty();
        }

        XmlSerializableCalendarManager xmlCalManager =
                XmlFileStorage.loadCalendarManagerDataFromSaveFile(new File(filePath));

        try {
            return Optional.of(xmlCalManager.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + calendarManagerFile + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveCalendarManager(ReadOnlyCalendarManager calendarManager) throws IOException {
        saveCalendarManager(calendarManager, filePath);
    }

    @Override
    public void saveCalendarManager(ReadOnlyCalendarManager calendarManager, String filePath) throws IOException {
        requireNonNull(calendarManager);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveCalendarManagerDataToFile(file, new XmlSerializableCalendarManager(calendarManager));
    }
}
```
###### \java\seedu\address\storage\XmlFileStorage.java
``` java
    public static void saveCalendarManagerDataToFile(File file, XmlSerializableCalendarManager calendarManager)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, calendarManager);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage());
        }
    }

    /**
     * Returns calendar manager data in the file or an empty address book
     */
    public static XmlSerializableCalendarManager loadCalendarManagerDataFromSaveFile(File file)
            throws DataConversionException, FileNotFoundException {

        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableCalendarManager.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }
```
###### \java\seedu\address\storage\XmlSerializableCalendarManager.java
``` java
@XmlRootElement(name = "calendarmanager")
public class XmlSerializableCalendarManager {
    @XmlElement
    private List<XmlAdaptedCalendarEntry> calendarEntries;

    /**
     * Creates an empty XmlSerializableCalendarManager.
     */
    public XmlSerializableCalendarManager() {
        calendarEntries = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableCalendarManager(ReadOnlyCalendarManager src) {
        this();
        calendarEntries.addAll(src.getCalendarEntryList().stream().map(XmlAdaptedCalendarEntry::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this calendarManager into the model's {@code CalendarManager} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedCalendarEntry}.
     */
    public CalendarManager toModelType() throws IllegalValueException {
        CalendarManager calendarManager = new CalendarManager();
        for (XmlAdaptedCalendarEntry entry: calendarEntries) {
            calendarManager.addCalendarEntry(entry.toModelType());
        }

        return calendarManager;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableCalendarManager)) {
            return false;
        }

        XmlSerializableCalendarManager otherCm = (XmlSerializableCalendarManager) other;
        return calendarEntries.equals(otherCm.calendarEntries);
    }
}
```
###### \java\seedu\address\ui\CalendarEntryCard.java
``` java
public class CalendarEntryCard extends UiPart<Region> {

    private static final String FXML = "CalendarEntryCard.fxml";

    public final CalendarEntry calendarEntry;

    @FXML
    private HBox cardPane;

    @FXML
    private Label entryTitle;

    @FXML
    private Label id;

    @FXML
    private Label startDate;

    @FXML
    private Label endDate;

    @FXML
    private Label timeDuration;


    public CalendarEntryCard(CalendarEntry calendarEntry, int displayedIndex) {
        super(FXML);
        this.calendarEntry = calendarEntry;
        id.setText(displayedIndex + ". ");
        entryTitle.setText(calendarEntry.getEntryTitle().toString());
        startDate.setText("From: " + calendarEntry.getStartDate().toString());
        endDate.setText("To: " + calendarEntry.getEndDate().toString());
        timeDuration.setText("Between " + calendarEntry.getStartTime().toString()
                + " and " + calendarEntry.getEndTime().toString());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CalendarEntryCard)) {
            return false;
        }

        // state check
        CalendarEntryCard card = (CalendarEntryCard) other;
        return id.getText().equals(card.id.getText())
                && calendarEntry.equals(card.calendarEntry);
    }
}
```
###### \java\seedu\address\ui\CalendarEntryListPanel.java
``` java
public class CalendarEntryListPanel extends UiPart<Region> {

    private static final String FXML = "CalendarEntryListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(CalendarEntryListPanel.class);

    @FXML
    private ListView<CalendarEntryCard> calendarEntryCardListView;

    public CalendarEntryListPanel(ObservableList<CalendarEntry> calendarEntries) {
        super(FXML);
        setConnections(calendarEntries);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<CalendarEntry> calendarEntryList) {
        ObservableList<CalendarEntryCard> mappedList = EasyBind.map(calendarEntryList, (calendarEntry) ->
                        new CalendarEntryCard(calendarEntry, calendarEntryList.indexOf(calendarEntry) + 1));
        calendarEntryCardListView.setItems(mappedList);
        calendarEntryCardListView.setCellFactory(listView -> new CalendarEntryListPanel.CalendarEntryListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        calendarEntryCardListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in calendar entry list panel changed to : '" + newValue + "'");
                        raise(new CalendarEntryPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code CalendarEntryCard}.
     */
    class CalendarEntryListViewCell extends ListCell<CalendarEntryCard> {

        @Override
        protected void updateItem(CalendarEntryCard calendarEntry, boolean empty) {
            super.updateItem(calendarEntry, empty);

            if (empty || calendarEntry == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(calendarEntry.getRoot());
            }
        }
    }

}
```
###### \java\seedu\address\ui\CalendarPanel.java
``` java
public class CalendarPanel extends UiPart<Region> {

    public static final String DAY_VIEW = "Day";
    public static final String MONTH_VIEW = "Month";
    public static final String WEEK_VIEW = "Week";

    private static final String FXML = "CalendarPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    private final CalendarView calendarView;
    private final CalendarSource calendarSource;

    @FXML
    private StackPane calendarPanelholder;

    public CalendarPanel(Calendar calendar) {
        super(FXML);
        calendarView = CalendarFxUtil.returnModifiedCalendarView();
        calendarSource = new CalendarSource();

        initialiseCalendar(calendar);
        createTimeThread();
        registerAsAnEventHandler(this);
    }

    /**
     * Adapted from CalendarFX developer manual QuickStart section.
     * http://dlsc.com/wp-content/html/calendarfx/manual.html#_quick_start
     */
    private void createTimeThread() {
        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                    });

                    try {
                        // update every 10 seconds
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
        };

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
    }

    /**
     * Sets up CalendarFX.
     */
    public void initialiseCalendar(Calendar calendar) {
        calendarSource.getCalendars().addAll(calendar);
        calendarView.getCalendarSources().setAll(calendarSource);
        calendarPanelholder.getChildren().setAll(calendarView);
    }

    @Subscribe
    public void handleDisplayCalendarRequestEvent(DisplayCalendarRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        String view = event.getView();
        if (view != null) {
            if (view.equalsIgnoreCase(MONTH_VIEW)) {
                calendarView.showMonthPage();
            } else if (view.equalsIgnoreCase(DAY_VIEW)) {
                calendarView.showDayPage();
            } else if (view.equalsIgnoreCase(WEEK_VIEW)) {
                calendarView.showWeekPage();
            }
        }
    }

}
```
###### \java\seedu\address\ui\CentrePanel.java
``` java
public class CentrePanel extends UiPart<Region> {

    private static final String FXML = "CentrePanel.fxml";

    private CalendarPanel calendarPanel;

    private PersonPanel personPanel;
    private ObservableList<CalendarEntry> calendarEvents;

    @FXML
    private StackPane centrePlaceholder;

    public CentrePanel(Calendar calendar) {
        super(FXML);

        calendarPanel = new CalendarPanel(calendar);
        this.calendarEvents = calendarEvents;

        displayPersonPanel();
        registerAsAnEventHandler(this);
    }

    /**
     * Displays the Person Panel.
     */
    public void displayPersonPanel() {
        personPanel = new PersonPanel();
        centrePlaceholder.getChildren().add(personPanel.getRoot());
    }

    /**
     * Provides a method to access PersonPanel's method.
     */
    public void freeResources() {
        personPanel.freeResources();
    }

    /**
     * Displays the Calendar Panel.
     */
    public void displayCalendarPanel() {
        centrePlaceholder.getChildren().clear();
        centrePlaceholder.getChildren().add(calendarPanel.getRoot());
    }

    @Subscribe
    private void handleDisplayCalendarRequestEvent(DisplayCalendarRequestEvent event) {
        displayCalendarPanel();
        calendarPanel.handleDisplayCalendarRequestEvent(event);
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        displayPersonPanel();
        personPanel.handlePersonPanelSelectionChangedEvent(event);
    }
}
```
###### \resources\view\CalendarPanel.fxml
``` fxml

<?import javafx.scene.layout.StackPane?>

<StackPane fx:id="calendarPanelholder" minHeight="400.0" minWidth="600.0" prefHeight="400.0" prefWidth="600.0" xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1" />
```
###### \resources\view\CentrePanel.fxml
``` fxml

<?import javafx.scene.layout.StackPane?>


<StackPane prefHeight="400.0" prefWidth="600.0" xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">
   <children>
      <StackPane fx:id="centrePlaceholder" prefHeight="400.0" prefWidth="600.0" />
   </children>
</StackPane>
```
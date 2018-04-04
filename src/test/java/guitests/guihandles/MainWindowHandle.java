package guitests.guihandles;

import javafx.stage.Stage;

/**
 * Provides a handle for {@code MainWindow}.
 */
public class MainWindowHandle extends StageHandle {

    private final PersonListPanelHandle personListPanel;
    private final ResultDisplayHandle resultDisplay;
    private final CommandBoxHandle commandBox;
    private final StatusBarFooterHandle statusBarFooter;
    private final MainMenuHandle mainMenu;
    private final CentrePanelHandle centrePanel;
    private final RightPanelHandle rightPanel;

    public MainWindowHandle(Stage stage) {
        super(stage);

        personListPanel = new PersonListPanelHandle(getChildNode(PersonListPanelHandle.PERSON_LIST_VIEW_ID));
        resultDisplay = new ResultDisplayHandle(getChildNode(ResultDisplayHandle.RESULT_DISPLAY_ID));
        commandBox = new CommandBoxHandle(getChildNode(CommandBoxHandle.COMMAND_INPUT_FIELD_ID));
        statusBarFooter = new StatusBarFooterHandle(getChildNode(StatusBarFooterHandle.STATUS_BAR_PLACEHOLDER));
        mainMenu = new MainMenuHandle(getChildNode(MainMenuHandle.MENU_BAR_ID));
        centrePanel = new CentrePanelHandle(getChildNode(CentrePanelHandle.CENTRE_PANEL_ID));
        rightPanel = new RightPanelHandle(getChildNode(RightPanelHandle.RIGHT_PANEL_ID));
    }

    public PersonListPanelHandle getPersonListPanel() {
        return personListPanel;
    }

    public ResultDisplayHandle getResultDisplay() {
        return resultDisplay;
    }

    public CommandBoxHandle getCommandBox() {
        return commandBox;
    }

    public StatusBarFooterHandle getStatusBarFooter() {
        return statusBarFooter;
    }

    public MainMenuHandle getMainMenu() {
        return mainMenu;
    }

    public PersonPanelHandle getPersonPanel() {
        return centrePanel.getPersonPanelHandle();
    }

    public CalendarPanelHandle getCalendarPanel() {
        return centrePanel.getCalendarPanelHandle();
    }

    public CalendarEntryListPanelHandle getCalendarEntryListPanel() {
        return rightPanel.getCalendarEntryListPanel();
    }

    public OrderListPanelHandle getOrderListPanel() {
        return rightPanel.getOrderListPanelHandle();
    }
}

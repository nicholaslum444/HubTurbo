package unstable;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import guitests.UITest;
import javafx.application.Platform;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;

import org.junit.Before;
import org.junit.Test;

import ui.IdGenerator;
import ui.components.FilterTextField;
import ui.listpanel.ListPanel;
import ui.listpanel.ListPanelCell;


public class ContextMenuTests extends UITest {

    private static final int EVENT_DELAY = 1000;
    private static final int DIALOG_DELAY = 1500;

    @Before
    public void setup() {
        String panelFilterTextFieldId = IdGenerator.getPanelFilterTextFieldIdForTest("dummy/dummy", 0);

        Platform.runLater(stage::show);
        Platform.runLater(stage::requestFocus);

        FilterTextField filterTextField = find(panelFilterTextFieldId);
        filterTextField.setText("");
        Platform.runLater(filterTextField::requestFocus);

        click(panelFilterTextFieldId);
        push(KeyCode.ENTER);
        sleep(EVENT_DELAY);
    }

    /**
     * Tests context menu when no item is selected
     * All menu items should be disabled
     */
    @Test
    public void contextMenuDisabling_noIssueInListView_contextMenuItemsDisabled() {
        String panelId = IdGenerator.getPanelIdForTest("dummy/dummy", 0);
        String panelFilterTextFieldId = IdGenerator.getPanelFilterTextFieldIdForTest("dummy/dummy", 0);

        ListPanel issuePanel = find(panelId);

        click(panelFilterTextFieldId);
        type("asdf");
        push(KeyCode.ENTER);
        sleep(EVENT_DELAY);
        rightClick(panelId);
        sleep(EVENT_DELAY);

        ContextMenu contextMenu = issuePanel.getContextMenu();
        for (MenuItem menuItem : contextMenu.getItems()) {
            assertTrue(menuItem.isDisable());
        }
    }

    /**
     * Tests selecting "Mark as read" and "Mark as unread"
     * context menu items
     */
    @Test
    public void testMarkAsReadUnread() {
        String cellId = IdGenerator.getPanelCellIdForTest("dummy/dummy", 0, 9);

        ListPanelCell listPanelCell = find(cellId);

        click(cellId);
        rightClick(cellId);
        sleep(EVENT_DELAY);
        click("Mark as read (E)");
        sleep(EVENT_DELAY);
        assertTrue(listPanelCell.getIssue().isCurrentlyRead());

        click(cellId);
        rightClick(cellId);
        sleep(EVENT_DELAY);
        click("Mark as unread (U)");
        sleep(EVENT_DELAY);
        assertFalse(listPanelCell.getIssue().isCurrentlyRead());
    }

    /**
     * Tests selecting "Change labels" context menu item
     */
    @Test
    public void testChangeLabels() {
        String cellId = IdGenerator.getPanelCellId("dummy/dummy", 0, 9);
        String labelTextFieldId = IdGenerator.getLabelPickerTextFieldIdForTest();

        click(cellId);
        rightClick(cellId);
        sleep(EVENT_DELAY);
        click("Change labels (L)");
        sleep(DIALOG_DELAY);

        assertNotNull(find(labelTextFieldId));

        push(KeyCode.ESCAPE);
        sleep(EVENT_DELAY);
    }

    /**
     * Tests selecting "Change milestone" context menu item
     */
    @Test
    public void contextMenu_selectChangeMilestoneMenu_successful() {
        click("#dummy/dummy_col0_9");
        rightClick("#dummy/dummy_col0_9");
        sleep(EVENT_DELAY);
        click("Change milestone (M)");
        sleep(DIALOG_DELAY);

        assertNotNull(find("#milestonePickerTextField"));

        push(KeyCode.ESCAPE);
        sleep(EVENT_DELAY);
    }

    /**
     * Tests selecting "Close issue" and "Reopen issue"
     */
    @Test
    public void testCloseReopenIssue() {
        click("#dummy/dummy_col0_9");
        rightClick("#dummy/dummy_col0_9");
        sleep(EVENT_DELAY);
        click("Close issue (C)");
        sleep(EVENT_DELAY);
        waitUntilNodeAppears("OK");
        click("OK");
        sleep(EVENT_DELAY);
        waitUntilNodeAppears("Undo");
        click("Undo");
        sleep(EVENT_DELAY);

        click("#dummy/dummy_col0_6");
        rightClick("#dummy/dummy_col0_6");
        sleep(EVENT_DELAY);
        click("Reopen issue (O)");
        sleep(EVENT_DELAY);
        waitUntilNodeAppears("OK");
        click("OK");
        sleep(EVENT_DELAY);
        waitUntilNodeAppears("Undo");
        click("Undo");
        sleep(EVENT_DELAY);
    }

}

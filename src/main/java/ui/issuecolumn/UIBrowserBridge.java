package ui.issuecolumn;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import ui.UI;
import util.TickingTimer;
import util.events.IssueCreatedEventHandler;
import util.events.IssueSelectedEventHandler;
import util.events.LabelCreatedEventHandler;
import util.events.MilestoneCreatedEventHandler;

/**
 * A abstract component in charge of creating, displaying, and enabling edits of issues.
 * Its function is to decouple the UI logic and events from the BrowserComponent.
 * In this sense it is similar in function to ui.issuepanel.expanded.IssueCommentsDisplay.
 *
 * Unlike the aforementioned component is owned by the ColumnControl, which is not recreated
 * on issue selection. Thus its constructor does not contain a reference to a particular issue.
 * Instead it takes ownership of issues via events.
 */
public class UIBrowserBridge {

    private static final int BROWSER_REQUEST_DELAY = 400; //milliseconds
    private TickingTimer timer;
    private Optional<String> nextRepoId = Optional.empty();
    private Optional<Integer> nextIssueId = Optional.empty();

    public UIBrowserBridge(UI ui) {
        timer = createTickingTimer(ui);
        timer.start();

        ui.registerEvent((IssueSelectedEventHandler) e -> {
            nextRepoId = Optional.of(e.repoId);
            nextIssueId = Optional.of(e.id);
            timer.restart();
            if (timer.isPaused()) {
                timer.resume();
            }
        });

        ui.registerEvent((IssueCreatedEventHandler) e ->
            ui.getBrowserComponent().newIssue());

        ui.registerEvent((LabelCreatedEventHandler) e ->
            ui.getBrowserComponent().newLabel());

        ui.registerEvent((MilestoneCreatedEventHandler) e ->
            ui.getBrowserComponent().newMilestone());
    }

    private TickingTimer createTickingTimer(UI ui) {
        return new TickingTimer("Browser Request Delay Timer", BROWSER_REQUEST_DELAY, integer -> {
            // do nothing for each tick
        }, () -> {
            Platform.runLater(() -> {
                if (nextRepoId.isPresent() && nextIssueId.isPresent()) {
                    ui.getBrowserComponent().showIssue(nextRepoId.get(), nextIssueId.get());
                }
            });
            timer.pause();
        }, TimeUnit.MILLISECONDS);
    }
}
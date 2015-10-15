package undo;

import backend.resource.TurboIssue;

import java.util.List;

public class Undoable {

    public final TurboIssue issue;
    private List<Action> actions; // mutable, actions can be added or removed

    public Undoable(TurboIssue issue, List<Action> actions) {
        this.issue = issue;
        this.actions = actions;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void removeAction (Action action) {
        actions.remove(action);
    }

}

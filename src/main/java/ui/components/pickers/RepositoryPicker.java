package ui.components.pickers;

import javafx.application.Platform;
import javafx.stage.Stage;
import ui.UI;
import util.events.ShowRepositoryPickerEventHandler;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * This class represents an entry point to RepositoryPicker by handling operations related to RepositoryPickerDialog.
 */
public class RepositoryPicker {

    private final UI ui;
    private final Stage stage;
    private final Consumer<String> onValueChangeCallback;

    public RepositoryPicker(UI ui, Stage stage, Consumer<String> onValueChangeCallback) {
        this.ui = ui;
        this.stage = stage;
        this.onValueChangeCallback = onValueChangeCallback;
        ui.registerEvent((ShowRepositoryPickerEventHandler) e -> Platform.runLater(() -> showRepositoryPicker()));
    }

    private void showRepositoryPicker() {
        Set<String> storedRepos = ui.logic.getStoredRepos();
        String defaultRepo = ui.logic.getDefaultRepo();
        RepositoryPickerDialog dialog = new RepositoryPickerDialog(storedRepos, stage);
        Optional<String> chosenRepo = dialog.showAndWait();
        if (chosenRepo.isPresent() && !defaultRepo.equals(chosenRepo.get())) {
            onValueChangeCallback.accept(chosenRepo.get());
        }
    }

}

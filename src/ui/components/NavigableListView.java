package ui.components;

import java.util.Optional;
import java.util.function.IntConsumer;

import javafx.scene.input.KeyCode;

/**
 * A very specialized ListView subclass that:
 * 
 * - can be navigated with the arrow keys and Enter
 * - supports an event for item selection
 * - provides methods for retaining selection after its contents are changed
 * 
 * It depends on the functionality of ScrollableListView to ensure that
 * navigation scrolls the list properly. The Up, Down, and Enter key events
 * should not be separately bound on it.
 * 
 * An item is considered selected when:
 * 
 * - it is highlighted with the arrow keys, but only when the Shift key is not down
 * - Enter is pressed when it is highlighted
 * - it is clicked
 */
public class NavigableListView<T> extends ScrollableListView<T> {

	// Tracks the index of the list which should be currently selected
	private Optional<Integer> selectedIndex = Optional.empty();
	
	// Used for saving and restoring selection
	private Optional<T> selectedItem = Optional.empty();
	
	// Indicates that saveSelection was called, in the event that saveSelection itself fails
	// (when nothing is selected, both should be no-ops)
	private boolean saveSelectionCalled = false;

	private IntConsumer onItemSelected = i -> {};
	
	public NavigableListView() {
		setupKeyEvents();
		setupMouseEvents();
	}
	
	/**
	 * Should be called before making changes to the item list of this list view if
	 * it's important that selection is retained after.
	 */
	public void saveSelection() {
		if (getSelectionModel().getSelectedItem() != null) {
			selectedItem = Optional.of(getSelectionModel().getSelectedItem());
		}
		saveSelectionCalled = true;
	}
	
	/**
	 * Should be called to restore selection after making changes to the item list
	 * of this list view. Must be called after saveSelection is.
	 * @throws IllegalStateException if called before saveSelection is
	 */
	public void restoreSelection() {
		if (!selectedItem.isPresent()) {
			if (!saveSelectionCalled) {
				throw new IllegalStateException("saveSelection must be called before restoreSelection");
			} else {
				saveSelectionCalled = false;
				return; // No-op
			}
		}
		saveSelectionCalled = false;
		
		// Find index of previously-selected item
		int index = -1;
		int i = 0;
		for (T item : getItems()) {
			if (item.equals(selectedItem)) {
				index = i;
				break;
			}
			i++;
		}
		
		if (index == -1) {
			// The item disappeared; do nothing, as selection will be resolved on its own
		} else {
			// Select that item
			getSelectionModel().clearAndSelect(index);
			selectedIndex = Optional.of(index);
			// Do not trigger event
		}
	}

	private void setupMouseEvents() {
		setOnMouseClicked(e -> {
			selectedIndex = Optional.of(getSelectionModel().getSelectedIndex());
			onItemSelected.accept(selectedIndex.get());
		});
	}

	private void setupKeyEvents() {
		setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case UP:
			case DOWN:
				e.consume();
				handleUpDownKeys(e.getCode() == KeyCode.DOWN);
				assert selectedIndex.isPresent() : "handleUpDownKeys doesn't set selectedIndex!";
				if (!e.isShiftDown()) {
					onItemSelected.accept(selectedIndex.get());
				}
				break;
			case ENTER:
				e.consume();
				if (selectedIndex.isPresent()) {
					onItemSelected.accept(selectedIndex.get());	
				}
				break;
			default:
				break;
			}
		});
	}
	
	private void handleUpDownKeys(boolean isDownKey) {
		
		// Nothing is selected or the list is empty; do nothing
		if (!selectedIndex.isPresent()) return;
		if (getItems().size() == 0) return;
		
		// Compute new index and clamp it within range
		int newIndex = selectedIndex.get() + (isDownKey ? 1 : -1);
		newIndex = Math.min(Math.max(0, newIndex), getItems().size()-1);
		
		// Update selection state and our selection model
		getSelectionModel().clearAndSelect(newIndex);
		selectedIndex = Optional.of(newIndex);
		
		// Ensure that the newly-selected item is in view
		scrollAndShow(newIndex);
	}

	public void setOnItemSelected(IntConsumer callback) {
		onItemSelected = callback;
	}
}
package tests;

import org.junit.Test;

import backend.resource.TurboLabel;
import ui.components.pickers.LabelPickerState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LabelPickerStateTests {

    @Test
    public void determineState_addMatchedLabels() {
        LabelPickerState state = setupState("f-aa p.high ", "");

        assertEquals(2, state.getAddedLabels().size());
    }

    @Test
    public void determineState_removeMatchedAddedLabel() {
        LabelPickerState state = setupState("p.medium ", "priority.medium");

        assertEquals(0, state.getAddedLabels().size());
    }

    @Test
    public void determineState_invalidQuery_noChangeToState() {
        LabelPickerState state = setupState("       ", "test");

        assertEquals(0, state.getAddedLabels().size());
    }

    @Test
    public void determineState_exclusiveLabels_removeConflictingLabels() {
        LabelPickerState state = setupState("p.medium ", "priority.low");
        assertEquals(1, state.getInitialLabels().size());
        assertEquals("priority.medium", state.getAddedLabels().get(0));
        assertEquals(1, state.getAddedLabels().size());
        assertEquals(1, state.getRemovedLabels().size());
        assertTrue(state.getRemovedLabels().contains("priority.low"));
    }

    @Test
    public void determineState_labelsInSameGroup_oneLabelAssigned() {
        LabelPickerState state = setupState("p.medium ", "priority.low", "priority.high");

        assertEquals(1, state.getAssignedLabels().size());
    }


    public LabelPickerState setupState(String userInput, String... labelNames) {
        return new LabelPickerState(getHashSet(labelNames), getTestRepoLabels(), userInput);
    }

    public List<TurboLabel> getTestRepoLabels() {
        List<String> labelNames = getArrayList("priority.high", "priority.medium", "priority.low", 
                                               "highest", "Problem.Heavy", "f-aaa", "f-bbb");

        return labelNames.stream().map(name -> new TurboLabel("", name)).collect(Collectors.toList());
    }

    public Set<String> getHashSet(String... labelNames) {
        Set<String> setOfLabelNames = new HashSet<>();
        for (String labelName : labelNames) {
            setOfLabelNames.add(labelName);
        }

        return setOfLabelNames;
    }

    public List<String> getArrayList(String... labelNames) {
        List<String> listOfLabelNames = new ArrayList<>();
        for (String labelName : labelNames) {
            listOfLabelNames.add(labelName);
        }

        return listOfLabelNames;
    }

}

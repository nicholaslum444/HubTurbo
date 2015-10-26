package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import util.GithubURLPageElements;

public class GithubURLPageElementsTest {
    @Test
    public void gitHubURLPageElementTest() {
        assertEquals("123", GithubURLPageElements.extractIssueNumber("fixes #123").get());
        assertEquals("123", GithubURLPageElements.extractIssueNumber("Closed #123. This is dummy").get());
        assertEquals("123", GithubURLPageElements.extractIssueNumber("refer to #144. Closed #123. This is du").get());
        assertNotEquals("131", GithubURLPageElements.extractIssueNumber("fixes #123").get());
    }
}

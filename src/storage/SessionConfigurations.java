package storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.egit.github.core.IRepositoryIdProvider;

/**
 * Abstractions for the contents of the session file.
 */
public class SessionConfigurations {
	private HashMap<String, List<String>> projectFilters;
	private List<RepoViewRecord> lastViewedRepositories; 
	
	public SessionConfigurations() {
		projectFilters = new HashMap<>();
		lastViewedRepositories = new ArrayList<>();
	}
	
	public SessionConfigurations(HashMap<String, List<String>> projectFilters,
			List<RepoViewRecord> lastViewedRepositories) {
		this.projectFilters = projectFilters;
		this.lastViewedRepositories = lastViewedRepositories;
	}
	
	public void setFiltersForNextSession(IRepositoryIdProvider project, List<String> filter) {
		if (project != null) {
			projectFilters.put(project.generateId(), filter);
		}
	}
	
	public List<String> getFiltersFromPreviousSession(IRepositoryIdProvider project) {
		if (project == null) {
			return new ArrayList<>();
		}
		return projectFilters.get(project.generateId());
	}
	
	/**
	 * Adds a repository to the list of last-viewed repositories.
	 * The list will always have 10 or fewer items.
	 */
	public void addToLastViewedRepositories(String repository) {
		// Create record for this repository
		RepoViewRecord latestRepoView = new RepoViewRecord(repository);
		int index = lastViewedRepositories.indexOf(latestRepoView);
		if (index < 0) {
			lastViewedRepositories.add(latestRepoView);
		} else {
			lastViewedRepositories.get(index).setTimestamp(latestRepoView.getTimestamp());
		}
		
		// Keep only the 10 latest records
		Collections.sort(lastViewedRepositories);
		while (lastViewedRepositories.size() > 10) {
			lastViewedRepositories.remove(lastViewedRepositories.size() - 1);
		}
		assert lastViewedRepositories.size() <= 10;
	}
	
	/**
	 * Returns last-viewed repositories in owner/name format.
	 */
	public List<String> getLastViewedRepositories() {
		return lastViewedRepositories.stream()
				.map(repoViewRecord -> repoViewRecord.getRepository())
				.collect(Collectors.toList());
	}
}

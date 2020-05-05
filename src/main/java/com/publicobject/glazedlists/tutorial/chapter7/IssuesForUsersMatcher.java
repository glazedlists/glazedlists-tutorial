package com.publicobject.glazedlists.tutorial.chapter7;

import ca.odell.glazedlists.matchers.Matcher;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ca.odell.issuezilla.Issue;

/**
 * This {@link Matcher} only matches users in a predefined set.
 *
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
public class IssuesForUsersMatcher implements Matcher<Issue> {

  /** the users to match */
  private Set<String> users = new HashSet<>();

  /**
   * Create a new {@link IssuesForUsersMatcher} that matches only {@link Issue}s that have one or
   * more user in the specified list.
   */
  public IssuesForUsersMatcher(Collection<String> users) {
    // defensive copy all the users
    this.users.addAll(users);
  }

  /**
   * Test whether to include or not include the specified issue based on whether or not their user
   * is selected.
   */
  @Override
  public boolean matches(Issue issue) {
    if (issue == null)
      return false;
    if (users.isEmpty())
      return true;

    String user = issue.getReporter();
    return users.contains(user);
  }
}

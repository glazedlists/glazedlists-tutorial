package com.publicobject.glazedlists.tutorial.chapter4;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.issuezilla.Issue;

/**
 * An IssuesToUserList is a list of users that is obtained by getting the users from an issues list.
 *
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
public class IssueToUserList extends TransformedList<Issue, String> { // <1>

  /**
   * Construct an IssuesToUserList from an EventList that contains only Issue objects.
   */
  public IssueToUserList(EventList<Issue> source) {
    super(source);
    source.addListEventListener(this); // <2>
  }

  /**
   * Gets the user at the specified index.
   */
  public String get(int index) { // <3>
    Issue issue = source.get(index);
    return issue.getReporter();
  }

  /**
   * When the source issues list changes, propogate the exact same changes for the users list.
   */
  public void listChanged(ListEvent<Issue> listChanges) {
    updates.forwardEvent(listChanges); // <4>
  }

  /** {@inheritDoc} */
  protected boolean isWritable() { // <5>
    return false;
  }
}

package com.publicobject.glazedlists.tutorial.chapter4;

import java.util.Comparator;

import ca.odell.issuezilla.Issue;

/**
 * Compare issues by priority.
 * 
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
public class IssueComparator implements Comparator<Issue> {
  public int compare(Issue issueA, Issue issueB) {

    // rating is between 1 and 5, lower is more important
    int issueAValue = issueA.getPriority().getValue();
    int issueBValue = issueB.getPriority().getValue();

    return issueAValue - issueBValue;
  }
}

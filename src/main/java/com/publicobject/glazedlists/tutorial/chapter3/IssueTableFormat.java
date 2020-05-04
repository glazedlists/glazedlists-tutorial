package com.publicobject.glazedlists.tutorial.chapter3;

import ca.odell.glazedlists.gui.TableFormat;

import ca.odell.issuezilla.Issue;

/**
 * Display issues in a tabular form.
 *
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
public class IssueTableFormat implements TableFormat<Issue> {

  @Override
  public int getColumnCount() {
    return 6;
  }

  @Override
  public String getColumnName(int column) {
    switch (column) {
    case 0:
      return "ID";
    case 1:
      return "Type";
    case 2:
      return "Priority";
    case 3:
      return "State";
    case 4:
      return "Result";
    case 5:
      return "Summary";
    }
    throw new IllegalStateException("Unexpected column: " + column);
  }

  @Override
  public Object getColumnValue(Issue issue, int column) {
    switch (column) {
    case 0:
      return issue.getId();
    case 1:
      return issue.getIssueType();
    case 2:
      return issue.getPriority();
    case 3:
      return issue.getStatus();
    case 4:
      return issue.getResolution();
    case 5:
      return issue.getShortDescription();
    }
    throw new IllegalStateException("Unexpected column: " + column);
  }
}

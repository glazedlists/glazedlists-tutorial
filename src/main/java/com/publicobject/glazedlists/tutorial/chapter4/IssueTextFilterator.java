package com.publicobject.glazedlists.tutorial.chapter4;

import java.util.List;

import ca.odell.glazedlists.TextFilterator;
import ca.odell.issuezilla.Issue;

/**
 * Get the Strings to filter against for a given Issue.
 * 
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
public class IssueTextFilterator implements TextFilterator<Issue> {
    public void getFilterStrings(List<String> baseList, Issue issue) {
        baseList.add(issue.getComponent());
        baseList.add(issue.getIssueType());
        baseList.add(issue.getOperatingSystem());
        baseList.add(issue.getResolution());
        baseList.add(issue.getShortDescription());
        baseList.add(issue.getStatus());
        baseList.add(issue.getSubcomponent());
        baseList.add(issue.getVersion());
    }
}

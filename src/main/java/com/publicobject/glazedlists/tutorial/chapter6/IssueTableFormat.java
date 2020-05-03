package com.publicobject.glazedlists.tutorial.chapter6;

import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.issuezilla.Issue;

/**
 * Display issues in a tabular form.
 * 
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
public class IssueTableFormat implements TableFormat<Issue> {
    
    public int getColumnCount() {
        return 6;
    }
    
    public String getColumnName(int column) {
        if(column == 0)      return "ID";
        else if(column == 1) return "Type";
        else if(column == 2) return "Priority";
        else if(column == 3) return "State";
        else if(column == 4) return "Result";
        else if(column == 5) return "Summary";

        throw new IllegalStateException();
    }
    
    public Object getColumnValue(Issue issue, int column) {

        if(column == 0)      return issue.getId();
        else if(column == 1) return issue.getIssueType();
        else if(column == 2) return issue.getPriority();
        else if(column == 3) return issue.getStatus();
        else if(column == 4) return issue.getResolution();
        else if(column == 5) return issue.getShortDescription();

        throw new IllegalStateException();
    }
}

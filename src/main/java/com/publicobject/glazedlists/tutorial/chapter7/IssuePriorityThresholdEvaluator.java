package com.publicobject.glazedlists.tutorial.chapter7;

import ca.odell.glazedlists.ThresholdList;
import ca.odell.issuezilla.Issue;

/**
 * Evaluates an issue by returning its threshold value.
 * 
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
public class IssuePriorityThresholdEvaluator implements ThresholdList.Evaluator<Issue> {
    public int evaluate(Issue issue) {
        
        // rating is between 1 and 5, lower is more important
        int issueRating = issue.getPriority().getValue();
        
        // flip: now rating is between 1 and 5, higher is more important
        int inverseRating = 6 - issueRating;
        return inverseRating;
    }
}

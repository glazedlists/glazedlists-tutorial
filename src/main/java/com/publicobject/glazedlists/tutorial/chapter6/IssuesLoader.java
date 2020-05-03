package com.publicobject.glazedlists.tutorial.chapter6;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.issuezilla.Issue;
import ca.odell.issuezilla.IssuezillaXMLParser;
import ca.odell.issuezilla.IssuezillaXMLParserHandler;

/**
 * Loads issues on a background thread.
 * 
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
public class IssuesLoader implements Runnable, IssuezillaXMLParserHandler {

    /** the issues list */
    private EventList<Issue> issues = new BasicEventList<Issue>();
    
    /** where issues shall be found */
    private String file;
    
    /**
     * Get the list that issues are being loaded into.
     */
    public EventList<Issue> getIssues() {
        return issues;
    }
    
    /**
     * Load the issues from the specified file.
     */
    public void load(String file) {
        this.file = file;
        
        // start a background thread
        Thread backgroundThread = new Thread(this);
        backgroundThread.setName("Issues from " + file);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }
    
    /**
     * When run, this fetches the issues from the issues URL and refreshes
     * the issues list.
     */
    public void run() {
        // load some issues
        try {
            IssuezillaXMLParser parser = new IssuezillaXMLParser();
            InputStream issuesInStream = new FileInputStream(file);
            parser.loadIssues(issuesInStream, this);
            issuesInStream.close();
        } catch(IOException e) {
            e.printStackTrace();
            return;
        }
    }
    
    /**
     * Handles a loaded issue.
     */
    public void issueLoaded(Issue issue) {
        issues.getReadWriteLock().writeLock().lock();
        try {
            issues.add(issue);
        } finally {
            issues.getReadWriteLock().writeLock().unlock();
        }
    }
}

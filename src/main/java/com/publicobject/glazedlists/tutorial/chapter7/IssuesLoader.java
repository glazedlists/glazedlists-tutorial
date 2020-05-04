package com.publicobject.glazedlists.tutorial.chapter7;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

import java.io.IOException;
import java.io.InputStream;

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

  /**
   * Get the list that issues are being loaded into.
   */
  public EventList<Issue> getIssues() {
    return issues;
  }

  /**
   * Load the issues.
   */
  public void load() {
    // start a background thread
    Thread backgroundThread = new Thread(this);
    backgroundThread.setName("Issues from resource");
    backgroundThread.setDaemon(true);
    backgroundThread.start();
  }

  /**
   * When run, this fetches the issues from the issues URL and refreshes the issues list.
   */
  @Override
  public void run() {
    // load some issues
    IssuezillaXMLParser parser = new IssuezillaXMLParser();
    try (InputStream inputStream = IssuesLoader.class.getResourceAsStream("/issues.xml")) {
      parser.loadIssues(inputStream, this);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
  }

  /**
   * Handles a loaded issue.
   */
  @Override
  public void issueLoaded(Issue issue) {
    issues.getReadWriteLock().writeLock().lock();
    try {
      issues.add(issue);
    } finally {
      issues.getReadWriteLock().writeLock().unlock();
    }
  }
}

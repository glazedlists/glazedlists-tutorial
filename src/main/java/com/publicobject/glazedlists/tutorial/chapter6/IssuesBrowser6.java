package com.publicobject.glazedlists.tutorial.chapter6;

import static ca.odell.glazedlists.swing.GlazedListsSwing.eventTableModelWithThreadProxyList;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.AdvancedTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

import com.publicobject.glazedlists.tutorial.chapter2.IssueTableFormat;
import com.publicobject.glazedlists.tutorial.chapter7.IssueTextFilterator;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import ca.odell.issuezilla.Issue;

import com.raelity.lib.ui.Screens;

/**
 * An IssueBrowser is a program for finding and viewing issues.
 *
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
public class IssuesBrowser6 {

  /** reads issues from a stream and populates the issues event list */
  private IssuesLoader issueLoader = new IssuesLoader();

  /** event list that hosts the issues */
  private EventList<Issue> issuesEventList = issueLoader.getIssues();

  /**
   * Create an IssueBrowser for the specified issues.
   */
  public IssuesBrowser6() {
    issueLoader.load();
  }

  /**
   * Display a frame for browsing issues. This should only be run on the Swing event dispatch
   * thread.
   */
  public void display() {
    issuesEventList.getReadWriteLock().readLock().lock(); // <1>
    try {
      // create the transformed models
      SortedList<Issue> sortedIssues = new SortedList<>(issuesEventList, new IssueComparator());
      UsersSelect usersSelect = new UsersSelect(sortedIssues);
      FilterList<Issue> userFilteredIssues = new FilterList<>(sortedIssues, usersSelect);
      JTextField filterEdit = new JTextField(10);
      IssueTextFilterator filterator = new IssueTextFilterator();
      MatcherEditor<Issue> textMatcherEditor = new TextComponentMatcherEditor<>(filterEdit, filterator);
      FilterList<Issue> textFilteredIssues = new FilterList<>(userFilteredIssues, textMatcherEditor);

      // create the issues table
      AdvancedTableModel<Issue> tableModel = eventTableModelWithThreadProxyList(
          textFilteredIssues, new IssueTableFormat());
      JTable issuesJTable = new JTable(tableModel);
      TableComparatorChooser.install(issuesJTable, sortedIssues, TableComparatorChooser.MULTIPLE_COLUMN_MOUSE);
      JScrollPane issuesTableScrollPane = new JScrollPane(issuesJTable);

      // create the users list
      JScrollPane usersListScrollPane = new JScrollPane(usersSelect.getJList());

      // create the panel
      JPanel panel = new JPanel();
      panel.setLayout(new GridBagLayout());
      panel.add(new JLabel("Filter: "),      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
          GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      panel.add(filterEdit,                  new GridBagConstraints(0, 1, 1, 1, 0.15, 0.0,
          GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      panel.add(new JLabel("Reported By: "), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
          GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      panel.add(usersListScrollPane,         new GridBagConstraints(0, 3, 1, 1, 0.15, 1.0,
          GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      panel.add(issuesTableScrollPane,       new GridBagConstraints(1, 0, 1, 4, 0.85, 1.0,
          GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

      // create a frame with that panel
      JFrame frame = new JFrame("Issues");
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      frame.setSize(540, 380);
      frame.getContentPane().add(panel);
      Screens.translateToPrefScreen(frame);
      frame.setVisible(true);
    } finally {
      issuesEventList.getReadWriteLock().readLock().unlock();
    }
  }

  /**
   * Launch the IssuesBrowser from the commandline.
   */
  public static void main(String[] args) {
    // create the browser and start loading issues
    final IssuesBrowser6 browser = new IssuesBrowser6();

    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() { // <2>
      @Override
      public void run() {
        browser.display();
      }
    });

  }
}

package com.publicobject.glazedlists.tutorial.chapter2;

import static ca.odell.glazedlists.swing.GlazedListsSwing.eventTableModelWithThreadProxyList;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.AdvancedTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import ca.odell.issuezilla.Issue;
import ca.odell.issuezilla.IssuezillaXMLParser;

import com.raelity.lib.ui.Screens;

/**
 * An IssueBrowser is a program for finding and viewing issues.
 *
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
public class IssuesBrowser2 {

  /** event list that hosts the issues */
  private EventList<Issue> issuesEventList = new BasicEventList<>();

  /**
   * Create an IssueBrowser for the specified issues.
   */
  public IssuesBrowser2(Collection<Issue> issues) {
    issuesEventList.addAll(issues);
  }

  // tag::Display[]
  /**
   * Display a frame for browsing issues.
   */
  public void display() {
    SortedList<Issue> sortedIssues = new SortedList<>(issuesEventList, new IssueComparator());// <1>

    // create a panel with a table
    JPanel panel = new JPanel(new GridBagLayout());
    AdvancedTableModel<Issue> tableModel = eventTableModelWithThreadProxyList(
        sortedIssues, new IssueTableFormat()); // <2>
    JTable issuesJTable = new JTable(tableModel); // <3>
    TableComparatorChooser.install(issuesJTable, sortedIssues, TableComparatorChooser.MULTIPLE_COLUMN_MOUSE); // <4>
    JScrollPane issuesTableScrollPane = new JScrollPane(issuesJTable);
    panel.add(issuesTableScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    // create a frame with that panel
    JFrame frame = new JFrame("Issues");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(540, 380);
    frame.getContentPane().add(panel);
    Screens.translateToPrefScreen(frame);
    frame.setVisible(true);
  }
  // end::Display[]

  /**
   * Launch the IssuesBrowser from the commandline.
   */
  public static void main(String[] args) {
    // load some issues
    final Collection<Issue> issues;
    IssuezillaXMLParser parser = new IssuezillaXMLParser();
    try (InputStream inputStream = IssuesBrowser2.class.getResourceAsStream("/issues.xml")) {
      issues = parser.loadIssues(inputStream, null);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        // create the browser
        IssuesBrowser2 browser = new IssuesBrowser2(issues);
        browser.display();
      }
    });
  }
}

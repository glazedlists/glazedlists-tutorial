package com.publicobject.glazedlists.tutorial.chapter1;

import static ca.odell.glazedlists.swing.GlazedListsSwing.eventListModelWithThreadProxyList;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.DefaultEventListModel;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
public class IssuesBrowser1 {

  /** event list that hosts the issues */
  private EventList<Issue> issuesEventList = new BasicEventList<>(); // <1>

  /**
   * Create an IssueBrowser for the specified issues.
   */
  public IssuesBrowser1(Collection<Issue> issues) {
    issuesEventList.addAll(issues); // <2>
  }

  /**
   * Display a frame for browsing issues.
   */
  public void display() {
    JPanel panel = new JPanel(new GridBagLayout());
    DefaultEventListModel<Issue> listModel = eventListModelWithThreadProxyList(issuesEventList);// <3>
    JList<Issue> issuesJList = new JList<>(listModel); // <4>
    JScrollPane issuesListScrollPane = new JScrollPane(issuesJList);
    panel.add(issuesListScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    // create a frame with that panel
    JFrame frame = new JFrame("Issues");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(540, 380);
    frame.getContentPane().add(panel);
    Screens.translateToPrefScreen(frame);
    frame.setVisible(true);
  }

  /**
   * Launch the IssuesBrowser from the commandline.
   */
  public static void main(String[] args) {
    // load some issues
    final Collection<Issue> issues;
    IssuezillaXMLParser parser = new IssuezillaXMLParser();
    try (InputStream inputStream = IssuesBrowser1.class.getResourceAsStream("/issues.xml")) {
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
        IssuesBrowser1 browser = new IssuesBrowser1(issues);
        browser.display();
      }
    });
  }
}

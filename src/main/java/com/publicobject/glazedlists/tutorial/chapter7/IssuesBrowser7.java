package com.publicobject.glazedlists.tutorial.chapter7;

import static ca.odell.glazedlists.swing.GlazedListsSwing.eventTableModelWithThreadProxyList;
import static ca.odell.glazedlists.swing.GlazedListsSwing.lowerRangeModel;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.ThresholdList;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.AdvancedTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

import com.publicobject.glazedlists.tutorial.chapter2.IssueTableFormat;

import javax.swing.BoundedRangeModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Hashtable;

import ca.odell.issuezilla.Issue;

import com.raelity.lib.ui.Screens;

/**
 * An IssueBrowser is a program for finding and viewing issues.
 *
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
public class IssuesBrowser7 {

  /** reads issues from a stream and populates the issues event list */
  private IssuesLoader issueLoader = new IssuesLoader();

  /** event list that hosts the issues */
  private EventList<Issue> issuesEventList = issueLoader.getIssues();

  /**
   * Create an IssueBrowser for the issues.
   */
  public IssuesBrowser7() {
    issueLoader.load();
  }

  /**
   * Display a frame for browsing issues. This should only be run on the Swing event dispatch
   * thread.
   */
  public void display() {
    JList<String> userSelectList;
    JTextField filterEdit;
    ThresholdList<Issue> priorityFilteredIssues;
    JScrollPane issuesTableScrollPane;
    // lock while creating the transformed models
    issuesEventList.getReadWriteLock().readLock().lock();
    try {
      UsersSelect usersSelect = new UsersSelect(issuesEventList);
      userSelectList = usersSelect.getJList();
      FilterList<Issue> userFilteredIssues = new FilterList<>(issuesEventList, usersSelect);
      filterEdit = new JTextField(10);
      IssueTextFilterator filterator = new IssueTextFilterator();
      MatcherEditor<Issue> textMatcherEditor = new TextComponentMatcherEditor<>(filterEdit, filterator);
      FilterList<Issue> textFilteredIssues = new FilterList<>(userFilteredIssues, textMatcherEditor);

      IssuePriorityThresholdEvaluator evaluator = new IssuePriorityThresholdEvaluator();
      priorityFilteredIssues = new ThresholdList<>(textFilteredIssues, evaluator);

      SortedList<Issue> sortedIssues = new SortedList<>(priorityFilteredIssues, new IssueComparator());

      // create the issues table
      AdvancedTableModel<Issue> tableModel = eventTableModelWithThreadProxyList(
          sortedIssues, new IssueTableFormat());
      JTable issuesJTable = new JTable(tableModel);
      TableComparatorChooser.install(issuesJTable, sortedIssues, TableComparatorChooser.MULTIPLE_COLUMN_MOUSE);
      issuesTableScrollPane = new JScrollPane(issuesJTable);
    } finally {
      issuesEventList.getReadWriteLock().readLock().unlock();
    }
    // tag::ThresholdSlider[]
    // create the threshold slider
    // range model handles locking itself
    BoundedRangeModel priorityFilterRangeModel = lowerRangeModel(priorityFilteredIssues);
    priorityFilterRangeModel.setRangeProperties(1, 0, 1, 5, false);
    JSlider priorityFilterSlider = new JSlider(priorityFilterRangeModel);
    Hashtable<Integer, JLabel> priorityFilterSliderLabels = new Hashtable<>();
    priorityFilterSliderLabels.put(new Integer(1), new JLabel("Low"));
    priorityFilterSliderLabels.put(new Integer(5), new JLabel("High"));
    priorityFilterSlider.setLabelTable(priorityFilterSliderLabels);
    priorityFilterSlider.setMajorTickSpacing(1);
    priorityFilterSlider.setSnapToTicks(true);
    priorityFilterSlider.setPaintLabels(true);
    priorityFilterSlider.setPaintTicks(true);
    // end::ThresholdSlider[]

    // create the users list
    JScrollPane usersListScrollPane = new JScrollPane(userSelectList);

    // create the panel
    JPanel panel = new JPanel(new GridBagLayout());
    panel.add(new JLabel("Filter: "),           new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 0));
    panel.add(filterEdit, new GridBagConstraints(0, 1, 1, 1, 0.15, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 5, 10, 5), 0, 0));
    panel.add(new JLabel("Minimum Priority: "), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 0));
    panel.add(priorityFilterSlider,             new GridBagConstraints(0, 3, 1, 1, 0.15, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 5, 10, 5), 0, 0));
    panel.add(new JLabel("Reported By: "),      new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 0));
    panel.add(usersListScrollPane,              new GridBagConstraints(0, 5, 1, 1, 0.15, 1.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 5, 5, 5), 0, 0));
    panel.add(issuesTableScrollPane,            new GridBagConstraints(1, 0, 1, 6, 0.85, 1.0,
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
    // create the browser and start loading issues
    final IssuesBrowser7 browser = new IssuesBrowser7();

    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        browser.display();
      }
    });
  }
}

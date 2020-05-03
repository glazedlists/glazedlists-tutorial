package com.publicobject.glazedlists.tutorial.chapter5;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.UniqueList;
import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import ca.odell.glazedlists.matchers.Matcher;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.AdvancedListSelectionModel;
import ca.odell.glazedlists.swing.DefaultEventListModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import ca.odell.issuezilla.Issue;

/**
 * This {@link MatcherEditor} matches issues if their user is selected.
 *
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 */
public class UsersSelect extends AbstractMatcherEditor<Issue> implements ListSelectionListener {

    /** a list of users */
    EventList<String> usersEventList;
    EventList<String> usersSelectedList;

    /** a widget for selecting users */
    JList usersJList;

    /**
     * Create a {@link IssuesForUsersMatcherEditor} that matches users from the
     * specified {@link EventList} of {@link Issue}s.
     */
    public UsersSelect(EventList<Issue> source) {
        // derive the users list from the issues list
        EventList<String> usersNonUnique = new IssueToUserList(source);
        usersEventList = new UniqueList<String>(usersNonUnique);

        // create a JList that contains users
        DefaultEventListModel<String> usersListModel = GlazedListsSwing.eventListModelWithThreadProxyList(usersEventList);
        usersJList = new JList(usersListModel);

        // create an EventList containing the JList's selection
        AdvancedListSelectionModel<String> userSelectionModel = GlazedListsSwing.eventSelectionModelWithThreadProxyList(usersEventList);
        usersJList.setSelectionModel(userSelectionModel);
        usersSelectedList = userSelectionModel.getSelected();

        // handle changes to the list's selection
        usersJList.addListSelectionListener(this);
    }

    /**
     * Get the widget for selecting users.
     */
    public JList getJList() {
        return usersJList;
    }

    /**
     * When the JList selection changes, create a new Matcher and fire
     * an event.
     */
    public void valueChanged(ListSelectionEvent e) {
        Matcher<Issue> newMatcher = new IssuesForUsersMatcher(usersSelectedList);
        fireChanged(newMatcher);
    }
}

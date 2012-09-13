package org.jboss.essc.web.pages;

import javax.inject.Inject;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.resource.CssResourceReference;
import org.jboss.essc.web._cp.HeaderPanel;
import org.jboss.essc.web._cp.pageBoxes.RecentChangesBox;
import org.jboss.essc.web._cp.pageBoxes.RecentReleasesBox;
import org.jboss.essc.web.dao.ContactDao;


/**
 * Dynamic behavior for the ListContact page
 * 
 * @author Ondrej Zizka
 */
@SuppressWarnings("serial")
public class HomePage extends BaseLayoutPage {

    // Inject the ContactDao using @Inject
    @Inject
    private ContactDao contactDao;

    private static final int RECENT_RELEASES_ROWS = 6;
    private static final int RECENT_CHANGES_ROWS = 6;
    
    
    public HomePage() {

        add( new RecentReleasesBox("recentReleases", RECENT_RELEASES_ROWS) );
        
        add( new RecentChangesBox("recentChanges", RECENT_CHANGES_ROWS) );
    }
    
    
    /** Adds CSS reference. */
    public void renderHead(IHeaderResponse response) {
        //response.renderCSSReference(new PackageResourceReference(HomePage.class, "default/calendar.css"));
        response.renderCSSReference(new CssResourceReference( HomePage.class, "default.css" ));
    }

}

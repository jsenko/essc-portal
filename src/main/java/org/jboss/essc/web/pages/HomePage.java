package org.jboss.essc.web.pages;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.resource.CssResourceReference;
import org.jboss.essc.web._cp.pageBoxes.AboutSmallBox;
import org.jboss.essc.web._cp.pageBoxes.RecentChangesBox;
import org.jboss.essc.web._cp.pageBoxes.RecentReleasesBox;


/**
 * HomePage.
 * 
 * @author Ondrej Zizka
 */
@SuppressWarnings("serial")
public class HomePage extends BaseLayoutPage {

    private static final int RECENT_RELEASES_ROWS = 6;
    private static final int RECENT_CHANGES_ROWS = 6;
    
    
    public HomePage() {

        add( new AboutSmallBox("aboutBox") );
        
        add( new RecentReleasesBox("recentReleases", RECENT_RELEASES_ROWS) );
        
        add( new RecentChangesBox("recentChanges", RECENT_CHANGES_ROWS) );
    }
    
    
    /** Adds CSS reference. */
    public void renderHead(IHeaderResponse response) {
        //response.renderCSSReference(new PackageResourceReference(HomePage.class, "default/calendar.css"));
        response.renderCSSReference(new CssResourceReference( HomePage.class, "default.css" ));
    }

}// class

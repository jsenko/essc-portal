package org.jboss.essc.web.pages;

import javax.inject.Inject;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.resource.CssResourceReference;
import org.jboss.essc.web.dao.ContactDao;


/**
 * Dynamic behavior for the ListContact page
 * 
 * @author Ondrej Zizka
 */
@SuppressWarnings("serial")
public class ProjectPage extends BaseLayoutPage {

    // Inject the ContactDao using @Inject
    @Inject
    private ContactDao contactDao;

    
    public ProjectPage() {
        
    }
    
    
    /** Adds CSS reference. */
    public void renderHead(IHeaderResponse response) {
        //response.renderCSSReference(new PackageResourceReference(HomePage.class, "default/calendar.css"));
        response.renderCSSReference(new CssResourceReference( HomePage.class, "default.css" ));
    }

}

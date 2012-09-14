package org.jboss.essc.web.pages;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.CssResourceReference;
import org.jboss.essc.web._cp.HeaderPanel;
import org.jboss.essc.web._cp.SidebarPanel;


/**
 *  Base layout of all pages in this app.
 * 
 *  @author Ondrej Zizka
 */
public class BaseLayoutPage extends WebPage {


    // Set up the dynamic behavior for the page, widgets bound by id
    public BaseLayoutPage() {

        add( new HeaderPanel("header") );
        
        add( new SidebarPanel("sidebar") );
        
    }
    
    
    /** Adds CSS reference. */
    public void renderHead(IHeaderResponse response) {
        //response.renderCSSReference(new PackageResourceReference(HomePage.class, "default/calendar.css"));
        response.renderCSSReference(new CssResourceReference( BaseLayoutPage.class, "default.css" ));
    }

}// class

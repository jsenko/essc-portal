package org.jboss.essc.web.pages;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.CssResourceReference;
import org.jboss.essc.web._cp.pagePanes.HeaderPanel;
import org.jboss.essc.web._cp.pagePanes.SidebarPanel;
import org.jboss.essc.web.security.EsscAuthSession;


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
    
    
    /**
     *  Global helper to avoid casting everywhere.
     */
    public EsscAuthSession getSession(){
        return (EsscAuthSession) Session.get();
    }
    
    
    /** Adds CSS reference. */
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference(new CssResourceReference( BaseLayoutPage.class, "default.css" ));
    }

}// class

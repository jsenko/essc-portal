package org.jboss.essc.web._cp.links;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.jboss.essc.web.model.ProductRelease;
import org.jboss.essc.web.pages.ReleasePage;


/**
 * @author Ondrej Zizka
 */
public class ReleaseLink extends Panel {

    public ReleaseLink( String id, final ProductRelease rel ) {
        super( id );
        
        add( new Link("link") {
            @Override public void onClick() {
                setResponsePage(new ReleasePage(rel) );
            }
            { add( new Label("label", rel.getVersion()) ); }
        });
            
    }

}
package org.jboss.essc.web._cp.links;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jboss.essc.web.model.Release;
import org.jboss.essc.web.pages.rel.ReleasePage;


/**
 * @author Ondrej Zizka
 */
public class ReleaseLink extends Panel {

    public ReleaseLink( String id, final Release rel ) {
        super( id );
        setRenderBodyOnly(true);
        
        add( new BookmarkablePageLink("link", ReleasePage.class, 
                ReleasePage.createPageParameters( rel )
             ).add( new Label("label", rel.getVersion()) )
        );
    }

}// class

package org.jboss.essc.web._cp.links;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jboss.essc.web.model.Product;
import org.jboss.essc.web.pages.prod.ProductPage;


/**
 * @author Ondrej Zizka
 */
public class ProductLink extends Panel {

    public ProductLink( String id, final Product prod ) {
        super( id );
        setRenderBodyOnly(true);
        
        add( new BookmarkablePageLink("link", ProductPage.class, 
                new PageParameters().add("name", prod.getName())
             ).add( new Label("label", prod.getName()) ) 
        );
    }

}// class

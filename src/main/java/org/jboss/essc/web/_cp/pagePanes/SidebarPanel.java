package org.jboss.essc.web._cp.pagePanes;

import javax.inject.Inject;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.jboss.essc.web._cp.links.ProductLink;
import org.jboss.essc.web.dao.ProductDaoBean;
import org.jboss.essc.web.model.Product;


/**
 * @author Ondrej Zizka
 */
public class SidebarPanel extends Panel {

    @Inject private ProductDaoBean dao;
    
    
    public SidebarPanel( String id ) {
        super(id);
        this.setRenderBodyOnly( true );
        
        add( new ListView<Product>("projects", dao.getProducts_orderName(0) ) {
            @Override protected void populateItem( ListItem<Product> item ) {
                item.add(new ProductLink("link", item.getModelObject()) );
            }
        } );
    }

}// class

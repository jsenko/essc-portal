package org.jboss.essc.web._cp;

import javax.inject.Inject;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.jboss.essc.web._cp.links.ProductLink;
import org.jboss.essc.web.dao.ProductLineDaoBean;
import org.jboss.essc.web.model.ProductLine;


/**
 * @author Ondrej Zizka
 */
public class SidebarPanel extends Panel {

    @Inject private ProductLineDaoBean dao;
    
    
    public SidebarPanel( String id ) {
        super(id);
        this.setRenderBodyOnly( true );
        
        add( new ListView<ProductLine>("projects", dao.getProductLines_orderName(0) ) {
            @Override protected void populateItem( ListItem<ProductLine> item ) {
                item.add(new ProductLink("link", item.getModelObject()) );
            }
        } );
    }

}// class

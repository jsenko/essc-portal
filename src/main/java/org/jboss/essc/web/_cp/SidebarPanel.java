package org.jboss.essc.web._cp;

import javax.inject.Inject;
import org.apache.wicket.markup.html.panel.Panel;
import org.jboss.essc.web.dao.ProductReleaseDaoBean;
import org.jboss.essc.web.model.ProductLine;


/**
 * Contains a list of products. Appears on the home page.
 * 
 * @author Ondrej Zizka
 */
public class SidebarPanel extends Panel {

    @Inject private ProductReleaseDaoBean dao;
    
    
    public SidebarPanel( String id ) {
        super(id);
        this.setRenderBodyOnly( true );
    }// const
    
    

}
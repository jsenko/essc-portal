package org.jboss.essc.web._cp.pageBoxes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.inject.Inject;
import org.apache.wicket.markup.html.panel.Panel;
import org.jboss.essc.web.dao.ProductReleaseDaoBean;
import org.jboss.essc.web.model.ProductLine;


/**
 * Contains a list of products. Appears on the home page.
 * 
 * @author Ondrej Zizka
 */
public class StatusBox extends Panel {

    @Inject private ProductReleaseDaoBean dao;
    
    private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

    
    public StatusBox( String id, ProductLine line ) {
        super(id);
        
    }// const
    
    

}
package org.jboss.essc.web._cp.pageBoxes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.inject.Inject;
import org.apache.wicket.markup.html.panel.Panel;
import org.jboss.essc.web.dao.ReleaseDaoBean;
import org.jboss.essc.web.model.Product;


/**
 * Contains a list of products. Appears on the home page.
 * 
 * @author Ondrej Zizka
 */
public class AppStatusBox extends Panel {

    @Inject private ReleaseDaoBean dao;
    
    private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

    
    public AppStatusBox( String id, Product product ) {
        super(id);
        
    }// const
    
    

}
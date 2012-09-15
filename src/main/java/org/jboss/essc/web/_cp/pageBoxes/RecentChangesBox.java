package org.jboss.essc.web._cp.pageBoxes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.jboss.essc.web.dao.ProductReleaseDaoBean;
import org.jboss.essc.web.model.ProductRelease;


/**
 * A list of recent changes.
 * 
 * @author Ondrej Zizka
 */
public class RecentChangesBox extends Panel {

    @Inject private ProductReleaseDaoBean dao;
    
    private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

    private int numReleases = 6;
    
    
    
    public RecentChangesBox( String id, int numReleases ) {
        super(id);
        this.setRenderBodyOnly( true );
        
        this.numReleases = numReleases;
        
        add( new ListView<ProductRelease>("rows", dao.getProductReleases_orderDateDesc(this.numReleases)) {

            // Populate the table of contacts
            @Override
            protected void populateItem( final ListItem<ProductRelease> item) {
                ProductRelease pr = item.getModelObject();
                item.add( new Label("product", pr.getProduct().getName()));
                item.add( new Label("version", pr.getVersion()));
                Date date = pr.getLastChanged();
                item.add( new Label("changed", (date == null) ? "" : DF.format( date )));
                //item.add( new Label("state", pr.getStatus().name()));
                //item.add( new Label("gitHash", pr.getGitHash()));
                //item.add( new Label("parent", pr.getParent().getVersion()));
            }
        });
    }// const
    
    

}// class
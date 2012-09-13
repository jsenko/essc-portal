package org.jboss.essc.web._cp.pageBoxes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.jboss.essc.web.dao.ProductReleaseDaoBean;
import org.jboss.essc.web.model.ProductRelease;


/**
 * Contains a list of products. Appears on the home page.
 * 
 * @author Ondrej Zizka
 */
public class RecentReleasesBox extends Panel {

    @Inject private ProductReleaseDaoBean dao;
    
    private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

    private int numReleases = 6;
    
    
    
    public RecentReleasesBox( String id, int numReleases ) {
        super(id);
        this.setRenderBodyOnly( true );
        
        this.numReleases = numReleases;
        
        add( new ListView<ProductRelease>("rows", dao.getProductReleases_orderDateDesc(this.numReleases)) {

            // Populate the table of contacts
            @Override
            protected void populateItem( final ListItem<ProductRelease> item) {
                ProductRelease pr = item.getModelObject();
                item.add( new Label("project", pr.getLine().getName()));
                item.add( new Label("version", pr.getVersion()));
                item.add( new Label("planned", DF.format( pr.getPlannedFor() )));
                //item.add( new Label("state", pr.getStatus().name()));
                //item.add( new Label("gitHash", pr.getGitHash()));
                //item.add( new Label("parent", pr.getParent().getVersion()));
            }
        });
    }// const
    
    

}// class
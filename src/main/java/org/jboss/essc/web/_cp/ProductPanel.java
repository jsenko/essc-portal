package org.jboss.essc.web._cp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.jboss.essc.web.dao.ReleaseDaoBean;
import org.jboss.essc.web.model.Product;
import org.jboss.essc.web.model.Release;
import org.jboss.essc.web.pages.HomePage;


/**
 * Contains a list of products. Appears on the home page.
 * 
 * TODO: Rename to ProductsPanel or ProductsBox
 * 
 * @author Ondrej Zizka
 */
public class ProductPanel extends Panel {

    @Inject private ReleaseDaoBean dao;
    
    private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

    
    public ProductPanel( String id, Product product ) {
        super(id);
        
        add( new ListView<Release>("releaseList", dao.getProductReleasesOfLine(product)) {

            // Populate the table of contacts
            @Override
            protected void populateItem( final ListItem<Release> item) {
                Release pr = item.getModelObject();
                item.add( new Label("version", pr.getVersion()));
                item.add( new Label("planned", DF.format( pr.getPlannedFor() )));
                item.add( new Label("state", pr.getStatus().name()));
                item.add( new Label("gitHash", pr.getTraits().getGitHash()));
                item.add( new Link<Release>("delete", item.getModel()) {
                    @Override
                    public void onClick() {
                        dao.remove(item.getModelObject());
                        setResponsePage(HomePage.class);
                    }
                });
            }
        });
    }// const
    
    

}// class

package org.jboss.essc.web._cp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.inject.Inject;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.jboss.essc.web.dao.ProductReleaseDaoBean;
import org.jboss.essc.web.model.ProductLine;
import org.jboss.essc.web.model.ProductRelease;
import org.jboss.essc.web.pages.HomePage;


/**
 * Contains a list of products. Appears on the home page.
 * 
 * @author Ondrej Zizka
 */
public class ReleasesPanel extends Panel {

    @Inject private ProductReleaseDaoBean dao;
    
    private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
    
    private ProductLine product;

    
    public ReleasesPanel( String id, ProductLine product ) {
        super(id);
        this.product = product;
        
        //add( line == null ? new WebMarkupContainer("productCol").setVisible(false) : new  )
        add( new WebMarkupContainer("productCol").setVisible( isProductColVisible() ) );
        
        add( new ListView<ProductRelease>("releaseList", dao.getProductReleasesOfLine(product)) {

            // Populate the table of contacts
            @Override
            protected void populateItem( final ListItem<ProductRelease> item) {
                ProductRelease pr = item.getModelObject();
                item.add( new Label("product", pr.getProduct().getName()).setVisible( isProductColVisible() ) );
                item.add( new Label("version", pr.getVersion()));
                item.add( new Label("planned", DF.format( pr.getPlannedFor() )));
                item.add( new Label("state", pr.getStatus().name()));
                item.add( new Label("gitHash", pr.getGitHash()));
                item.add( new Link<ProductRelease>("delete", item.getModel()) {
                    @Override
                    public void onClick() {
                        dao.remove(item.getModelObject());
                        setResponsePage(HomePage.class);
                    }
                });
            }
        });
    }// const

    
    
    private boolean isProductColVisible() {
        return product != null;
    }

}// class
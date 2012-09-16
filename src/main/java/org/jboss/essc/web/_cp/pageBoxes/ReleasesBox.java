package org.jboss.essc.web._cp.pageBoxes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.jboss.essc.web._cp.links.ProductLink;
import org.jboss.essc.web._cp.links.ReleaseLink;
import org.jboss.essc.web.dao.ProductReleaseDaoBean;
import org.jboss.essc.web.model.ProductLine;
import org.jboss.essc.web.model.ProductRelease;


/**
 * A list of releases.
 * 
 * @author Ondrej Zizka
 */
public class ReleasesBox extends Panel {

    @Inject protected ProductReleaseDaoBean dao;
    
    private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

    protected int numReleases = 6;
    
    private ProductLine forProduct;
    
    
    public ReleasesBox( String id, int numReleases ) {
        this( id, null, numReleases );
    }
    
    
    public ReleasesBox( String id, ProductLine forProduct, int numReleases ) {
        super(id);
        this.setRenderBodyOnly( true );
        
        this.forProduct = forProduct;
        this.numReleases = numReleases;
        
        List<ProductRelease> releases = getReleases();
        final boolean showProd = this.forProduct == null;

        add( new Label("heading", "Releases" + (showProd ? "" : " of " + this.forProduct.getName() ) ) );
        add( new WebMarkupContainer("productTH").setVisible( showProd ) );
        
        //if( releases.size() == 0 )
        add( new ListView<ProductRelease>("rows", releases)
        {
            // Populate the table of contacts
            @Override
            protected void populateItem( final ListItem<ProductRelease> item) {
                ProductRelease pr = item.getModelObject();
                //item.add( new Label("product", pr.getProduct().getName()).setVisible(ReleasesBox.this.forProduct == null) );
                item.add( new WebMarkupContainer("productTD")
                        .add( new ProductLink("productLink", pr.getProduct()) )
                        .setVisible(showProd)
                );
                item.add( new ReleaseLink("versionLink", pr));
                item.add( new Label("status", pr.getStatus() == null ? "" : pr.getStatus().getStatusString()));
                Date date = pr.getPlannedFor();
                item.add( new Label("planned", (date == null) ? "" : DF.format( date )));
                //item.add( new Label("gitHash", pr.getGitHash()));
                //item.add( new Label("parent", pr.getParent().getVersion()));
            }
        });
    }// const
    
    
    protected List<ProductRelease> getReleases(){
        //return dao.getProductReleases_orderDateDesc(this.numReleases);
        return dao.getProductReleases_orderName(this.numReleases);
    }
        

}// class
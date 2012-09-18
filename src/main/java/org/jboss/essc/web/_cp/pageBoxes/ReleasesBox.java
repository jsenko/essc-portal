package org.jboss.essc.web._cp.pageBoxes;

import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jboss.essc.web._cp.links.ProductLink;
import org.jboss.essc.web._cp.links.ReleaseLink;
import org.jboss.essc.web.dao.ReleaseDaoBean;
import org.jboss.essc.web.model.Product;
import org.jboss.essc.web.model.Release;
import org.jboss.essc.web.pages.AddReleasePage;


/**
 * A list of releases.
 * 
 * @author Ondrej Zizka
 */
public class ReleasesBox extends Panel {

    @Inject protected ReleaseDaoBean dao;
    

    protected int numReleases = 6;
    
    private Product forProduct;
    
    
    public ReleasesBox( String id, int numReleases ) {
        this( id, null, numReleases );
    }
    
    
    public ReleasesBox( String id, Product forProduct, int numReleases ) {
        super(id);
        this.setRenderBodyOnly( true );
        
        this.forProduct = forProduct;
        this.numReleases = numReleases;
        
        List<Release> releases = getReleases();
        final boolean showProd = this.forProduct == null;

        add( new Label("heading", "Releases" + (showProd ? "" : " of " + this.forProduct.getName() ) ) );
        add( new WebMarkupContainer("productTH").setVisible( showProd ) );
        
        //if( releases.size() == 0 )
        add( new ListView<Release>("rows", releases)
        {
            // Populate the table of contacts
            @Override
            protected void populateItem( final ListItem<Release> item) {
                Release pr = item.getModelObject();
                //item.add( new Label("product", pr.getProduct().getName()).setVisible(ReleasesBox.this.forProduct == null) );
                item.add( new WebMarkupContainer("productTD")
                        .add( new ProductLink("productLink", pr.getProduct()) )
                        .setVisible(showProd)
                );
                item.add( new ReleaseLink("versionLink", pr));
                item.add( new Label("status", pr.getStatus() == null ? "" : pr.getStatus().getStatusString()));
                item.add( new Label("planned", pr.formatPlannedFor()) );
                //item.add( new Label("gitHash", pr.getGitHash()));
                //item.add( new Label("parent", pr.getParent().getVersion()));
            }
        });
        
        // "Add" link
        add( new WebMarkupContainer("add") .add( 
                forProduct == null 
                  ? new WebMarkupContainer("link")
                  : new BookmarkablePageLink("link", AddReleasePage.class, new PageParameters().add("product", this.forProduct.getName()) ) 
                )
                .setVisibilityAllowed(this.forProduct != null)
        );
        
    }// const
    
    
    protected List<Release> getReleases(){
        if( this.forProduct == null )
            return dao.getReleases_orderDateDesc(this.numReleases);
        else
            return dao.getReleasesOfLine( forProduct );
    }
        

}// class
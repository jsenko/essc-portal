package org.jboss.essc.web._cp.pageBoxes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jboss.essc.web._cp.links.ProductLink;
import org.jboss.essc.web._cp.links.ReleaseLink;
import org.jboss.essc.web.dao.ReleaseDaoBean;
import org.jboss.essc.web.model.Product;
import org.jboss.essc.web.model.Release;
import org.jboss.essc.web.model.User;
import org.jboss.essc.web.pages.rel.AddReleasePage;
import org.jboss.essc.web.security.EsscAuthSession;


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
        
        final boolean showProductCol = this.forProduct == null;
        final boolean showInternalReleases = isShowInternalStuff();

        List<Release> releases = getReleases( showInternalReleases );
        
        add( new Label("heading", "Releases" + (showProductCol ? "" : " of " + this.forProduct.getName() ) ) );
        add( new WebMarkupContainer("productTH").setVisible( showProductCol ) );
        
        //if( releases.size() == 0 )
        add( new ListView<Release>("rows", releases)
        {
            // Populate the table of contacts
            @Override
            protected void populateItem( final ListItem<Release> item) {
                Release rel = item.getModelObject();
                
                // CSS for releases row - differentiate internal from public.
                if( showInternalReleases )  // Only if necessary.
                    item.add( new AttributeAppender("class", rel.isInternal() ? "internal" : "public") );
                
                //item.add( new Label("product", pr.getProduct().getName()).setVisible(ReleasesBox.this.forProduct == null) );
                item.add( new WebMarkupContainer("productTD")
                        .add( new ProductLink("productLink", rel.getProduct()) )
                        .setVisible(showProductCol)
                );
                item.add( new ReleaseLink("versionLink", rel));
                
                // Status
                String plannedFor = rel.getPlannedFor() == null ? "" : " "+ rel.formatPlannedFor() + " ("+ rel.formatPlannedForRelative()+")";
                String status = rel.getStatus() == null ? "" : rel.getStatus().getStatusString() + " " + plannedFor;
                item.add( new Label("status", status));
                
                // Links
                item.add( new WebMarkupContainer("links").add(
                    new ListView<LabelAndLink>("repeater", createLinksList(rel)){
                        @Override protected void populateItem( ListItem<LabelAndLink> item ) {
                            LabelAndLink ll = item.getModelObject();
                            item.add(new ExternalLink("link", ll.link, ll.label) );
                        }
                    }
                ) );
                
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
    
    
    protected List<Release> getReleases( boolean showInternal ){
        if( this.forProduct == null )
            return dao.getReleases_orderDateDesc(this.numReleases, showInternal);
        else
            return dao.getReleasesOfProduct( forProduct, showInternal );
    }
    
    
    private static List<LabelAndLink> createLinksList( Release rel ) {
        List<LabelAndLink> links = new ArrayList<LabelAndLink>();
        addLinkIfNotNull( links, "Release", rel.getTraits().getLinkReleasedBinaries());
        addLinkIfNotNull( links, "Docs", rel.getTraits().getLinkReleasedDocs());
        addLinkIfNotNull( links, "Tests", rel.getTraits().getLinkMeadJob());
        addLinkIfNotNull( links, "Tattle", rel.getTraits().getLinkTattleTale());
        addLinkIfNotNull( links, "How to build", rel.getTraits().getLinkBuildHowto());
        //links.add( new LinkAndLabel( "", rel.getTraits().getLink()) );
        return links;
    }
    
    private static void addLinkIfNotNull( List<LabelAndLink> links, String label, String link ) {
        if( null != link)
            links.add( new LabelAndLink(label, link) );
    }

    // TODO: Could be handled by CSS only.
    private boolean isShowInternalStuff() {
        User user = ((EsscAuthSession)getSession()).getUser();
        return user == null ? false : user.isShowProd();
    }


}// class



class LabelAndLink implements Serializable {
    
    public final String label;
    public final String link;

    public LabelAndLink( String label, String link ) {
        this.label = label;
        this.link = link;
    }
}

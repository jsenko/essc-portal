package org.jboss.essc.web.pages;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.jboss.essc.web._cp.pageBoxes.NoItemsFoundBox;
import org.jboss.essc.web._cp.pageBoxes.ReleaseTraitsBox;
import org.jboss.essc.web._cp.pageBoxes.ReleasesBox;
import org.jboss.essc.web.dao.ProductLineDaoBean;
import org.jboss.essc.web.dao.ProductReleaseDaoBean;
import org.jboss.essc.web.model.ProductLine;
import org.jboss.essc.web.model.ProductRelease;


/**
 * Dynamic behavior for the ListContact page
 * 
 * @author Ondrej Zizka
 */
@SuppressWarnings("serial")
public class ReleasePage extends BaseLayoutPage {

    @Inject private ProductReleaseDaoBean releaseDao;
    
    private ProductRelease release;

    
    public ReleasePage( PageParameters par ) {
        String prod =  par.get("product").toOptionalString();
        String ver = par.get("version").toOptionalString();
        String titleIfNotFound = prod + " " + ver;
        try {
            this.release = releaseDao.getProductRelease( prod, ver );
        }
        catch( NoResultException ex ){ /* Release remains null. */ }
        init( titleIfNotFound );
    }

    public ReleasePage( ProductRelease release ) {
        this.release = release;
        init("Release not specified.");
    }
    
    private void init( String titleIfNotFound ){
        
        add( new Label("productName", this.release.getProduct().getName()) );
        add( new Label("version", this.release.getVersion()) );
        
        if( this.release != null ){
            add( new ReleaseTraitsBox("traits", this.release) );
        }
        else {
            add( new NoItemsFoundBox("traits", titleIfNotFound));
        }
    }
    

    public static PageParameters createPageParameters( ProductRelease rel ){
        return new PageParameters()
            .add("product", rel.getProduct().getName())
            .add("version", rel.getVersion() );
    }
    
    
    /** Adds CSS reference. */
    public void renderHead(IHeaderResponse response) {
        //response.renderCSSReference(new PackageResourceReference(HomePage.class, "default/calendar.css"));
        response.renderCSSReference(new CssResourceReference( HomePage.class, "default.css" ));
    }

}// class

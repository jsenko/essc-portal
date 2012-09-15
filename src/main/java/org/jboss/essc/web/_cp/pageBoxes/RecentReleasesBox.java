package org.jboss.essc.web._cp.pageBoxes;

import java.util.List;
import org.jboss.essc.web.model.ProductLine;
import org.jboss.essc.web.model.ProductRelease;


/**
 * A list of recent releases.
 * 
 * @author Ondrej Zizka
 */
public class RecentReleasesBox extends ReleasesBox {

    public RecentReleasesBox( String id, ProductLine forProduct, int numReleases ) {
        super( id, forProduct, numReleases );
    }

    public RecentReleasesBox( String id, int numReleases ) {
        super( id, numReleases );
    }

    
    protected List<ProductRelease> getReleases(){
        return this.dao.getProductReleases_orderDateDesc(this.numReleases);
    }
    
}// class

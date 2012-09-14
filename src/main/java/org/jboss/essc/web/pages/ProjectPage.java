package org.jboss.essc.web.pages;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.jboss.essc.web._cp.pageBoxes.NoItemsFoundBox;
import org.jboss.essc.web._cp.pageBoxes.ReleasesBox;
import org.jboss.essc.web.dao.ProductLineDaoBean;
import org.jboss.essc.web.model.ProductLine;


/**
 * Dynamic behavior for the ListContact page
 * 
 * @author Ondrej Zizka
 */
@SuppressWarnings("serial")
public class ProjectPage extends BaseLayoutPage {

    @Inject private ProductLineDaoBean productDao;
    
    private ProductLine product;

    
    public ProjectPage( PageParameters par ) {
        try {
            this.product = productDao.getProductLineByName( par.get("name").toString() );
        }
        catch( NoResultException ex ){
            // remains null.
        }
        init();
    }

    public ProjectPage( ProductLine product ) {
        this.product = product;
        init();
    }
    
    private void init(){
        if( this.product != null ){
            add( new ReleasesBox("releases", this.product, 100) );
        }
        else {
            add( new NoItemsFoundBox("releases"));
        }
    }
    
    
    
    /** Adds CSS reference. */
    public void renderHead(IHeaderResponse response) {
        //response.renderCSSReference(new PackageResourceReference(HomePage.class, "default/calendar.css"));
        response.renderCSSReference(new CssResourceReference( HomePage.class, "default.css" ));
    }

}// class

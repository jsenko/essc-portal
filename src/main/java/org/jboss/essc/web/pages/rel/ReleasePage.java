package org.jboss.essc.web.pages.rel;

import org.jboss.essc.web.pages.prod.ProductPage;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jboss.essc.web._cp.pageBoxes.ReleaseBox;
import org.jboss.essc.web._cp.pageBoxes.NoItemsFoundBox;
import org.jboss.essc.web.dao.ReleaseDaoBean;
import org.jboss.essc.web.model.Release;
import org.jboss.essc.web.pages.BaseLayoutPage;


/**
 * @author Ondrej Zizka
 */
@SuppressWarnings("serial")
public class ReleasePage extends BaseLayoutPage {

    @Inject private ReleaseDaoBean releaseDao;
    
    private Release release;

    
    public ReleasePage( PageParameters par ) {
        String prod =  par.get("product").toOptionalString();
        String ver = par.get("version").toOptionalString();
        String titleIfNotFound = prod + " " + ver;
        try {
            this.release = releaseDao.getRelease( prod, ver );
        }
        catch( NoResultException ex ){ /* Release remains null. */ }
        init( titleIfNotFound );
    }

    public ReleasePage( Release release ) {
        this.release = release;
        init("Release not specified.");
    }
    
    private void init( String titleIfNotFound ){
        
        if( this.release != null ){
            add( new ReleaseBox("releaseBox", this.release) );
        }
        else {
            add( new NoItemsFoundBox("releaseBox", titleIfNotFound));
        }
        
        
        // Danger Zone
        WebMarkupContainer dangerZone = new WebMarkupContainer("dangerZone");
        this.add( dangerZone );
        
        // Danger Zone Form
        dangerZone.add( new StatelessForm("form") {
            {
                // Really button
                final AjaxButton really = new AjaxButton("deleteReally") {};
                really.setVisible(false).setRenderBodyOnly(false);
                really.setOutputMarkupPlaceholderTag(true);
                add( really );
                
                // Delete button
                add( new AjaxLink("delete"){
                    @Override public void onClick( AjaxRequestTarget target ) {
                        target.add( really );
                        really.setVisible(true);
                    }
                });
            }
            @Override protected void onSubmit() {
                PageParameters params = ProductPage.createPageParameters(release.getProduct());
                releaseDao.remove( release );
                setResponsePage(ProductPage.class, params );
            }
        });
        
    }
    

    public static PageParameters createPageParameters( Release rel ){
        return new PageParameters()
            .add("product", rel.getProduct().getName())
            .add("version", rel.getVersion() );
    }
    
}// class

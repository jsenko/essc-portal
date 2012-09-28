package org.jboss.essc.web.pages;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jboss.essc.web._cp.links.PropertiesDownloadLink;
import org.jboss.essc.web._cp.pageBoxes.NoItemsFoundBox;
import org.jboss.essc.web._cp.pageBoxes.ReleaseTraitsPanel;
import org.jboss.essc.web._cp.pageBoxes.ReleasesBox;
import org.jboss.essc.web.dao.ProductDaoBean;
import org.jboss.essc.web.model.Product;


/**
 * Dynamic behavior for the ListContact page
 * 
 * @author Ondrej Zizka
 */
@SuppressWarnings("serial")
public class ProductPage extends BaseLayoutPage {

    @Inject private ProductDaoBean productDao;
    
    // Components
    private Form<Product> form;

    // Data
    private Product product;

    
    public ProductPage( PageParameters par ) {
        try {
            this.product = productDao.getProductByName( par.get("name").toString() );
        }
        catch( NoResultException ex ){ /* remains null. */ }
        init();
    }

    public ProductPage( Product product ) {
        this.product = product;
        init();
    }
    
    private void init()
    {
        // Feedback
        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId( true );
        feedbackPanel.setFilter( new ContainerFeedbackMessageFilter(this) );
        add(feedbackPanel);

        // Form
        this.form = new StatelessForm("form") {
            @Override protected void onSubmit() {
                product = productDao.update( (Product) product );
            }
        };
        this.form.setVersioned(false);
        add( this.form );
        
        // Boxes
        if( this.product != null ){
            add( new ReleasesBox("releasesBox", this.product, 100) );
            this.form.add( new ReleaseTraitsPanel("templates", this.product) );
        }
        else {
            add( new NoItemsFoundBox("releasesBox", "No product specified."));
            this.form.add( new WebMarkupContainer("templates"));
        }
        
        
        // Save as .properties - TODO
        this.form.add( new PropertiesDownloadLink("downloadProps", product.getTraits(), product.getName() + "-traits.properties") );

        
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
                        //really.add(AttributeModifier.replace("style", "")); // Removes style="visibility: hidden".
                        //super.onSubmit( target, form );
                    }
                });
            }
            @Override protected void onSubmit() {
                productDao.deleteIncludingReleases( (Product) product );
                setResponsePage(HomePage.class);
            }
        });

    }// init()

    
    
    public static PageParameters createPageParameters( Product prod ){
        return new PageParameters().add("name", prod.getName());
    }
    
}// class
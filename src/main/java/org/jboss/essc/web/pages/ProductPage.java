package org.jboss.essc.web.pages;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Bytes;
import org.jboss.essc.web._cp.links.PropertiesDownloadLink;
import org.jboss.essc.web._cp.pageBoxes.NoItemsFoundBox;
import org.jboss.essc.web._cp.pageBoxes.ReleaseTraitsPanel;
import org.jboss.essc.web._cp.pageBoxes.ReleasesBox;
import org.jboss.essc.web.dao.ProductDaoBean;
import org.jboss.essc.web.model.Product;
import org.jboss.essc.web.model.ReleaseTraits;
import org.jboss.essc.web.util.PropertiesUtils;


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
        setDefaultModel( new PropertyModel( this, "product") );
        
        // Feedback
        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId( true );
        feedbackPanel.setFilter( new ContainerFeedbackMessageFilter(this) );
        add(feedbackPanel);

        // Form
        this.form = new StatelessForm("form") {
            @Override protected void onSubmit() {
                product = productDao.update( product );
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

        // Upload & apply .properties
        this.add( new Form("uploadForm"){
            FileUploadField upload;
            {
                add( this.upload = new FileUploadField("file") );
                setMultiPart(true);
                setMaxSize(Bytes.kilobytes(100));
            }

            @Override
            protected void onSubmit() {
                FileUpload fu = this.upload.getFileUpload();
                if( null == fu ) return;
                
                Properties props = new Properties();
                try {
                    props.load( fu.getInputStream() );
                }
                catch( IOException ex ) {
                    feedbackPanel.error( "Could not process properties: " + ex.toString() );
                    return;
                }
                
                ReleaseTraits traits = ((Product)getPage().getDefaultModelObject()).getTraits();
                PropertiesUtils.applyToObjectFlat( traits, props );
                productDao.update( product );
            }
        });
        
        
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

    
    
    public Product getProduct() { return product; }
    public void setProduct( Product product ) { this.product = product; }
    
    
}// class
package org.jboss.essc.web._cp.pageBoxes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import javax.inject.Inject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.lang.Bytes;
import org.jboss.essc.web._cp.PropertiesUploadForm;
import org.jboss.essc.web._cp.links.PropertiesDownloadLink;
import org.jboss.essc.web.dao.ProductDaoBean;
import org.jboss.essc.web.dao.ReleaseDaoBean;
import org.jboss.essc.web.model.Product;
import org.jboss.essc.web.model.Release;
import org.jboss.essc.web.model.ReleaseTraits;
import org.jboss.essc.web.util.PropertiesUtils;


/**
 * Contains a list of products. Appears on the home page.
 * 
 * @author Ondrej Zizka
 */
public class ReleaseBox extends Panel {
    
    @Inject private ReleaseDaoBean daoRelease;

    
    // Components
    private Form<Release> form;

    // Data
    private Release release;

    
        
    public ReleaseBox( String id, final Release release ) {
        super( id );
        this.release = release;
        this.setDefaultModel( new PropertyModel<Release> ( this, "release") );

        // Heading
        add( new Label("productName", this.release.getProduct().getName()) );
        add( new Label("version", this.release.getVersion()) );
        
        // Feedback
        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId( true );
        add(feedbackPanel);
        
        // Form
        this.form = new StatelessForm("form") {
            @Override protected void onSubmit() {
                ReleaseBox.this.release = daoRelease.update( (Release) release );
            }
        };
        this.form.setVersioned(false);
        add( this.form );
        
        // Status
        this.form.add( new DropDownChoice("status",
                new PropertyModel( release, "status"),
                new ArrayList<Release.Status>( Arrays.asList( Release.Status.values() ))
        ));
        
        // Date
        //this.form.add( new AjaxEditableLabel("date").add(new PatternValidator("\\d{4}-\\d\\d-\\d\\d") ) );
        this.form.add( new DateTextField("date", new PropertyModel<Date>( release, "plannedFor"), "yyyy-MM-dd")
            .add(new AjaxFormComponentUpdatingBehavior("onchange") {
                @Override protected void onUpdate( AjaxRequestTarget target ) {
                    Date date = ((DateTextField)getFormComponent()).getModelObject();
                    release.setPlannedFor( date );
                }
            })
        );
        
        // Traits
        this.form.add( new ReleaseTraitsPanel( "traits", release ) );
        
        // Save as .properties - TODO
        this.form.add( new PropertiesDownloadLink("downloadProps", release, release.toStringIdentifier() + "-traits.properties") );
        
        // Upload & apply .properties
        this.add( new PropertiesUploadForm("uploadForm"){

            @Override protected void onSubmit() {
                Properties props;
                try {
                    props = processPropertiesFromUploadedFile();
                }
                catch( IOException ex ) {
                    feedbackPanel.error( "Could not process properties: " + ex.toString() );
                    return;
                }
                
                Release rel = (Release) ReleaseBox.this.getDefaultModelObject();
                ReleaseTraits traits = release.getTraits();
                PropertiesUtils.applyToObjectFlat( traits, props );
                rel = daoRelease.update( rel );
                ReleaseBox.this.setDefaultModelObject( rel );
            }
        });

    }

    
    public Release getRelease() { return release; }
    public void setRelease( Release release ) { this.release = release; }
    
}// class

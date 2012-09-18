package org.jboss.essc.web._cp;

import java.util.ArrayList;
import java.util.Arrays;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.jboss.essc.web.dao.ProductLineDaoBean;
import org.jboss.essc.web.dao.ProductReleaseDaoBean;
import org.jboss.essc.web.model.ProductRelease;


/**
 * Contains a list of products. Appears on the home page.
 * 
 * @author Ondrej Zizka
 */
public class ReleaseBox extends Panel {
    
    @Inject private ProductReleaseDaoBean prodRelDao;
    @Inject private ProductLineDaoBean prodDao;

    // Components
    private Form<ProductRelease> form;

    // Data
    private ProductRelease release;

    
        
    public ReleaseBox( String id, final ProductRelease release ) {
        super( id );
        this.release = release;

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
                ReleaseBox.this.release = prodRelDao.update( (ProductRelease) release );
            }
        };
        this.form.setVersioned(false);
        add( this.form );
        
        // Status
        this.form.add( new DropDownChoice("status",
                new PropertyModel( release, "status"),
                new ArrayList<ProductRelease.Status>( Arrays.asList( ProductRelease.Status.values() ))
        ).setVisibilityAllowed( release instanceof ProductRelease ) );
        
        
    }

}// class

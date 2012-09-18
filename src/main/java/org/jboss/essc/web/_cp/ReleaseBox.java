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
import org.jboss.essc.web._cp.pageBoxes.ReleaseTraitsPanel;
import org.jboss.essc.web.dao.ProductDaoBean;
import org.jboss.essc.web.dao.ReleaseDaoBean;
import org.jboss.essc.web.model.Release;


/**
 * Contains a list of products. Appears on the home page.
 * 
 * @author Ondrej Zizka
 */
public class ReleaseBox extends Panel {
    
    @Inject private ReleaseDaoBean prodRelDao;
    @Inject private ProductDaoBean prodDao;

    // Components
    private Form<Release> form;

    // Data
    private Release release;

    
        
    public ReleaseBox( String id, final Release release ) {
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
                ReleaseBox.this.release = prodRelDao.update( (Release) release );
            }
        };
        this.form.setVersioned(false);
        add( this.form );
        
        // Status
        this.form.add( new DropDownChoice("status",
                new PropertyModel( release, "status"),
                new ArrayList<Release.Status>( Arrays.asList( Release.Status.values() ))
        ).setVisibilityAllowed( release instanceof Release ) );
        
        // Traits
        this.form.add( new ReleaseTraitsPanel( "traits", release ) );
    }

}// class

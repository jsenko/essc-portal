package org.jboss.essc.web._cp.pageBoxes;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.UrlValidator;
import org.jboss.essc.web.model.IHasTraits;
import org.jboss.essc.web.model.ProductLine;
import org.jboss.essc.web.model.ProductRelease;
import org.jboss.essc.web.model.ReleaseTraits;
import org.jboss.essc.wicket.UrlHttpRequestValidator;


/**
 *  A box with release traits OR product templates.
 * 
 *  TODO: Rename to ReleaseTraitsPanel
 * 
 * @author Ondrej Zizka
 */
public class ReleaseTraitsBox extends Panel {
    
    //@Inject private ProductReleaseDaoBean prodRelDao;
    //@Inject private ProductLineDaoBean prodDao;

    // Components
    //private Form<IHasTraits> form;

    // Data
    private IHasTraits release;


    
    // Wicket needs this :(
    public ReleaseTraitsBox( String id, final ProductLine prod ) {
        this( id, prod, 0 );
    }
    public ReleaseTraitsBox( String id, final ProductRelease release ) {
        this( id, release, 0 );
    }
    public ReleaseTraitsBox( String id, final IHasTraits release ) {
        this( id, release, 0 );
    }

    public ReleaseTraitsBox( String id, final IHasTraits release, int foo ) {
        super(id);
        this.release = release;

        // Feedback
        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId( true );
        feedbackPanel.setFilter( new ContainerFeedbackMessageFilter(this) );
        add(feedbackPanel);
        
        // Validators
        UrlValidator val = new UrlValidator();
        UrlHttpRequestValidator val2 = new UrlHttpRequestValidator();
        
        // Traits
        ReleaseTraits traits = this.release.getTraits();
        
        //this.add( new TextField("releasedBinaries", new PropertyModel( traits, "linkReleasedBinaries") ).add(val).add(val2) );
        this.add( new AjaxEditableLabel("releasedBinaries", new PropertyModel( traits, "linkReleasedBinaries") ){
            @Override protected void onError( AjaxRequestTarget target ) {
                target.add( feedbackPanel );
                //super.onError( target ); // puts the focus back.
            }
            @Override protected void onSubmit( AjaxRequestTarget target ) {
                target.add( feedbackPanel );
                super.onSubmit( target );
            }
        }.add(val).add(val2) );
        this.add( new AjaxEditableLabel("stagedBinaries",   new PropertyModel( traits, "linkStagedBinaries") ).add(val) ); //.add(val2) );
        this.add( new AjaxEditableLabel("releasedDocs",     new PropertyModel( traits, "linkReleasedDocs") ).add(val).add(val2) );
        this.add( new AjaxEditableLabel("stagedDocs",       new PropertyModel( traits, "linkStagedDocs") ).add(val).add(val2) );
        this.add( new AjaxEditableLabel("gitRepo",          new PropertyModel( traits, "linkGitRepo") ).add(val).add(val2) );
        this.add( new AjaxEditableLabel("gitHash",          new PropertyModel( traits, "gitHash") ).add(val).add(val2) );
        this.add( new AjaxEditableLabel("mead",             new PropertyModel( traits, "linkMead") ).add(val).add(val2) );
        this.add( new AjaxEditableLabel("brew",             new PropertyModel( traits, "linkBrew") ).add(val).add(val2) );
        this.add( new AjaxEditableLabel("issuesFixed",      new PropertyModel( traits, "linkIssuesFixed") ).add(val).add(val2) );
        this.add( new AjaxEditableLabel("issuesFound",      new PropertyModel( traits, "linkIssuesFound") ).add(val).add(val2) );
        //this.add( new AjaxEditableLabel("",            new PropertyModel( traits, "link") ).add(val).add(val2) );

    }// const

    
}// class

package org.jboss.essc.web._cp.pageBoxes;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.UrlValidator;
import org.jboss.essc.web.model.IHasTraits;
import org.jboss.essc.web.model.Product;
import org.jboss.essc.web.model.Release;
import org.jboss.essc.web.model.ReleaseTraits;
import org.jboss.essc.wicket.UrlHttpRequestValidator;


/**
 *  A box with release traits OR product templates.
 * 
 * @author Ondrej Zizka
 */
public class ReleaseTraitsPanel extends Panel {
    
    // Components
    final FeedbackPanel feedbackPanel;
    
    // Validators
    UrlValidator val = new UrlValidator();
    UrlHttpRequestValidator val2 = new UrlHttpRequestValidator();
    
    // Data
    private IHasTraits release;


    
    // Wicket needs this :(
    public ReleaseTraitsPanel( String id, final Product prod ) {
        this( id, prod, 0 );
    }
    public ReleaseTraitsPanel( String id, final Release release ) {
        this( id, release, 0 );
    }
    public ReleaseTraitsPanel( String id, final IHasTraits release ) {
        this( id, release, 0 );
    }

    public ReleaseTraitsPanel( String id, final IHasTraits release, int foo ) {
        super(id);
        this.release = release;

        // Feedback
        this.feedbackPanel = new FeedbackPanel("feedback");
        this.feedbackPanel.setOutputMarkupId( true );
        this.feedbackPanel.setFilter( new ContainerFeedbackMessageFilter(this) );
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
        this.add( new MyAjaxEditableLabel("stagedBinaries",   new PropertyModel( traits, "linkStagedBinaries") ) );
        this.add( new MyAjaxEditableLabel("releasedDocs",     new PropertyModel( traits, "linkReleasedDocs") ) );
        this.add( new MyAjaxEditableLabel("stagedDocs",       new PropertyModel( traits, "linkStagedDocs") ) );
        this.add( new MyAjaxEditableLabel("gitRepo",          new PropertyModel( traits, "linkGitRepo") ) );
        this.add( new MyAjaxEditableLabel("gitHash",          new PropertyModel( traits, "gitHash") ) );
        this.add( new MyAjaxEditableLabel("mead",             new PropertyModel( traits, "linkMead") ) );
        this.add( new MyAjaxEditableLabel("brew",             new PropertyModel( traits, "linkBrew") ) );
        this.add( new MyAjaxEditableLabel("issuesFixed",      new PropertyModel( traits, "linkIssuesFixed") ) );
        this.add( new MyAjaxEditableLabel("issuesFound",      new PropertyModel( traits, "linkIssuesFound") ) );
        //this.add( new MyAjaxEditableLabel("",            new PropertyModel( traits, "link") ) );

    }// const

    
    
    /**
     *  Adds the feedback panel to AJAX target.
     */
    class MyAjaxEditableLabel extends AjaxEditableLabel<String>{

        public MyAjaxEditableLabel( String id, IModel<String> model ) {
            super( id, model );
            this.add( val );
            this.add( val2 );
        }

        @Override protected void onError( AjaxRequestTarget target ) {
            target.add( feedbackPanel );
            //super.onError( target ); // puts the focus back.
        }
        @Override protected void onSubmit( AjaxRequestTarget target ) {
            target.add( feedbackPanel );
            super.onSubmit( target );
        }
    }
    
}// class

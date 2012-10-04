package org.jboss.essc.web._cp.pageBoxes;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.UrlValidator;
import org.jboss.essc.web.model.IHasTraits;
import org.jboss.essc.web.model.Product;
import org.jboss.essc.web.model.Release;
import org.jboss.essc.web.model.ReleaseTraits;
import org.jboss.essc.wicket.UrlHttpRequestValidator;
import org.jboss.essc.wicket.UrlSimpleValidator;

import static org.jboss.essc.web.model.Release.Status;


/**
 *  A box with release traits OR product templates.
 * 
 * @author Ondrej Zizka
 */
public class ReleaseTraitsPanel extends Panel {
    
    // Components
    final FeedbackPanel feedbackPanel;
    
    // Validators
    UrlValidator urlFormatValidator = new UrlValidator();
    UrlSimpleValidator urlFormatSimpleValidator = new UrlSimpleValidator();
    UrlHttpRequestValidator urlHttpValidator = new UrlHttpRequestValidator();
    
    // Data
    private IHasTraits release;
    private boolean urlVerificationEnabled;


    
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

        
        Model<IHasTraits> rm = new Model(release);
        
        //this.add( new MyAjaxEditableLabel("releasedBinaries", new PropertyModel( traits, "linkReleasedBinaries") ));
        this.add( new ReleaseTraitRowPanel("releasedBinaries", rm, "Released binaries",   "linkReleasedBinaries", Status.RELEASED, this, feedbackPanel));
        //this.add( new MyAjaxEditableLabel("stagedBinaries",   new PropertyModel( traits, "linkStagedBinaries") ) );
        this.add( new ReleaseTraitRowPanel("stagedBinaries",   rm, "Staged binaries",     "linkStagedBinaries",   Status.STAGED, this, feedbackPanel));
        //this.add( new MyAjaxEditableLabel("releasedDocs",     new PropertyModel( traits, "linkReleasedDocs") ) );
        this.add( new ReleaseTraitRowPanel("releasedDocs",     rm, "Released docs",      "linkReleasedDocs",      Status.RELEASED, this, feedbackPanel));
        //this.add( new MyAjaxEditableLabel("stagedDocs",       new PropertyModel( traits, "linkStagedDocs") ) );
        this.add( new ReleaseTraitRowPanel("stagedDocs",       rm, "Staged docs",        "linkStagedDocs",        Status.STAGED, this, feedbackPanel));
        //this.add( new MyAjaxEditableLabel("linkJavadoc",      new PropertyModel( traits, "linkJavadoc") ) );
        this.add( new ReleaseTraitRowPanel("linkJavadoc",      rm, "Public API Javadoc", "linkJavadoc",           Status.RELEASED, this, feedbackPanel));


        this.add( new MyAjaxEditableLabel("issuesFixed",      new PropertyModel( traits, "linkIssuesFixed") ) );
        this.add( new MyAjaxEditableLabel("issuesFound",      new PropertyModel( traits, "linkIssuesFound") ) );
        
        // Build
        this.add( new MyAjaxEditableLabel("linkBuildHowto",   new PropertyModel( traits, "linkBuildHowto") ) );
        this.add( new MyAjaxEditableLabel("gitHash",          new PropertyModel( traits, "gitHash") ) );
        this.add( new MyAjaxEditableLabel("gitRepo",          new PropertyModel( traits, "linkGitRepo") ) );
        this.add( new MyAjaxEditableLabel("mead",             new PropertyModel( traits, "linkMead") ) );
        this.add( new MyAjaxEditableLabel("brew",             new PropertyModel( traits, "linkBrew") ) );

        // Tests and reports
        this.add( new MyAjaxEditableLabel("linkMeadJob",      new PropertyModel( traits, "linkMeadJob") ) );
        this.add( new MyAjaxEditableLabel("linkTattleTale",   new PropertyModel( traits, "linkTattleTale") ) );
        this.add( new MyAjaxEditableLabel("linkCodeCoverage", new PropertyModel( traits, "linkCodeCoverage") ) );
        this.add( new MyAjaxEditableLabel("linkTck",          new PropertyModel( traits, "linkTck") ) );
        this.add( new MyAjaxEditableLabel("linkCC",           new PropertyModel( traits, "linkCC") ) );
        this.add( new MyAjaxEditableLabel("link508",          new PropertyModel( traits, "link508") ) );
        this.add( new MyAjaxEditableLabel("linkJavaEE",       new PropertyModel( traits, "linkJavaEE") ) );
        
        //this.add( new MyAjaxEditableLabel("",            new PropertyModel( traits, "link") ) );

    }// const

    
    
    /**
     *  Adds the feedback panel to AJAX target.
     */
    public class MyAjaxEditableLabel extends AjaxEditableLabel<String>{

        public MyAjaxEditableLabel( String id, IModel<String> model ) {
            super( id, model );
            if( isUrlVerificationEnabled() ){
                this.add( urlFormatValidator );
                this.add( urlHttpValidator );
            } else {
                this.add( urlFormatSimpleValidator );
            }
            add(AttributeModifier.append("class", " fakeLink") );
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
    
    
    //<editor-fold defaultstate="collapsed" desc="Get/Set">
    public boolean isUrlVerificationEnabled() { return urlVerificationEnabled; }
    public void setUrlVerificationEnabled( boolean urlVerificationEnabled ) { this.urlVerificationEnabled = urlVerificationEnabled; }
    //</editor-fold>

    
}// class

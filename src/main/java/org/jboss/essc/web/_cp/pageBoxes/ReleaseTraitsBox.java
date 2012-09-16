package org.jboss.essc.web._cp.pageBoxes;

import java.util.ArrayList;
import java.util.Arrays;
import javax.inject.Inject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.UrlValidator;
import org.jboss.essc.web.dao.ProductLineDaoBean;
import org.jboss.essc.web.dao.ProductReleaseDaoBean;
import org.jboss.essc.web.model.IHasTraits;
import org.jboss.essc.web.model.ProductLine;
import org.jboss.essc.web.model.ProductRelease;
import org.jboss.essc.wicket.UrlHttpRequestValidator;


/**
 * @author Ondrej Zizka
 */
public class ReleaseTraitsBox extends Panel {
    
    @Inject private ProductReleaseDaoBean prodRelDao;
    @Inject private ProductLineDaoBean prodDao;

    // Components
    private Form<IHasTraits> insertForm;

    // Data
    private IHasTraits release;


    
    public ReleaseTraitsBox( String id, final IHasTraits release ) {
        super(id);
        
        this.release = release;
        //add( new Label("productName", release.getProduct().getName() ));
        //add( new Label("version", release.getVersion() ));
        
        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId( true );
        add(feedbackPanel);
        
        this.insertForm = new StatelessForm("form") {
            @Override protected void onSubmit() {
                if( release instanceof ProductRelease )
                    ReleaseTraitsBox.this.release = prodRelDao.update( (ProductRelease) release );
                else if( release instanceof ProductLine )
                    ReleaseTraitsBox.this.release = prodDao.update( (ProductLine) release );
            }
        };
        this.insertForm.setVersioned(false);
        add( this.insertForm );
        
        this.insertForm.add( new DropDownChoice("status",
                new PropertyModel( release, "status"),
                new ArrayList<ProductRelease.Status>( Arrays.asList( ProductRelease.Status.values() ))
        ));
        
        UrlValidator val = new UrlValidator();
        UrlHttpRequestValidator val2 = new UrlHttpRequestValidator();
        
        //this.insertForm.add( new TextField("releasedBinaries", new PropertyModel( release, "linkReleasedBinaries") ).add(val).add(val2) );
        this.insertForm.add( new AjaxEditableLabel("releasedBinaries", new PropertyModel( release, "linkReleasedBinaries") ){
            @Override protected void onError( AjaxRequestTarget target ) {
                target.add( feedbackPanel );
                //super.onError( target ); // puts the focus back.
            }
            @Override protected void onSubmit( AjaxRequestTarget target ) {
                target.add( feedbackPanel );
                super.onSubmit( target );
            }
        }.add(val).add(val2) );
        this.insertForm.add( new AjaxEditableLabel("stagedBinaries",   new PropertyModel( release, "linkStagedBinaries") ).add(val) ); //.add(val2) );
        this.insertForm.add( new AjaxEditableLabel("releasedDocs",     new PropertyModel( release, "linkReleasedDocs") ).add(val).add(val2) );
        this.insertForm.add( new AjaxEditableLabel("stagedDocs",       new PropertyModel( release, "linkStagedDocs") ).add(val).add(val2) );
        this.insertForm.add( new AjaxEditableLabel("gitRepo",          new PropertyModel( release, "linkGitRepo") ).add(val).add(val2) );
        this.insertForm.add( new AjaxEditableLabel("gitHash",          new PropertyModel( release, "gitHash") ).add(val).add(val2) );
        this.insertForm.add( new AjaxEditableLabel("mead",             new PropertyModel( release, "linkMead") ).add(val).add(val2) );
        this.insertForm.add( new AjaxEditableLabel("brew",             new PropertyModel( release, "linkBrew") ).add(val).add(val2) );
        this.insertForm.add( new AjaxEditableLabel("issuesFixed",      new PropertyModel( release, "linkIssuesFixed") ).add(val).add(val2) );
        this.insertForm.add( new AjaxEditableLabel("issuesFound",      new PropertyModel( release, "linkIssuesFound") ).add(val).add(val2) );
        //this.insertForm.add( new AjaxEditableLabel("",            new PropertyModel( release, "link") ).add(val).add(val2) );

    }// const

    
}// class

package org.jboss.essc.web._cp.pageBoxes;

import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.jboss.essc.web.dao.ProductLineDaoBean;
import org.jboss.essc.web.dao.ProductReleaseDaoBean;
import org.jboss.essc.web.model.ProductRelease;


/**
 * @author Ondrej Zizka
 */
public class ReleaseTraitsBox extends Panel {
    
    @Inject private ProductReleaseDaoBean prodRelDao;
    @Inject private ProductLineDaoBean prodDao;

    // Components
    private Form<ProductRelease> insertForm;

    // Data
    private ProductRelease release;


    
    public ReleaseTraitsBox( String id, final ProductRelease release ) {
        super(id);
        
        this.release = release;
        
        add( new Label("productName", release.getProduct().getName() ));
        add( new Label("version", release.getVersion() ));
        
        add(new FeedbackPanel("feedback"));
        
        this.insertForm = new Form("form") {
            @Override protected void onSubmit() {
                ReleaseTraitsBox.this.release = prodRelDao.update(release);
            }
        };
        add( this.insertForm );
        
        this.insertForm.add( new TextField("releasedBinaries", new PropertyModel( release, "linkReleasedBinaries") ) );
        this.insertForm.add( new TextField("stagedBinaries",   new PropertyModel( release, "linkStagedBinaries") ) );
        this.insertForm.add( new TextField("releasedDocs",     new PropertyModel( release, "linkReleasedDocs") ) );
        this.insertForm.add( new TextField("stagedDocs",       new PropertyModel( release, "linkStagedDocs") ) );
        this.insertForm.add( new TextField("gitRepo",          new PropertyModel( release, "linkGitRepo") ) );
        this.insertForm.add( new TextField("gitHash",          new PropertyModel( release, "gitHash") ) );
        this.insertForm.add( new TextField("mead",             new PropertyModel( release, "linkMead") ) );
        this.insertForm.add( new TextField("brew",             new PropertyModel( release, "linkBrew") ) );
        this.insertForm.add( new TextField("issuesFixed",      new PropertyModel( release, "linkIssuesFixed") ) );
        this.insertForm.add( new TextField("issuesFound",      new PropertyModel( release, "linkIssuesFound") ) );
        //this.insertForm.add( new TextField("",            new PropertyModel( release, "link") ) );

    }// const

    
}// class

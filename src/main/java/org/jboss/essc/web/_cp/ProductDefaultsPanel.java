package org.jboss.essc.web._cp;

import javax.inject.Inject;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.jboss.essc.web.dao.ProductDaoBean;
import org.jboss.essc.web.dao.ReleaseDaoBean;
import org.jboss.essc.web.model.ProductLine;
import org.jboss.essc.web.model.ReleaseTraits;


/**
 * @author Ondrej Zizka
 */
public class ProductDefaultsPanel extends Panel {
    
    @Inject private ReleaseDaoBean prodRelDao;
    @Inject private ProductDaoBean prodDao;

    // Components
    private Form<ProductLine> insertForm;

    // Data
    private ProductLine product;

    
    public ProductDefaultsPanel( String id, final ProductLine _product ) {
        super(id);
        
        add(new FeedbackPanel("feedback"));
        
        // Form
        this.insertForm = new StatelessForm<ProductLine>("insertForm") {
            @Override protected void onSubmit() {
                ProductDefaultsPanel.this.product = prodDao.update(_product);
            }
        };
        
        // Traits
        ReleaseTraits traits = this.product.getTraits();
        
        this.insertForm.add( new TextField("releasedBinaries", new PropertyModel( traits, "linkReleasedBinaries") ) );
        this.insertForm.add( new TextField("stagedBinaries",   new PropertyModel( traits, "linkStagedBinaries") ) );
        this.insertForm.add( new TextField("releasedDocs",     new PropertyModel( traits, "linkReleasedDocs") ) );
        this.insertForm.add( new TextField("stagedDocs",       new PropertyModel( traits, "linkStagedDocs") ) );
        this.insertForm.add( new TextField("mead",             new PropertyModel( traits, "linkMead") ) );
        this.insertForm.add( new TextField("brew",             new PropertyModel( traits, "linkBrew") ) );
        this.insertForm.add( new TextField("issuesFixed",      new PropertyModel( traits, "linkIssuesFixed") ) );
        this.insertForm.add( new TextField("issuesFound",      new PropertyModel( traits, "linkIssuesFound") ) );
        //this.insertForm.add( new TextField("", new PropertyModel( traits, "link") ) );

    }// const
    
}
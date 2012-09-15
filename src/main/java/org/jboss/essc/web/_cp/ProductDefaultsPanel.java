package org.jboss.essc.web._cp;

import javax.inject.Inject;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.jboss.essc.web.dao.ProductLineDaoBean;
import org.jboss.essc.web.dao.ProductReleaseDaoBean;
import org.jboss.essc.web.model.ProductLine;


/**
 * @author Ondrej Zizka
 */
public class ProductDefaultsPanel extends Panel {
    
    @Inject private ProductReleaseDaoBean prodRelDao;
    @Inject private ProductLineDaoBean prodDao;

    // Components
    private Form<ProductLine> insertForm;

    // Data
    private ProductLine product;

    
    public ProductDefaultsPanel( String id, final ProductLine _product ) {
        super(id);
        
        add(new FeedbackPanel("feedback"));
        
        this.insertForm = new Form<ProductLine>("insertForm") {
            @Override protected void onSubmit() {
                ProductDefaultsPanel.this.product = prodDao.update(_product);
            }
        };
        
        this.insertForm.add( new TextField("releasedBinaries", new PropertyModel( product, "linkReleasedBinaries") ) );
        this.insertForm.add( new TextField("stagedBinaries",   new PropertyModel( product, "linkStagedBinaries") ) );
        this.insertForm.add( new TextField("releasedDocs",     new PropertyModel( product, "linkReleasedDocs") ) );
        this.insertForm.add( new TextField("stagedDocs",       new PropertyModel( product, "linkStagedDocs") ) );
        this.insertForm.add( new TextField("mead",             new PropertyModel( product, "linkMead") ) );
        this.insertForm.add( new TextField("brew",             new PropertyModel( product, "linkBrew") ) );
        this.insertForm.add( new TextField("issuesFixed",      new PropertyModel( product, "linkIssuesFixed") ) );
        this.insertForm.add( new TextField("issuesFound",      new PropertyModel( product, "linkIssuesFound") ) );
        //this.insertForm.add( new TextField("", new PropertyModel( product, "link") ) );

    }// const
    
}
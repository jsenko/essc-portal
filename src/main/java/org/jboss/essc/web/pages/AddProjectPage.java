package org.jboss.essc.web.pages;

import javax.inject.Inject;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jboss.essc.web.dao.ProductLineDaoBean;
import org.jboss.essc.web.dao.ProductReleaseDaoBean;
import org.jboss.essc.web.model.Contact;
import org.jboss.essc.web.model.ProductLine;

/**
 *
 * @author Ondrej Zizka
 */
@SuppressWarnings("serial")
public class AddProjectPage extends BaseLayoutPage {

    @Inject private ProductReleaseDaoBean prodRelDao;
    @Inject private ProductLineDaoBean prodDao;

    // Components
    private Form<Contact> insertForm;

    // Data
    private ProductLine product = new ProductLine();
    

    
    public AddProjectPage(PageParameters params) {
        
        String proj = params.get("project").toOptionalString();
        add(new Label("titleReleaseOf", proj == null ? "" : " of " + proj ));
        
        add(new FeedbackPanel("feedback"));

        this.insertForm = new Form<Contact>("form") {

            @Override
            protected void onSubmit() {
                product = prodDao.addProductLine(product);
                setResponsePage(ProjectPage.class);
            }
        };

        this.insertForm.add(new RequiredTextField<String>("name", new PropertyModel<String>(this.product, "name")));
        add(this.insertForm);
    }

    
    
    
    public ProductLine getProduct() { return product; }
    public void setProduct( ProductLine product ) { this.product = product; }
    
}// class

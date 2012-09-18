package org.jboss.essc.web.pages;

import javax.inject.Inject;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jboss.essc.web.dao.ProductDaoBean;
import org.jboss.essc.web.dao.ReleaseDaoBean;
import org.jboss.essc.web.model.ProductLine;

/**
 *
 * @author Ondrej Zizka
 */
@SuppressWarnings("serial")
public class AddProductPage extends BaseLayoutPage {

    @Inject private ReleaseDaoBean prodRelDao;
    @Inject private ProductDaoBean prodDao;

    // Components
    private Form<ProductLine> insertForm;

    // Data
    private ProductLine product = new ProductLine();
    

    
    public AddProductPage(PageParameters params) {
        
        String proj = params.get("product").toOptionalString();
        
        add(new FeedbackPanel("feedback"));

        this.insertForm = new Form<ProductLine>("form") {
            @Override protected void onSubmit() {
                product = prodDao.addProductLine(product);
                setResponsePage(ProductPage.class, new PageParameters().add("name", product.getName()) );
            }
        };

        this.insertForm.add(new RequiredTextField<String>("name", new PropertyModel<String>(this.product, "name")));
        add(this.insertForm);
    }

    
    
    
    public ProductLine getProduct() { return product; }
    public void setProduct( ProductLine product ) { this.product = product; }
    
}
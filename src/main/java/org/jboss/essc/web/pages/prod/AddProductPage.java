package org.jboss.essc.web.pages.prod;

import org.jboss.essc.web.pages.prod.ProductPage;
import javax.inject.Inject;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jboss.essc.web.dao.ProductDaoBean;
import org.jboss.essc.web.dao.ReleaseDaoBean;
import org.jboss.essc.web.model.Product;
import org.jboss.essc.web.pages.BaseLayoutPage;

/**
 *
 * @author Ondrej Zizka
 */
@SuppressWarnings("serial")
public class AddProductPage extends BaseLayoutPage {

    @Inject private ReleaseDaoBean prodRelDao;
    @Inject private ProductDaoBean prodDao;

    // Components
    private Form<Product> insertForm;

    // Data
    private Product product = new Product();
    

    
    public AddProductPage(PageParameters params) {
        
        String proj = params.get("product").toOptionalString();
        
        add(new FeedbackPanel("feedback"));

        this.insertForm = new Form<Product>("form") {
            @Override protected void onSubmit() {
                product = prodDao.addProduct(product);
                setResponsePage(ProductPage.class, new PageParameters().add("name", product.getName()) );
            }
        };

        this.insertForm.add(new RequiredTextField<String>("name", new PropertyModel<String>(this.product, "name")));
        add(this.insertForm);
    }

    
    
    
    public Product getProduct() { return product; }
    public void setProduct( Product product ) { this.product = product; }
    
}
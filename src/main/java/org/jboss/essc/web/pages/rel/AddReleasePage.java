package org.jboss.essc.web.pages.rel;

import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jboss.essc.web.dao.ProductDaoBean;
import org.jboss.essc.web.dao.ReleaseDaoBean;
import org.jboss.essc.web.model.Product;
import org.jboss.essc.web.model.Release;
import org.jboss.essc.web.pages.BaseLayoutPage;
import org.jboss.logging.Logger;


/**
 *
 * @author Ondrej Zizka
 */
@SuppressWarnings("serial")
public class AddReleasePage extends BaseLayoutPage {

    @Inject private ReleaseDaoBean releaseDao;
    @Inject private ProductDaoBean prodDao;

    // Components
    private Form<Release> form;

    // Data
    private Product product;
    private String version;
    

    
    public AddReleasePage(Product product) {
        this.product = product;
        init(null);
    }
    
    public AddReleasePage(PageParameters params) {
        String prodName = params.get("product").toOptionalString();
        this.product = prodDao.findProductByName(prodName);
        init(prodName);
    }
    
    private void init( String prodName ){
        
        add(new Label("titleReleaseOf", prodName == null ? "" : " of " + prodName ));
        
        add(new FeedbackPanel("feedback"));

        // Form
        this.form = new Form<Release>("form") {
            @Override protected void onSubmit() {
                Release rel = releaseDao.addRelease( product, version );
                setResponsePage( ReleasePage.class, ReleasePage.createPageParameters( rel ) );
            }
        };

        // Product select
        this.form.add( new DropDownChoice("productSelect", new PropertyModel(this, "product"), new ProductsLDM() )
                .setChoiceRenderer( new ChoiceRenderer("name", "id") )
                .setRequired( true ) 
        );

        // Version
        this.form.add( new RequiredTextField<String>("version", new PropertyModel<String>(this, "version")));
        
        add(this.form);
    }

    
    
    
    public Product getProduct() { return product; }
    public void setProduct( Product product ) { this.product = product; }
    public String getVersion() { return version; }
    public void setVersion( String version ) { this.version = version; }

    
    // ProductsLDM 
    private class ProductsLDM extends LoadableDetachableModel<List<Product>> {

        public ProductsLDM() {
        }

        @Override
        protected List<Product> load() {
            List<Product> products = prodDao.getProducts_orderName(0);
            Logger.getLogger(AddReleasePage.class).info("Found products #: "  + products.size());
            return products;
        }
    }// class
    

}// class

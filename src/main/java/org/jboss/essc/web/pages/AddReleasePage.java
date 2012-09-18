package org.jboss.essc.web.pages;

import java.util.ArrayList;
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
import org.jboss.essc.web.model.ProductLine;
import org.jboss.essc.web.model.ProductRelease;
import org.jboss.logging.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Ondrej Zizka
 */
@SuppressWarnings("serial")
public class AddReleasePage extends BaseLayoutPage {

    @Inject private ReleaseDaoBean prodRelDao;
    @Inject private ProductDaoBean prodDao;

    // Components
    private Form<ProductRelease> insertForm;

    // Data
    private ProductLine product;
    private String version;
    

    
    public AddReleasePage(PageParameters params) {
        
        String proj = params.get("product").toOptionalString();
        add(new Label("titleReleaseOf", proj == null ? "" : " of " + proj ));
        
        add(new FeedbackPanel("feedback"));

        this.insertForm = new Form<ProductRelease>("form") {
            @Override protected void onSubmit() {
                ProductRelease rel = prodRelDao.addProductRelease( product, version );
                setResponsePage( ReleasePage.class, ReleasePage.createPageParameters( rel ) );
            }
        };

        this.insertForm.add( new RequiredTextField<String>("version", new PropertyModel<String>(this, "version")));
        this.insertForm.add( new DropDownChoice("productSelect", new PropertyModel<String>(this, "line"), new ProductsLDM() )
                .setChoiceRenderer( new ChoiceRenderer("name", "id") )   );
        
        add(this.insertForm);
    }

    
    
    
    public ProductLine getLine() { return product; }
    public void setLine( ProductLine product ) { this.product = product; }
    public String getVersion() { return version; }
    public void setVersion( String version ) { this.version = version; }

    
    // ProductsLDM 
    private class ProductsLDM extends LoadableDetachableModel<List<ProductLine>> {

        public ProductsLDM() {
        }

        @Override
        protected List<ProductLine> load() {
            List<ProductLine> products = prodDao.getProductLines_orderName(0);
            //LoggerFactory.getLogger(AddReleasePage.class).info("Found products #: "  + products.size());
            Logger.getLogger(AddReleasePage.class).info("Found products #: "  + products.size());
            return products;
        }
    }// class
    

}// class

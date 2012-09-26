package org.jboss.essc.web._cp.pagePanes;

import javax.inject.Inject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.jboss.essc.web._cp.links.ProductLink;
import org.jboss.essc.web.dao.ProductDaoBean;
import org.jboss.essc.web.model.Product;
import org.jboss.essc.web.pages.HomePage;
import org.jboss.essc.web.pages.LoginPage;
import org.jboss.essc.web.security.EsscAuthSession;


/**
 * @author Ondrej Zizka
 */
public class SidebarPanel extends Panel {

    @Inject private ProductDaoBean dao;
    
    
    public SidebarPanel( String id ) {
        super(id);
        this.setRenderBodyOnly( true );
        
        // User menu "box". Later it could be a real box.
        if( ((EsscAuthSession)getSession()).isSignedIn()  ){
            // Logout link
            add( new AjaxLink("loginLink") {
                    @Override public void onClick( AjaxRequestTarget target ) {
                        getSession().invalidateNow();
                        setResponsePage( HomePage.class );
                    }
                }
                .add( new Label("label", "Logout") )
            );
        } else {
            // Login link
            add( new BookmarkablePageLink("loginLink", LoginPage.class)
                .add( new Label("label", "Login / Register") ) );
        }
                
        add( new ListView<Product>("projects", dao.getProducts_orderName(0) ) {
            @Override protected void populateItem( ListItem<Product> item ) {
                item.add(new ProductLink("link", item.getModelObject()) );
            }
        } );
    }

}// class

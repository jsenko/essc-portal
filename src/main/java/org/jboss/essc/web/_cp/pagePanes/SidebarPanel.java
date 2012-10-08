package org.jboss.essc.web._cp.pagePanes;

import javax.inject.Inject;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.jboss.essc.web._cp.links.ProductLink;
import org.jboss.essc.web.dao.ProductDaoBean;
import org.jboss.essc.web.model.Product;
import org.jboss.essc.web.pages.user.LoginPage;
import org.jboss.essc.web.pages.user.UserPage;
import org.jboss.essc.web.security.EsscAuthSession;
import org.jboss.essc.web.util.MailSender;


/**
 * @author Ondrej Zizka
 */
public class SidebarPanel extends Panel {

    @Inject private ProductDaoBean dao;
    @Inject private MailSender mailSender;
    
    
    public SidebarPanel( String id ) {
        super(id);
        this.setRenderBodyOnly( true );
        
        EsscAuthSession sess = (EsscAuthSession)getSession();
        
        // User menu "box". Later it could be a real box.
        if( ! sess.isSignedIn()  ){
            // Login link
            add( new WebMarkupContainer("accountSettings").add( new Label("label", "").setVisible(false) ));
            add( new BookmarkablePageLink("loginLink", LoginPage.class)
                .add( new Label("label", "Login / Register") ) );
        } else {
            // Logout link
            add( new BookmarkablePageLink("accountSettings", UserPage.class).add( new Label("label", "Account Settings") ) );
            add( new Link("loginLink") {
                    @Override public void onClick() {
                        getSession().invalidate();
                    }
                }
                .add( new Label("label", "Logout " + sess.getUser().getName()) )
            );
        }
        
        // Product links
        add( new ListView<Product>("projects", dao.getProducts_orderName(0) ) {
            @Override protected void populateItem( ListItem<Product> item ) {
                item.add(new ProductLink("link", item.getModelObject()) );
            }
        } );
        
        // Quick message
        final TextArea ta = new TextArea("text");
        add( new Form("quickMessageForm"){
            @Override protected void onSubmit() {
                try {
                    mailSender.sendMail( "essc-list@redhat.com", "ESSC portal feedback", ta.getValue() );
                }
                catch( Exception ex ) {
                    error( "Can't send: " + ex.toString() );
                }
            }
        }.add( ta ));
        
    }// const

    
}// class

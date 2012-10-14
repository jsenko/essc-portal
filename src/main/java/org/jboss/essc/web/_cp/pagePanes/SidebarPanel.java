package org.jboss.essc.web._cp.pagePanes;

import java.awt.Color;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DefaultButtonImageResource;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.jboss.essc.web.CookieNames;
import org.jboss.essc.web._cp.links.ProductLink;
import org.jboss.essc.web.dao.ProductDaoBean;
import org.jboss.essc.web.model.Product;
import org.jboss.essc.web.pages.BaseLayoutPage;
import org.jboss.essc.web.pages.prod.AddProductPage;
import org.jboss.essc.web.pages.rel.AddReleasePage;
import org.jboss.essc.web.security.EsscAuthSession;
import org.jboss.essc.web.util.MailSender;


/**
 * @author Ondrej Zizka
 */
public class SidebarPanel extends Panel {

    @Inject private transient ProductDaoBean dao;
    @Inject private transient MailSender mailSender;
    
    
    public SidebarPanel( String id ) {
        super(id);
        this.setRenderBodyOnly( true );
        
        // User box
        add( new UserMenuBox("userBox") );
        
        // Administration
        add( new WebMarkupContainer("adminBox")
            .add( new BookmarkablePageLink("addReleaseLink",  AddReleasePage.class))
            .add( new BookmarkablePageLink("addProductLink",  AddProductPage.class))
        );
        
        // Products
        add( new BookmarkablePageLink("addProductLink", AddProductPage.class)
                .add( new Image("btn", new SharedResourceReference("btn.AddProduct") ) )
        );
        
        // Product links
        add( new ListView<Product>("products", dao.getProducts_orderName(0) ) {
            @Override protected void populateItem( ListItem<Product> item ) {
                item.add(new ProductLink("link", item.getModelObject()) );
            }
        } );
        
        // Settings
        add( new AjaxCheckBox("showInternalReleases") {
            @Override protected void onUpdate( AjaxRequestTarget target ) {
                target.add( getPage() );
                
                boolean val = StringUtils.isBlank( this.getValue() );
                
                EsscAuthSession sess = (EsscAuthSession)getSession();
                sess.getSettings().setShowInternalReleases( val );
                
                Cookie cookie = new Cookie(CookieNames.COOKIE_SHOW_INTERNAL_RELEASES, val ? "true" : "false");
                cookie.setPath("/");
                cookie.setMaxAge(Integer.MAX_VALUE);
                ((WebResponse)getRequestCycle().getResponse()).addCookie(cookie);
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

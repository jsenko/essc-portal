package org.jboss.essc.web._cp.pagePanes;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.jboss.essc.web.pages.user.LoginPage;
import org.jboss.essc.web.pages.user.UserPage;
import org.jboss.essc.web.security.EsscAuthSession;





/**
 * User menu box.
 * 
 * @author Ondrej Zizka
 */
public class UserMenuBox extends Panel {

    public UserMenuBox( String id ) {
        super( id );
        setRenderBodyOnly( false );
        
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
    }

    
}// class

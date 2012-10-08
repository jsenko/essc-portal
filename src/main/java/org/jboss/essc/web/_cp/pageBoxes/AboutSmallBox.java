package org.jboss.essc.web._cp.pageBoxes;

import javax.servlet.http.Cookie;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;


/**
 * About box for the HomePage.
 * 
 * @author Ondrej Zizka
 */
public class AboutSmallBox extends Panel {

    private final static String COOKIE_HIDE_ABOUT_BOX = "essc.hideAboutBox";

    
    public AboutSmallBox( String id ) {
        super(id);
        setOutputMarkupId( true );

        Cookie cookieHideAbout = ((WebRequest)getRequestCycle().getRequest()).getCookie(COOKIE_HIDE_ABOUT_BOX);
        if( cookieHideAbout != null )
            this.setVisibilityAllowed( false );
        

        // Hides the box and stores that info to cookies.
        add( new AjaxLink("dismiss"){

            @Override public void onClick( AjaxRequestTarget target ) {
                target.add( AboutSmallBox.this );
                AboutSmallBox.this.setVisible( false );
                Cookie cookie = new Cookie(COOKIE_HIDE_ABOUT_BOX, "true");
                cookie.setPath("/");
                cookie.setMaxAge(Integer.MAX_VALUE);
                ((WebResponse)getRequestCycle().getResponse()).addCookie(cookie);
            }
            
        });
        
    }// const
    
}// class

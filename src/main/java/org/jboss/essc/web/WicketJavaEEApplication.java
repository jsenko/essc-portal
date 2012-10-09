package org.jboss.essc.web;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import net.ftlines.wicket.cdi.CdiConfiguration;
import net.ftlines.wicket.cdi.ConversationPropagation;
import org.apache.wicket.*;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.component.IRequestableComponent;
import org.jboss.essc.web.model.User;
import org.jboss.essc.web.pages.HomePage;
import org.jboss.essc.web.pages.prod.AddProductPage;
import org.jboss.essc.web.pages.prod.ProductPage;
import org.jboss.essc.web.pages.rel.AddReleasePage;
import org.jboss.essc.web.pages.rel.ReleasePage;
import org.jboss.essc.web.pages.statics.AboutPage;
import org.jboss.essc.web.pages.statics.ErrorPage;
import org.jboss.essc.web.pages.statics.Http404;
import org.jboss.essc.web.pages.user.LoginPage;
import org.jboss.essc.web.pages.user.UserPage;
import org.jboss.essc.web.qualifiers.CurrentSession;
import org.jboss.essc.web.qualifiers.LoggedIn;
import org.jboss.essc.web.qualifiers.ShowInternals;
import org.jboss.essc.web.security.EsscAuthSession;
import org.jboss.essc.web.security.SecuredPage;


/**
 * Wicket application class.
 * 
 * @author Ondrej Zizka
 */
public class WicketJavaEEApplication extends WebApplication {

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

    
    @Override
    protected void init() {
        super.init();

        // Enable CDI
        BeanManager bm;
        try {
            bm = (BeanManager) new InitialContext().lookup("java:comp/BeanManager");
        } catch (NamingException e) {
            throw new IllegalStateException("Unable to obtain CDI BeanManager", e);
        }

        // Configure CDI, disabling Conversations as we aren't using them
        new CdiConfiguration(bm).setPropagation(ConversationPropagation.NONE).configure(this);

        // This would prevent Ajax components throwing an exception after session expiration.
        //this.getPageSettings().setRecreateMountedPagesAfterExpiry(false);
        
        //this.getApplicationSettings().setPageExpiredErrorPage(ErrorPage.class);
        this.getMarkupSettings().setStripWicketTags(true);
        //this.getResourceSettings().setThrowExceptionOnMissingResource( false ); // Fix: http://localhost:8080/essc-portal/release/EAP/HomePage.html?0
        //this.getPageSettings().setVersionPagesByDefault(false);
        
        
        // Mounts
        mountPage("/about", AboutPage.class);
        mountPage("/404",   Http404.class);
        mountPage("/login", LoginPage.class);
        mountPage("/user",  UserPage.class);
        
        mountPage("/addProduct",            AddProductPage.class);
        mountPage("/addRelease/#{product}", AddReleasePage.class);
        
        mountPage("/product/${name}",               ProductPage.class);
        mountPage("/release/${product}/${version}", ReleasePage.class);
     
        
        // Register the authorization strategy
        getSecuritySettings().setAuthorizationStrategy( new EsscAuthStrategy() );
        
    }// init()
    

    @Override
    public Session newSession( Request request, Response response ) {
        return new EsscAuthSession( request );
    }
    
    // CDI beans producers.
    @Produces @LoggedIn User getCurrentUser(){
        return ((EsscAuthSession) Session.get()).getUser();
    }

    @Produces @CurrentSession EsscAuthSession getCurrentSession(){
        return (EsscAuthSession) Session.get();
    }

    @Produces @ShowInternals Boolean isShowInternals(){
        User user = ((EsscAuthSession) Session.get()).getUser();
        return user != null && user.isShowProd();
    }

}// class




class EsscAuthStrategy implements IAuthorizationStrategy {

    public boolean isActionAuthorized( Component component, Action action ) {
        // authorize everything
        return true;
    }


    public <T extends IRequestableComponent> boolean isInstantiationAuthorized( Class<T> componentClass ) {
        // Check if the new Page requires authentication (implements the marker interface)
        if( SecuredPage.class.isAssignableFrom( componentClass ) ) {
            // Is user signed in?
            if( ((EsscAuthSession) Session.get()).isSignedIn() ) {
                return true;
            }

            // Intercept the request, but remember the target for later.
            // Invoke Component.continueToOriginalDestination() after successful logon to
            // continue with the target remembered.
            throw new RestartResponseAtInterceptPageException( LoginPage.class );
        }

        // okay to proceed
        return true;
    }

}

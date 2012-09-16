package org.jboss.essc.web;

import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import net.ftlines.wicket.cdi.CdiConfiguration;
import net.ftlines.wicket.cdi.ConversationPropagation;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.jboss.essc.web.pages.*;


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

        
        // Mounts
        mountPage("/about", AboutPage.class);
        mountPage("/addProduct", AddProductPage.class);
        mountPage("/addRelease", AddReleasePage.class);
        
        mountPage("/product/${name}", ProductPage.class);
        mountPage("/release/${product}/${version}", ReleasePage.class);
        
    }
    
}// class

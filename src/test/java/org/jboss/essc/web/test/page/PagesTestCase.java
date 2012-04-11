
package org.jboss.essc.web.test.page;

import org.apache.wicket.util.tester.WicketTester;
import org.jboss.essc.web.pages.AboutPage;
import org.jboss.essc.web.pages.BaseLayoutPage;
import org.jboss.essc.web.pages.HomePage;
import org.junit.Test;

/**
 *
 * @author Ondrej Zizka
 */
public class PagesTestCase {
    
    @Test
    public void testBaseLayoutPage(){
        WicketTester wt = new WicketTester(BaseLayoutPage.class);
        //WicketTester wt = new WicketTester( new WicketJavaEEApplication() );
        wt.startPage( new BaseLayoutPage() );
        wt.assertRenderedPage(BaseLayoutPage.class);
    }

    @Test
    public void testHomePage(){
        WicketTester wt = new WicketTester(HomePage.class);
        wt.startPage( new HomePage() );
        wt.assertRenderedPage(HomePage.class);
    }

    @Test
    public void testAboutPage(){
        WicketTester wt = new WicketTester(AboutPage.class);
        wt.startPage( new AboutPage() );
        wt.assertRenderedPage(AboutPage.class);
    }

}// class

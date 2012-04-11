package org.jboss.essc.web.pages;

import javax.inject.Inject;
import org.apache.wicket.markup.html.IHeaderResponse;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.resource.CssResourceReference;
import org.jboss.essc.web.dao.ContactDao;
import org.jboss.essc.web.model.Contact;

/**
 * Dynamic behavior for the ListContact page
 * 
 * @author Filippo Diotalevi
 */
@SuppressWarnings("serial")
public class AboutPage extends WebPage {


    // Set up the dynamic behavior for the page, widgets bound by id
    public AboutPage() {
    }
    
}

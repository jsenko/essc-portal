
package org.jboss.essc.web.pages;

import javax.inject.Inject;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.jboss.essc.web.dao.ContactDao;
import org.jboss.essc.web.model.Contact;

/**
 *
 * @author Ondrej Zizka
 */
@SuppressWarnings("serial")
public class InsertContact extends WebPage {
    
    private Form<Contact> insertForm;
    private String name;
    private String email;
    
    @Inject private ContactDao contactDao;

    
    public InsertContact() {
        add(new FeedbackPanel("feedback"));

        this.insertForm = new Form<Contact>("insertForm") {
            @Override protected void onSubmit() {
                contactDao.addContact(name, email);
                setResponsePage(ListContacts.class);
            }
        };

        this.insertForm.add(new RequiredTextField<String>("name", new PropertyModel<String>(this, "name")));
        this.insertForm.add(new RequiredTextField<String>("email", new PropertyModel<String>(this, "email")));
        add(this.insertForm);
    }

    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

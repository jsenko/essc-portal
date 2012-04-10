package org.jboss.essc.web.dao;

import java.util.List;
import javax.ejb.Local;
import org.jboss.essc.web.model.Contact;

/**
 *
 * @author Filippo Diotalevi
 */
@Local
public interface ContactDao {

    /**
     * Returns the currently available contacts.
     *
     * @return every contact in the database
     */
    public List<Contact> getContacts();

    /**
     * Returns a specific Contact from DB.
     *
     * @param id The Id for the Contact
     * @return The specified Contact object
     */
    public Contact getContact(Long id);

    /**
     * Persist a new Contact in the DB.
     *
     * @param name The name of the new Contact
     * @param email The e-mail address of the new Contact
     */
    public void addContact(String name, String email);

    /**
     * Removes a specific item from the DB.
     *
     * @param modelObject The specific Contact object, which we wants to remove
     */
    public void remove(Contact modelObject);
}

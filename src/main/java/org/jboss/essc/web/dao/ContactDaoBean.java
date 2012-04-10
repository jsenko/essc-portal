package org.jboss.essc.web.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.essc.web.model.Contact;

/**
 * A bean which manages Contact entities.
 */
@Stateless
public class ContactDaoBean implements ContactDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    @SuppressWarnings("unchecked")
    public List<Contact> getContacts() {
        return this.em.createQuery("SELECT c FROM Contact c").getResultList();
    }

    /**
     * Get Contact by ID.
     */
    @Override
    public Contact getContact(Long id) {
        return this.em.find(Contact.class, id);
    }

    /**
     * Add a new Contact.
     */
    @Override
    public void addContact(String name, String email) {
        this.em.merge(new Contact(null, name, email));
    }

    /**
     * Remove a Contact.
     */
    @Override
    public void remove(Contact modelObject) {
        Contact managed = this.em.merge(modelObject);
        this.em.remove(managed);
        this.em.flush();
    }
    
}

package org.jboss.essc.web.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.essc.web.model.Contact;
import org.jboss.essc.web.model.ProductLine;
import org.jboss.essc.web.model.ProductRelease;

/**
 * A bean which manages Contact entities.
 */
@Stateless
public class ProductReleaseDaoBean {

    @PersistenceContext
    private EntityManager em;


    @SuppressWarnings("unchecked")
    public List<ProductRelease> getProductReleases_orderName(int limit) {
        return this.em.createQuery("SELECT p FROM Product p ORDER BY p.line.name").getResultList();
    }

    /**
     * Get Contact by ID.
     */
    public ProductRelease getProductRelease(Long id) {
        return this.em.find(ProductRelease.class, id);
    }

    /**
     * Add a new Contact.
     */
    public void addProductRelease(ProductLine line, String version) {
        this.em.merge( new ProductRelease( null, line, version ) );
    }

    /**
     * Remove a Contact.
     */
    public void remove(Contact modelObject) {
        Contact managed = this.em.merge(modelObject);
        this.em.remove(managed);
        this.em.flush();
    }
    
}

package org.jboss.essc.web.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.essc.web.model.Product;


/**
 * A bean which manages Contact entities.
 */
@Stateless
public class ProductDaoBean {

    @PersistenceContext
    private EntityManager em;


    public List<Product> getProducts_orderName(int limit) {
        return this.em.createQuery("SELECT pl FROM Product pl ORDER BY pl.name").getResultList();
    }

    /**
     * Get ProductLine by ID.
     */
    public Product getProductLine(Long id) {
        return this.em.find(Product.class, id);
    }

    /**
     * Get ProductLine by name.
     */
    public Product getProductByName( String name ) {
        return this.em.createQuery("SELECT pl FROM Product pl WHERE pl.name = ?", Product.class).setParameter(1, name).getSingleResult();
    }

    /**
     * Add a new ProductLine.
     */
    public Product addProductLine( String name) {
        return this.em.merge( new Product( null, name ) );
    }

    /**
     * Add a new ProductLine.
     */
    public Product addProduct( Product prod ) {
        return this.em.merge( prod );
    }

    /**
     * Remove a ProductLine.
     */
    public void remove(Product pl) {
        Product managed = this.em.merge(pl);
        this.em.remove(managed);
        this.em.flush();
    }

    public Product update( Product product ) {
        Product managed = this.em.merge(product);
        return managed;
    }
    
}

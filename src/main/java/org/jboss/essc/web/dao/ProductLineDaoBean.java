package org.jboss.essc.web.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.essc.web.model.ProductLine;


/**
 * A bean which manages Contact entities.
 */
@Stateless
public class ProductLineDaoBean {

    @PersistenceContext
    private EntityManager em;


    public List<ProductLine> getProductLines_orderName(int limit) {
        return this.em.createQuery("SELECT pl FROM ProductLine pl ORDER BY pl.name").getResultList();
    }

    /**
     * Get ProductLine by ID.
     */
    public ProductLine getProductLine(Long id) {
        return this.em.find(ProductLine.class, id);
    }

    /**
     * Add a new ProductLine.
     */
    public ProductLine addProductLine( String name) {
        return this.em.merge( new ProductLine( null, name ) );
    }

    /**
     * Add a new ProductLine.
     */
    public ProductLine addProductLine( ProductLine prod ) {
        return this.em.merge( prod );
    }

    /**
     * Remove a ProductLine.
     */
    public void remove(ProductLine pl) {
        ProductLine managed = this.em.merge(pl);
        this.em.remove(managed);
        this.em.flush();
    }

    public ProductLine update( ProductLine product ) {
        ProductLine managed = this.em.merge(product);
        return managed;
    }
    
}// class

package org.jboss.essc.web.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
        //return this.em.createQuery("SELECT pr FROM ProductRelease pr ORDER BY pr.line.name").getResultList();
        //return this.em.createQuery("SELECT pr FROM ProductRelease pr LEFT JOIN ProductLine pl ORDER BY pl.name").getResultList(); // ON pr.line=pl 
        return this.em.createQuery("SELECT pr FROM ProductRelease pr LEFT JOIN pr.line pl ORDER BY pl.name").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<ProductRelease> getProductReleases_orderDateDesc(int limit) {
        return this.em.createQuery("SELECT pr FROM ProductRelease pr ORDER BY pr.plannedFor DESC").getResultList();
    }

    public List<ProductRelease> getProductReleasesOfLine(ProductLine line) {
        return this.em.createQuery("SELECT pr FROM ProductRelease pr WHERE pr.line=? ORDER BY pr.line.name").setParameter(1, line).getResultList();
    }

    
    /**
     * Get ProductRelease by ID.
     */
    public ProductRelease getProductRelease(Long id) {
        return this.em.find(ProductRelease.class, id);
    }
    
    /**
     *  Get release by product name and version.
     */
    public ProductRelease getProductRelease( String prodName, String version ) {
        return this.em.createQuery("SELECT pr FROM ProductRelease pr WHERE pr.line.name = ? AND pr.version = ?", ProductRelease.class)
                .setParameter(1, prodName)
                .setParameter(2, version)
                .getSingleResult();
    }
    

    /**
     * Add a new ProductRelease.
     */
    public ProductRelease addProductRelease( ProductLine line, String version) {
        return this.em.merge( new ProductRelease( null, line, version ) );
    }

    /**
     * Update a ProductRelease.
     */
    public ProductRelease update(ProductRelease pr) {
        ProductRelease managed = this.em.merge(pr);
        return managed;
    }

    /**
     * Remove a ProductRelease.
     */
    public void remove(ProductRelease pr) {
        ProductRelease managed = this.em.merge(pr);
        this.em.remove(managed);
        this.em.flush();
    }

    
}// class

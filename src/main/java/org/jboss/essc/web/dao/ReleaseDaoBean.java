package org.jboss.essc.web.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.essc.web.model.Product;
import org.jboss.essc.web.model.Release;

/**
 * A bean which manages Contact entities.
 */
@Stateless
public class ReleaseDaoBean {

    @PersistenceContext
    private EntityManager em;


    @SuppressWarnings("unchecked")
    @Deprecated // Non-sense.
    public List<Release> getProductReleases_orderName(int limit) {
        //return this.em.createQuery("SELECT pr FROM ProductRelease pr ORDER BY pr.product.name").getResultList();
        //return this.em.createQuery("SELECT pr FROM ProductRelease pr LEFT JOIN ProductLine pl ORDER BY pl.name").getResultList();
        return this.em.createQuery("SELECT pr FROM ProductRelease pr LEFT JOIN pr.product pl ORDER BY pl.name").getResultList();
    }

    public List<Release> getProductReleases_orderDateDesc(int limit) {
        return this.em.createQuery("SELECT pr FROM ProductRelease pr ORDER BY pr.plannedFor DESC").getResultList();
    }

    public List<Release> getProductReleasesOfLine(Product prod) {
        return this.em.createQuery("SELECT pr FROM ProductRelease pr WHERE pr.product = ? ORDER BY pr.version DESC").setParameter(1, prod).getResultList();
    }

    
    /**
     * Get ProductRelease by ID.
     */
    public Release getProductRelease(Long id) {
        return this.em.find(Release.class, id);
    }
    
    /**
     *  Get release by product name and version.
     */
    public Release getProductRelease( String prodName, String version ) {
        return this.em.createQuery("SELECT pr FROM ProductRelease pr WHERE pr.product.name = ? AND pr.version = ?", Release.class)
                .setParameter(1, prodName)
                .setParameter(2, version)
                .getSingleResult();
    }
    

    /**
     * Add a new ProductRelease.
     */
    public Release addProductRelease( Product product, String version) {
        return this.em.merge( new Release( null, product, version ) );
    }

    /**
     * Update a ProductRelease.
     */
    public Release update(Release pr) {
        Release managed = this.em.merge(pr);
        return managed;
    }

    /**
     * Remove a ProductRelease.
     */
    public void remove(Release pr) {
        Release managed = this.em.merge(pr);
        this.em.remove(managed);
        this.em.flush();
    }

    
}
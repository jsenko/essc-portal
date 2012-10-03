package org.jboss.essc.web.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
    public List<Release> getReleases_orderName(int limit) {
        //return this.em.createQuery("SELECT rel FROM Release rel ORDER BY rel.product.name").getResultList();
        //return this.em.createQuery("SELECT rel FROM Release rel LEFT JOIN Product prod ORDER BY prod.name").getResultList();
        return this.em.createQuery("SELECT rel FROM Release rel LEFT JOIN rel.product prod ORDER BY prod.name").getResultList();
    }

    public List<Release> getReleases_orderDateDesc(int limit) {
        return this.em.createQuery("SELECT rel FROM Release rel ORDER BY rel.plannedFor DESC").getResultList();
    }

    public List<Release> getReleasesOfProduct(Product prod) {
        return this.em.createQuery("SELECT rel FROM Release rel WHERE rel.product = ? ORDER BY rel.version DESC").setParameter(1, prod).getResultList();
    }

    public List<Release> getReleasesOfProduct(String prodName) {
        return this.em.createQuery("SELECT rel FROM Release rel WHERE rel.product.name = ? ORDER BY rel.version DESC").setParameter(1, prodName).getResultList();
    }

    
    /**
     * Get Release by ID.
     */
    public Release getRelease(Long id) {
        return this.em.find(Release.class, id);
    }
    
    /**
     *  Get Release by product name and version.
     */
    public Release getRelease( String prodName, String version ) {
        return this.em.createQuery("SELECT rel FROM Release rel WHERE rel.product.name = ? AND rel.version = ?", Release.class)
                .setParameter(1, prodName)
                .setParameter(2, version)
                .getSingleResult();
    }
    
    
    /**
     *  Does a release exist?
     */
    public boolean exists( String prodName, String version ){
        return this.em.createQuery("SELECT COUNT(*) FROM Release rel WHERE rel.product.name = ? AND rel.version = ?", Long.class)
                .setParameter(1, prodName)
                .setParameter(2, version)
                .getSingleResult() != 0;
    }

    /**
     * Add a new Release.
     */
    public Release addRelease( Product product, String version ) {
        // Verify that the relese doesn't exist yet.
        // TODO: use exists().
        try {
            getRelease( product.getName(), version );
            throw new IllegalArgumentException("Release already exists: " + product.getName() + " " + version );
        } catch (NoResultException ex){ /* OK */ }
        
        Release rel = new Release( null, product, version );
        return this.em.merge( rel );
    }

    /**
     * Update a Release.
     */
    public Release update(Release rel) {
        Release managed = this.em.merge(rel);
        return managed;
    }

    /**
     * Remove a Release.
     */
    public void remove(Release rel) {
        Release managed = this.em.merge(rel);
        this.em.remove(managed);
        this.em.flush();
    }

    
}
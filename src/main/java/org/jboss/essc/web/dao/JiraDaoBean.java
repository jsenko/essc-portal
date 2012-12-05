package org.jboss.essc.web.dao;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.essc.web.model.JiraProject;
import org.jboss.essc.web.model.JiraVersion;

@Stateless
public class JiraDaoBean
{
    // delay between database (cache) updates
	private final long delay = 1000 * 10 * 60 * 60 * 24; // a day
	
	@PersistenceContext
	private EntityManager em;
	
	
	private ObjectMapper mapper;
	
	@PostConstruct
	public void init()
	{
		mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS, true);
	}
	
	
	public JiraProject getProject(String key)
	{
		JiraProject p = null;
		try
		{
			p = em.createQuery("select p from JiraProject p where p.key = :key", JiraProject.class)
				.setParameter("key", key).getSingleResult();
		}
		catch(NoResultException e){ /* ok */ }
		
		if(p == null) // project not in cache
		{
			//get the project
			try
			{
				p = mapper.readValue(new URL("https://issues.jboss.org/rest/api/2/project/"+key), JiraProject.class);
			}
			catch (Exception e)
			{	
				return null; // failure
			}
			
			p.setLastUpdated(System.currentTimeMillis());
			
			// first persist all versions
			for(JiraVersion v: p.getVersions())
			{
				v.setProject(p);
				em.persist(v);
			}
			
			// and the project itself			
			em.persist(p);
		}
		else if(p.getLastUpdated() + delay < System.currentTimeMillis())
		{
			/*  force cache update by deleting all cached data for the specified project
			 *  TODO optimize by updated only changed data
			 */
			em .createQuery("delete from JiraVersion v where v.project = :project")
				.setParameter("project", p)
				.executeUpdate();
			
			em .createQuery("delete from JiraProject p where p.key = :key")
				.setParameter("key", key)
				.executeUpdate();
		}
		
		return p;
	}
	
	
	/**
	 * Get list of ids of versions of the specified project that are marked as released
	 * @param key key of the project
	 * @return list of ids or null when there are no versions or an error occured
	 */
	public List<Long> getReleasedIds(String key)
	{
	    JiraProject p = getProject(key);
	    if(p == null) return null;
	    @SuppressWarnings("unchecked")
        List<Long> ids = em.createQuery("select v.jiraId from JiraVersion v where v.project = :project and v.released = true")
	        .setParameter("project", p)
	        .getResultList();
	    
	    return ids;
	}

	
	/**
	 * Get list of version names for the given project.
	 * @param key
	 * @return list of version strings or null on failure
	 */
	public List<String> getVersionNames(String key)
	{
		JiraProject p = getProject(key);
		if(p == null) return null;
        @SuppressWarnings("unchecked")
        List<String> versionNames = em.createQuery("select v.name from JiraVersion v where v.project = :project")
            .setParameter("project", p)
            .getResultList();
        
        return versionNames;
	}
}

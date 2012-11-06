package org.jboss.essc.web.dao;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
	private final long delay = 1000 * 10;// * 60 * 60 * 24;
	
	@PersistenceContext
	private EntityManager em;
	
	
	private final ObjectMapper mapper;
	
	
	public JiraDaoBean()
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

	
	
	public List<String> getVersionStrings(String key)
	{
		JiraProject p = getProject(key);
		if(p == null) return null;
		List<String> strings = new ArrayList<String>();
		for(JiraVersion v: p.getVersions())
		{
			strings.add(v.getName());
		}
		return strings;
	}
}

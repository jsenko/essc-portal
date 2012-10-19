package org.jboss.essc.web.util;

import java.net.URL;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;

public class Jira
{
	
	private ObjectMapper mapper;
	
	
	public Jira()
	{
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS, true);
	}
	
	
	public Project getProject(String key)
	{
		try
		{
			return mapper.readValue(new URL("https://issues.jboss.org/rest/api/2/project/AS7"), Project.class);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}

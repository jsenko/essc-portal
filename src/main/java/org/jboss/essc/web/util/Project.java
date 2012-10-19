package org.jboss.essc.web.util;

import java.util.List;

public class Project
{
	//private String self;
	
	public long id;
	
	//private String key;
	
	private List<Version> versions;
	
	
	public long getId()
	{
		return id;
	}
	
	
	public List<Version> getVersions()
	{
		return versions;
	}
}

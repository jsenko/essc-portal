package org.jboss.essc.web.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class JiraProject
{
	@Id
	private Long id; 	// value from jira
	
	@Column(unique = true, nullable = false)
	private String key;
	
	private String name;
	
	@OneToMany(mappedBy = "project")
	private List<JiraVersion> versions;
	
	@JsonIgnore
	private long lastUpdated;
	

	public List<JiraVersion> getVersions() { return versions; }
	public void setVersions(List<JiraVersion> versions) { this.versions = versions; }
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getKey() { return key; }
	public void setKey(String key) { this.key = key; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public long getLastUpdated() { 	return lastUpdated; }
	public void setLastUpdated(long lastUpdated) { 	this.lastUpdated = lastUpdated; }
	
	
	@Override
	public int hashCode()
	{
		return (id == null) ? 0 : id.hashCode();
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JiraProject other = (JiraProject) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

	@Override
	public String toString() {
		return "JiraProject {id=" + id
				+ ", key=" + key
				+ ", name=" + name
				+ ", lastUpdated=" + lastUpdated
				+ "}";
	}
}

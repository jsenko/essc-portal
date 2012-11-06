package org.jboss.essc.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

@Entity
public class JiraVersion
{
	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonProperty("id")
	private long jiraId;
	
	@JsonIgnore
	@ManyToOne
	private JiraProject project;
	
	@Column(unique = true, nullable = false)
	private String name;
	
	
	private boolean released;


	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public JiraProject getProject() { return project; }
	public void setProject(JiraProject project) { this.project = project; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public boolean isReleased() { return released; }
	public void setReleased(boolean released) { this.released = released; }	
	public long getJiraId() { return jiraId; }
	public void setJiraId(long jiraId) { this.jiraId = jiraId; }
	
	
	@Override
	public String toString()
	{
		return "JiraVersion {id=" + id
				+ ", jiraId=" + jiraId
				+ ", project=" + project
				+ ", name=" + name
				+ ", released=" + released
				+ "}";
	}
	
	@Override
	public int hashCode()
	{
		return (id == null) ? 0 : id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JiraVersion other = (JiraVersion) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

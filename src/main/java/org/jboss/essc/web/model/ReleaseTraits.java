
package org.jboss.essc.web.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import javax.persistence.Embeddable;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;


/**
 *  Traits - used for 1) product trait templates 2) release trait values.
 * 
 * @author ozizka@redhat.com
 */
@Embeddable
public class ReleaseTraits implements Serializable, Cloneable {

    // Source links
    private String gitHash;
    
    // Files links
    private String linkReleasedBinaries;
    private String linkReleasedDocs;
    private String linkStagedBinaries;
    private String linkStagedDocs;
    private String linkMavenLocalRepo;
    private String linkJavadoc;
    
    // Info links
    private String linkIssuesFixed;
    private String linkIssuesFound;
    
    // Build links
    private String linkBuildHowto;
    private String linkGitRepo;
    private String linkMead;
    private String linkBrew;
    
    // Test links
    private String linkMeadJob;
    private String linkTattleTale;
    private String linkCodeCoverage;
    private String linkTck;
    private String linkCC;
    private String link508;
    private String linkJavaEE;


    public void replaceTemplatesTokens( String token, String val ){
        // Iterate over fields and copy those of matching names.
        Field[] fields = this.getClass().getDeclaredFields(); // Only gives public fields!
        for( Field field : fields ) {
            try {
                if( field.getType() != String.class )  continue;
                String tpl = (String) field.get(this);
                if( tpl == null )  continue;
                field.set( this, tpl.replace( token, val ) );
            }
            catch( Exception ex ) { }
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Get/Set">
    public String getGitHash() { return gitHash; }
    public void setGitHash( String gitHash ) { this.gitHash = gitHash; }
    
    // Build
    public String getLinkBuildHowto() { return linkBuildHowto; }
    public void setLinkBuildHowto( String linkBuildHowto ) { this.linkBuildHowto = linkBuildHowto; }
    public String getLinkGitRepo() { return linkGitRepo; }
    public void setLinkGitRepo( String linkGitRepo ) { this.linkGitRepo = linkGitRepo; }
    public String getLinkBrew() { return linkBrew; }
    public void setLinkBrew( String linkBrew ) { this.linkBrew = linkBrew; }
    public String getLinkMead() { return linkMead; }
    public void setLinkMead( String linkMead ) { this.linkMead = linkMead; }
        
    // Issues
    public String getLinkIssuesFixed() { return linkIssuesFixed; }
    public void setLinkIssuesFixed( String linkIssuesFixed ) { this.linkIssuesFixed = linkIssuesFixed; }
    public String getLinkIssuesFound() { return linkIssuesFound; }
    public void setLinkIssuesFound( String linkIssuesFound ) { this.linkIssuesFound = linkIssuesFound; }
    
    // Files
    public String getLinkMavenLocalRepo() { return linkMavenLocalRepo; }
    public void setLinkMavenLocalRepo( String linkMavenLocalRepo ) { this.linkMavenLocalRepo = linkMavenLocalRepo; }
    public String getLinkReleasedBinaries() { return linkReleasedBinaries; }
    public void setLinkReleasedBinaries( String linkReleasedBinaries ) { this.linkReleasedBinaries = linkReleasedBinaries; }
    public String getLinkReleasedDocs() { return linkReleasedDocs; }
    public void setLinkReleasedDocs( String linkReleasedDocs ) { this.linkReleasedDocs = linkReleasedDocs; }
    public String getLinkStagedBinaries() { return linkStagedBinaries; }
    public void setLinkStagedBinaries( String linkStagedBinaries ) { this.linkStagedBinaries = linkStagedBinaries; }
    public String getLinkStagedDocs() { return linkStagedDocs; }
    public void setLinkStagedDocs( String linkStagedDocs ) { this.linkStagedDocs = linkStagedDocs; }
    
    // Tests
    public String getLinkCodeCoverage() { return linkCodeCoverage; }
    public void setLinkCodeCoverage( String linkCodeCoverage ) { this.linkCodeCoverage = linkCodeCoverage; }
    public String getLinkMeadJob() { return linkMeadJob; }
    public void setLinkMeadJob( String linkMeadJob ) { this.linkMeadJob = linkMeadJob; }
    public String getLinkTattleTale() { return linkTattleTale; }
    public void setLinkTattleTale( String linkTattleTale ) { this.linkTattleTale = linkTattleTale; }
    
    public String getLinkTck() { return linkTck; }
    public void setLinkTck( String linkTck ) { this.linkTck = linkTck; }
    public String getLinkJavaEE() { return linkJavaEE; }
    public void setLinkJavaEE( String linkJavaEE ) { this.linkJavaEE = linkJavaEE; }
    public String getLink508() { return link508; }
    public void setLink508( String link508 ) { this.link508 = link508; }
    public String getLinkCC() { return linkCC; }
    public void setLinkCC( String linkCC ) { this.linkCC = linkCC; }
    //</editor-fold>

    @Override
    protected ReleaseTraits clone() {
        byte[] bytes = SerializationUtils.serialize( this );
        return (ReleaseTraits) SerializationUtils.deserialize( bytes );
    }

    
}// class

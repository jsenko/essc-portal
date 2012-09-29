
package org.jboss.essc.web._cp.links;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.time.Duration;
import org.jboss.essc.web.util.PropertiesUtils;


/**
 *
 * @author ozizka@redhat.com
 */
public class PropertiesDownloadLink extends DownloadLink {

    public PropertiesDownloadLink( String id, final Object obj, String fileName ) {
        super( id, new AbstractReadOnlyModel<File>() {
            @Override public File getObject() {
                
                String propsString = PropertiesUtils.getPropertiesAsString(obj);
                
                try {
                    File tempFile = File.createTempFile( "essc-web-", ".properties" );
                    InputStream data = new ByteArrayInputStream( propsString.getBytes() );
                    Files.writeTo( tempFile, data );
                    return tempFile;
                }
                catch( IOException e ) {
                    throw new RuntimeException( e );
                }
            }
        } );
        this.setCacheDuration( Duration.NONE );
        this.setDeleteAfterDownload( true );
    }

}// class

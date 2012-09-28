
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


/**
 *
 * @author ozizka@redhat.com
 */
public class PropertiesDownloadLink extends DownloadLink {

    public PropertiesDownloadLink( String id, final Object obj, String fileName ) {
        super( id, new AbstractReadOnlyModel<File>() {
            @Override public File getObject() {
                
                String propsString = getPropertiesAsString(obj);
                
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

    
    
    /**
     *  Goes through all getters and converts them to a properties format -  one line per getter.
     */
    public static String getPropertiesAsString( Object obj ) {
        StringBuilder sb = new StringBuilder(1024);
        
        for( Method method : obj.getClass().getMethods() ){
            if( 0 != method.getParameterTypes().length )  continue;
            if( ! ( method.getReturnType().isPrimitive() || String.class.equals( method.getReturnType() ) ) )  continue;
            
            String name = method.getName();
            int start;
            if( name.startsWith("get") && name.length() > 3 )  start = 3;
            else if( method.getName().startsWith("is") && name.length() > 2 )  start = 2;
            else continue;

            sb.append( name.substring(start,start+1).toLowerCase() );
            sb.append( name.substring(start+1) );
            sb.append("=");
            try {
                sb.append( method.invoke( obj ) );
            }
            catch( Exception ex ) {
                sb.append( ex.toString().replace("\n", " ") );
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }

}

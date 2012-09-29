
package org.jboss.essc.web.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;


/**
 *
 * @author ozizka@redhat.com
 */
public class PropertiesUtils {

    
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

    
    /**
     *  Applies properties to given object.
     *  @throws IllegalArgumentException if any argument is null
     */
    public static void applyOnObjectStrict( Object obj, Properties props ) throws IllegalArgumentException {
        if( null == obj )    throw new IllegalArgumentException("obj can't be null.");
        if( null == props )  throw new IllegalArgumentException("props can't be null.");
        applyOnObject( obj, props );
    }
    
    /**
     *  Applies properties to given object.
     *  If any argument is null, does nothing.
     *  Assignment or parsing exceptions are logged.
     */
    public static void applyOnObject( Object obj, Properties props ) {
        if( null == obj || null == props )  return;
        
        // Create map of setters.
        Map<String, Method> methods = new HashMap();
        
        for( Method m : obj.getClass().getMethods() ){
            if( m.getParameterTypes().length != 1 )  continue;
            if( ! m.getName().startsWith("set") )  continue;
            
            Class paramType = m.getParameterTypes()[0];
            boolean paramTypeOK = String.class.equals( paramType );
            paramTypeOK |= Integer.class.equals( paramType );
            paramTypeOK |= Long.class.equals( paramType );
            if( paramTypeOK )
                methods.put( StringUtils.uncapitalize( m.getName().substring(3) ), m);
        }
        
        // Apply properties on matching setters.
        for( Map.Entry e : props.entrySet() ){
            
            Method m = methods.get(e.getKey());
            if( m == null ) continue;

            // Cast
            String val = (String) e.getValue();
            Class paramType = m.getParameterTypes()[0];
                
            try {
                if( String.class.equals(paramType) )
                    m.invoke( obj, val );
                
                if( Integer.class.equals(paramType) )
                    m.invoke( obj, Integer.parseInt(val) );
                
                if( Long.class.equals(paramType) )
                    m.invoke( obj, Long.parseLong(val) );
            }
            catch( Exception ex ) {
                // TODO: Log it.
            }
        }
    }// applyOnObject()

}// class

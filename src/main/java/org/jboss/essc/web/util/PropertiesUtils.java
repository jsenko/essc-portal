
package org.jboss.essc.web.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map.Entry;
import java.util.*;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 *
 * @author ozizka@redhat.com
 */
public class PropertiesUtils {

    
    /**
     *  Goes through all getters and converts them to a properties format -  one line per getter.
     *  On exception, a commented line is added; e.g.: # foo = ex.toString()
     */
    public static String getPropertiesAsString( Object obj ) {
        return getPropertiesAsString( obj, false );
    }
    public static String getPropertiesAsString( Object obj, boolean deep ) {
        StringBuilder sb = new StringBuilder(1024);
        Set visited = new HashSet();
        appendPropertiesAsString( obj, sb, visited, deep, "" );
        return sb.toString();
    }
    
    /**
     *  Recursive method for printing the object tree.
     * 
     * @param obj      Subject to print.
     * @param sb       StringBuilder to print to.
     * @param visited  A set of visited objects to prevent infinite loop.
     * @param deep     Recurse into non-primitive members.
     * @param prefix   Current prefix (when recursing, it's parents names, e.g. "invoice.item").
     */
    private static void appendPropertiesAsString( Object obj, StringBuilder sb, Set visited, boolean deep, final String prefix ) {
        if( obj == null || sb == null || visited == null || prefix == null )
            throw new IllegalArgumentException("Nothing can be null");
        
        if( visited.contains( obj ) )  return;
        visited.add( obj );
        
        // TODO: Support for List, Map etc?
        
        // Find getters and print them, or traverse into the returned object.
        for( Method method : obj.getClass().getMethods() ){
            if( ! Modifier.isPublic( method.getModifiers() ) )  continue;
            if( 0 != method.getParameterTypes().length )  continue;
            
            // Name
            String name = method.getName();
            int start;
            if( name.startsWith("get") && name.length() > 3 )  start = 3;
            else if( method.getName().startsWith("is") && name.length() > 2 )  start = 2;
            else continue;

            name = name.substring(start,start+1).toLowerCase() + name.substring(start+1); // property name
            
            String valStr;
            try {
                Object val = method.invoke( obj );
                Class type = method.getReturnType();
                // String or primitive.
                if( type.isPrimitive() || String.class.equals( type ) ){
                    valStr = ObjectUtils.toString( val );
                }
                // Complex object.
                else if( List.class.isAssignableFrom(type) ){
                    //sb.append("# Lists are not supported. ");
                    //valStr = "[...]";
                    String listPrefix = prefix + name + "[].";
                    for( Object item : (List) val ){
                        appendPropertiesAsString( item, sb, visited, deep, listPrefix );
                    }
                    continue; // Don't create a line for this object.
                }
                else if( Map.class.isAssignableFrom(type) ){
                    //sb.append("# Maps are not supported. ");
                    //valStr = "{...}";
                    String mapPrefixA = prefix + name + "[";
                    Set<Map.Entry> entrySet = ((Map) val).entrySet();
                    for( Map.Entry e : entrySet ){
                        appendPropertiesAsString( e.getValue(), sb, visited, deep, mapPrefixA + e.getKey() + "]." );
                    }
                    continue; // Don't create a line for this object.
                }
                else {
                    if( ! deep )  continue;
                    // Recurse.
                    appendPropertiesAsString( val, sb, visited, deep, prefix + name + "." );
                    continue; // Don't create a line for this object.
                }
            }
            catch( Exception ex ) {
                sb.append("# ");
                valStr = ex.toString().replace("\n", " ");
            }
            sb.append(prefix).append(name).append("=").append(valStr).append("\n");
        }
    }// appendPropertiesAsString()

    
    
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
     *  Applies properties to given object. Does not support traversing.
     *  If any argument is null, does nothing.
     *  Assignment or parsing exceptions are logged.
     */
    public static void applyOnObject( Object obj, Properties props ) {
        if( null == obj || null == props )  return;
        
        // Create map of setters.
        Map<String, Method> methods = ReflectionHelpers.createSettersMap(obj.getClass());
        
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

    
    
    
    /**
     *  Recursively applies properties to given object - supports "foo.bar".
     *  If any argument is null, does nothing.
     *  Assignment or parsing exceptions are logged.
     * 
     *  @throws  Rethrows all exceptions caught when writing to the object's properties.
     */
    public static void applyOnObjectRecursive( Object obj, Properties props ) throws Exception {
        if( null == obj || null == props )  return;
        
        // Create map of setters.
        Map<String, Method> setters = ReflectionHelpers.createSettersMap(obj.getClass());
        
        List<Exception> exs = new LinkedList();
        
        // Apply properties on matching setters.
        for( Map.Entry e : props.entrySet() ){
            try {
                applyOnObject( obj, e, setters );
            }
            catch( Exception ex ) {
                exs.add( ex );
            }
        }
        
        throw new Exception( listOfExceptionsAsString( exs ) );
        
    }// applyOnObject()

    
    /**
     *  Applies a single property to given object.
     * @param obj
     * @param setters - Optimization.
     * @returns  True if the property was found and set.
     */
    private static boolean applyOnObject( Object obj, final Entry e, Map<String, Method> setters) throws Exception {
        if( setters == null)
            setters = ReflectionHelpers.createSettersMap( obj );
        
        final String def = (String) e.getKey();
        boolean isMultiLevel = def.contains(".");
        final String head = StringUtils.substringBefore( def, ".");
        boolean isList = head.contains("[");
        final String name = StringUtils.substringBefore( def, "[");
        
        if( isList )  throw new UnsupportedOperationException("Lists and maps are read only.");
        
        if( ! isMultiLevel ){
            Method setter = setters.get( name );
            if( null == setter ) return false;
            try {
                setters.get( name ).invoke( obj, (String) e.getValue() );
            }
            catch( Exception ex ) {
                throw new Exception("Could not set " + def + ": " + ex.toString(), ex);
            }
            return true;
        }
        
        // Multilevel
        Method getter = ReflectionHelpers.findGetterForProperty( obj.getClass(), head );
        if( getter == null )  return false;
        
        Object child;
        try {
            child = getter.invoke(obj);
        }
        catch( Exception ex ) {
            throw new Exception("Could not get " + name + ": " + ex.toString(), ex);
        }
        
        final String tail = StringUtils.substringAfter(def, ".");
        
        Map.Entry childEntry = new AbstractMap.SimpleEntry<String, String>( tail, (String)e.getValue() );
        applyOnObject( obj, childEntry, null );
        
        return true;
    }
    
    
    
    
    
    public static class ReflectionHelpers {
        /**
        * @returns  A map  of  "POJO propertyName" -> setter Method.
        */
        public static Map<String, Method> createSettersMap( Object obj ){

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

            return methods;
        }


        /**
        * @returns  Getter for given property.
        */
        public static Method findGetterForProperty( Class cls, String name ){
            String capit = StringUtils.capitalize( name );
            Method getter = null;
            try {
                getter = cls.getMethod( "get" + capit );
            } catch ( NoSuchMethodException ex ){ }

            if( getter == null ) try {
                getter = cls.getMethod( "is" + capit );
            } catch ( NoSuchMethodException ex ){ }

            return getter;
        }
    }// ReflectionHelpers
    
    
    
    /**
     * Lists exceptions using their toString(), one per line.
     */
    public static String listOfExceptionsAsString( List<Exception> exs ){
        StringBuilder sb = new StringBuilder();
        for( Exception ex  : exs ) {
            sb.append( ex.toString() ).append("\n");
        }
        return sb.toString();
    }


}// class

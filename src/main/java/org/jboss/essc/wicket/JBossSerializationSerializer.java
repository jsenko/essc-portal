package org.jboss.essc.wicket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.serialize.ISerializer;
import org.jboss.serial.io.JBossObjectInputStream;
import org.jboss.serial.io.JBossObjectOutputStream;


/**
 *  Implementation of Wicket ISerializer which uses JBoss Serialization.
 */
public class JBossSerializationSerializer implements ISerializer {

    @Override
    public Object deserialize( byte[] serializedBytes ) {
        Object result = null;
        ByteArrayInputStream byteInputStream = null;
        JBossObjectInputStream jbossInputStream = null;

        try {
            byteInputStream = new ByteArrayInputStream( serializedBytes );
            jbossInputStream = new JBossObjectInputStream( byteInputStream );
            result = jbossInputStream.readObject();
        }
        catch( IOException e ) {
            throw new WicketRuntimeException( "Error deserializing object.", e );
        }
        catch( ClassNotFoundException e ) {
            throw new WicketRuntimeException( "Class not found for serialized object.", e );
        }
        finally {
            IOUtils.closeQuietly( jbossInputStream );
        }

        return result;
    }


    @Override
    public byte[] serialize( Object obj ) {
        byte[] serializedResult = null;
        JBossObjectOutputStream jbossOutputStream = null;
        ByteArrayOutputStream byteOutputStream = null;

        try {
            byteOutputStream = new ByteArrayOutputStream();
            jbossOutputStream = new JBossObjectOutputStream( byteOutputStream );
            jbossOutputStream.writeObject( obj );
            jbossOutputStream.flush();
            byteOutputStream.flush();

            serializedResult = byteOutputStream.toByteArray();
        }
        catch( IOException e ) {
            throw new WicketRuntimeException( "Error serializing object of type: " + obj.getClass().getCanonicalName(), e );
        }
        finally {
            IOUtils.closeQuietly( jbossOutputStream );
        }

        return serializedResult;
    }

}// class

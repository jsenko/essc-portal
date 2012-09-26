package org.jboss.essc.web.util;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailSender {
    
    
    /**
     * Resource for sending the email.  The mail subsystem is defined in either standalone.xml or domain.xml in your 
     * respective configuration directory. 
     */
    @Resource(mappedName="java:jboss/mail/Default")
    private Session mailSession;
    
    private String mailFrom    = "essc-list@redhat.com";
    private String mailReplyTo = "essc-list@redhat.com";
    private String mailContentType = "text/plain";
    private String mailEncoding    = "utf-8";
    


    /**
    * Sends a mail to specified address.
    * @param sAddress
    * @param sMailText
    */
    public void sendMail( String sMailTo, String sSubject, String sMailText ) throws Exception {

        try {
            MimeMessage message = new MimeMessage( mailSession );
            message.setFrom( new InternetAddress( this.mailFrom ) );
            message.setReplyTo( new Address[]{new InternetAddress( this.mailReplyTo )} );
            message.addRecipient( Message.RecipientType.TO, new InternetAddress( sMailTo ) );
            message.setHeader( "Content-Type", this.mailContentType + "; charset=\"" + this.mailEncoding + "\"");
            message.setSubject( sSubject );
            message.setText( sMailText );
            Transport.send( message );
        }
        catch( MessagingException ex ){
            throw new Exception( "Error sending mail: " + ex.getMessage(), ex );
        }

    }// sendMail()

    
}// class

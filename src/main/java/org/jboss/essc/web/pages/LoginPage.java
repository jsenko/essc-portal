package org.jboss.essc.web.pages;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.security.auth.login.LoginException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jboss.essc.web.dao.UserDaoBean;
import org.jboss.essc.web.model.User;
import org.jboss.essc.web.util.MailSender;
import org.jboss.essc.web.util.PicketBoxAuthPojo;
import org.picketbox.exceptions.PicketBoxProcessingException;
import org.picketbox.plugins.PicketBoxProcessor;


/**
 * @author Ondrej Zizka
 */
@SuppressWarnings("serial")
public class LoginPage extends BaseLayoutPage {

    @Inject private UserDaoBean userDao;
    @Inject private MailSender mailSender;
    
    
    // Components
    private Form<User> form;
    private FeedbackPanel feedback = (FeedbackPanel) new FeedbackPanel("feedback").setOutputMarkupId(true);
    private Button lostButton;

    // Data
    private User user = new User();
    

    
    public LoginPage(PageParameters params) {
        
        //String userName = params.get("user").toOptionalString();
        
        add(this.feedback);

        this.form = new Form<User>("form") {
            @Override protected void onSubmit() {
            }
        };

        // User, pass
        this.form.add(new RequiredTextField("user", new PropertyModel(this.user, "name")));
        this.form.add(new PasswordTextField("pass", new PropertyModel(this.user, "pass")));
        
        // Register button
        final AjaxButton loginBtn = new AjaxButton("login") {
            @Override
            protected void onSubmit( AjaxRequestTarget target, Form<?> form ) {
                target.add( feedback );
                //checkLoginWithPicketBox();
                try {
                    //User user_ = userDao.loadUserIfPasswordMatches( user );
                    if( !  LoginPage.this.getSession().authenticate( user ) )
                        throw new NoResultException("No such user.");
                    setResponsePage(HomePage.class);
                }
                catch( NoResultException ex ){
                    //setResponsePage(HomePage.class);
                    feedback.error("Wrong password or non-existent user: " + user.getName() + " / " + user.getPass());
                    feedback.info( "To get forgotten password, fill in user name and/or email.");
                    lostButton.setVisible( true );
                }
            }
        };
        this.form.add( loginBtn );
        
        // Mail
        this.form.add(new TextField("mail", new PropertyModel(this.user, "mail")));

        // Register button
        final AjaxButton regisButton = new AjaxButton("regis"){};
        this.form.add( regisButton );

        // Show productization releases?
        CheckBox showProd = new CheckBox("prod", new PropertyModel(this.user, "showProd"));
        this.form.add( showProd );
        
        // Lost password button
        this.lostButton = new AjaxFallbackButton("lost", form) {
            @Override protected void onSubmit( AjaxRequestTarget target, Form<?> form ) {
                target.add( feedback );
                try{
                    resetPassword(user);
                    feedback.info("Password was reset and sent by mail.");
                    info("Password was reset and sent by mail.");
                } catch (Exception ex){
                    feedback.error("Could not reset password: " + ex.getMessage() );
                    error("Could not reset password: " + ex.getMessage() );
                }
                //super.onSubmit( target, form );
            }
        };
        this.lostButton.setVisible(false).setOutputMarkupPlaceholderTag(true);
        this.form.add( this.lostButton );

        add(this.form);
    }


    /**
     *   Generates a new password and sends it to user's mail.
     */
    private void resetPassword( User user ) throws Exception {
        String pass = "pass";
        user.setPass( DigestUtils.md5Hex( pass ) );
        user = userDao.update(user);
        
        mailSender.sendMail( user.getMail(), "ESSC portal password", 
                "User name:    " + user.getName() + "\n" +
                "New password: " + user.getPass() + "\n"
        );
    }
    
    
    public User getUser() { return user; }
    public void setUser( User user ) { this.user = user; }

    
    
    
    /**
     *   Test of PicketBox login approach. Doesn't work - says 'Invalid'.
     */
    private void checkLoginWithPicketBox() {
        try {
            //ServletContext sc = (ServletContext) getRequest().getContainerRequest();

            PicketBoxProcessor processor = new PicketBoxProcessor();
            processor.setSecurityInfo("admin", "aaa");
            processor.process( new PicketBoxAuthPojo() );

            /*Principal admin = new SimplePrincipal("admin");
            processor.getCallerPrincipal().equals( admin );
            Subject callerSubject = processor.getCallerSubject(); // Null?
            callerSubject.getPrincipals().contains(admin);
            RoleGroup callerRoles = processor.getCallerRoles();
            */

            feedback.info( "Logged succesfully." );
            error("Foo");
        }
        catch( LoginException ex ) {
            feedback.error( "Login error: " + ex.getMessage() );
            error("Foo");
            //throw new RuntimeException(ex);
        }
        catch( PicketBoxProcessingException ex ) {
            Logger.getLogger( LoginPage.class.getName() ).log( Level.SEVERE, null, ex );
            feedback.error( "Login error: " + ex.getMessage() );
            error("Foo");
            //throw new RuntimeException(ex);
        }
    }
    
    
}// class

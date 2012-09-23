package org.jboss.essc.web.pages;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jboss.essc.web.dao.ProductDaoBean;
import org.jboss.essc.web.dao.ReleaseDaoBean;
import org.jboss.essc.web.dao.UserDaoBean;
import org.jboss.essc.web.model.Product;
import org.jboss.essc.web.model.User;


/**
 * @author Ondrej Zizka
 */
@SuppressWarnings("serial")
public class LoginPage extends BaseLayoutPage {

    @Inject private UserDaoBean userDao;

    
    // Components
    private Form<User> form;
    private FeedbackPanel feedback = new FeedbackPanel("feedback");
    private Button lostButton;

    // Data
    private User user;
    

    
    public LoginPage(PageParameters params) {
        
        //String userName = params.get("user").toOptionalString();
        
        add(this.feedback);

        this.form = new Form<User>("form") {
            @Override protected void onSubmit() {
                try {
                    User user_ = userDao.loadUserIfPasswordMatches( user );
                    setResponsePage(HomePage.class);
                }
                catch( NoResultException ex ){
                    //setResponsePage(HomePage.class);
                    feedback.error("Wrong password or non-existent user.");
                    feedback.info( "To get forgotten password, fill in user name and/or email.");
                    lostButton.setVisible( true );
                }
            }
        };

        // User, pass
        this.form.add(new RequiredTextField("user", new PropertyModel(this.user, "user")));
        this.form.add(new PasswordTextField("pass", new PropertyModel(this.user, "pass")));
        
        // Register button
        final AjaxButton loginBtn = new AjaxButton("login") {};
        this.form.add( loginBtn );
        
        // Mail
        this.form.add(new TextField("mail", new PropertyModel(this.user, "mail")));

        // Register button
        final AjaxButton regisButton = new AjaxButton("regis"){};
        this.form.add( loginBtn );

        // Show productization releases?
        CheckBox showProd = new CheckBox("prod", new PropertyModel(this.user, "showProd"));
        this.form.add( showProd );
        
        // Lost password button
        this.lostButton = new AjaxFallbackButton("lost", form) {
            @Override protected void onSubmit( AjaxRequestTarget target, Form<?> form ) {
                userDao.sendLostPassword(user);
                //super.onSubmit( target, form );
            }
        };
        this.lostButton.setVisible(false).setOutputMarkupPlaceholderTag(true);
        this.form.add( this.lostButton );

        add(this.form);
    }

    
    
    public User getUser() { return user; }
    public void setUser( User user ) { this.user = user; }
    
}// class

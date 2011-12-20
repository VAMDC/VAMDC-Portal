package org.vamdc.portal.session.security;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.RunAsOperation;
import org.jboss.seam.security.management.IdentityManagementException;
import org.jboss.seam.security.management.IdentityManager;
import org.jboss.seam.security.management.JpaIdentityStore;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.vamdc.portal.entity.security.User;

@Name("register")
@Scope(ScopeType.EVENT)
public class Register
{
	@Logger private Log log;

	@In private StatusMessages statusMessages;
	@In private IdentityManager identityManager;


	private String username;
	private String password;
	private String verifyPassword;
	private String email;
	private boolean registered=false;
	
	public void register()
	{
		registered=false;
		try {
			new RunAsOperation() {
				public void execute() {
					if (!identityManager.userExists(username)){
						identityManager.createUser(username,password);
					}else{
						throw new IdentityManagementException("User #{register.username} already exists!");
					}
				}
			}.addRole("admin").run();
			statusMessages.add(Severity.INFO,"User #0 registered successfully. You may login now.", username);
			log.info("Registered new user ",username);
			registered=true;
		} catch (IdentityManagementException e) {
			statusMessages.add(Severity.ERROR,e.getMessage());
		}

	}


	@Observer(JpaIdentityStore.EVENT_PRE_PERSIST_USER)
	public void onPrePersist(User user) {
		user.setEmail(email);
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getVerifyPassword() {
		return verifyPassword;
	}


	public void setVerifyPassword(String verifyPassword) {
		this.verifyPassword = verifyPassword;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public boolean isRegistered() {
		return registered;
	}

}

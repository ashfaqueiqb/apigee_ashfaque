package com.tp.ad.authentication;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import com.apigee.flow.execution.ExecutionContext;
import com.apigee.flow.execution.ExecutionResult;
import com.apigee.flow.execution.spi.Execution;
import com.apigee.flow.message.MessageContext;
import com.tp.ad.authentication.util.Utility;
import com.tp.ad.model.User;

/**
 * 
 * @author SGSRIVASTAVM
 *
 */
public class ExternalActiveDirectory implements Execution {

	private Hashtable<String, String> properties = new Hashtable<>();

	@Override
	public ExecutionResult execute(MessageContext arg0, ExecutionContext arg1) {
		try {
			if (isUserAuthenticated((String) arg0.getVariable("adHost"), (String) arg0.getVariable("adPort"),
					(String) arg0.getVariable("password"), (String) arg0.getVariable("ou"), arg0,
					(String) arg0.getVariable("userId"))) {
				arg0.setVariable("authenticated", "true");
			} else {
				arg0.setVariable("authenticated", "false");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ExecutionResult.SUCCESS;
	}

	/**
	 * 
	 * @param adHost
	 * @param adPort
	 * @param password
	 * @param ou
	 * @param messageContext
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private boolean isUserAuthenticated(String adHost, String adPort, String password, String ou,
			MessageContext messageContext, String userId) throws Exception {
		boolean result = false;
		User user = Utility.getUser(userId, password, (String) messageContext.getVariable("kvm.private_key"));
		String url = "ldap://" + adHost + ":" + adPort;
		properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		properties.put(Context.PROVIDER_URL, url);
		properties.put(Context.SECURITY_AUTHENTICATION, "simple");
		properties.put(Context.SECURITY_PROTOCOL, "ssl");
		properties.put(Context.SECURITY_PRINCIPAL, "CN=" + user.getUserName() + "," + ou);
		properties.put(Context.SECURITY_CREDENTIALS, user.getPassword());
		try {
			DirContext ctx = new InitialDirContext(properties);
			ctx.close();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}

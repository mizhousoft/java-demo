package com.mizhousoft.sshd;

import org.apache.sshd.common.util.logging.AbstractLoggingBean;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.session.ServerSession;

/**
 * A test {@link PasswordAuthenticator} that accepts an authentication attempt if the username is
 * not {@code null} and
 * same as password
 *
 * @author <a href="mailto:dev@mina.apache.org">Apache MINA SSHD Project</a>
 */
public class BogusPasswordAuthenticator extends AbstractLoggingBean implements PasswordAuthenticator
{
	public static final BogusPasswordAuthenticator INSTANCE = new BogusPasswordAuthenticator();

	public BogusPasswordAuthenticator()
	{
		super();
	}

	@Override
	public boolean authenticate(String username, String password, ServerSession session)
	{
		boolean result = (username != null) && username.equals(password);
		if (log.isDebugEnabled())
		{
			log.debug("authenticate({}) {} / {} - success={}", session, username, password, result);
		}

		return result;
	}
}

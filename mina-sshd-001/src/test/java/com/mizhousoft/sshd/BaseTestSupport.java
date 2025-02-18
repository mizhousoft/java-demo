package com.mizhousoft.sshd;

import java.io.IOException;
import java.time.Duration;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.io.DefaultIoServiceFactoryFactory;
import org.apache.sshd.common.io.IoServiceFactoryFactory;
import org.apache.sshd.common.util.net.SshdSocketAddress;
import org.apache.sshd.server.SshServer;

/**
 * Helper used as base class for all test classes
 *
 * @author <a href="mailto:dev@mina.apache.org">Apache MINA SSHD Project</a>
 */
public abstract class BaseTestSupport
{
	// can be used to override the 'localhost' with an address other than 127.0.0.1 in case it is
	// required
	public static final String TEST_LOCALHOST = System.getProperty("org.apache.sshd.test.localhost", SshdSocketAddress.LOCALHOST_IPV4);

	public static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);

	public static final Duration AUTH_TIMEOUT = Duration.ofSeconds(8);

	public static final Duration OPEN_TIMEOUT = Duration.ofSeconds(9);

	public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);

	public static final Duration CLOSE_TIMEOUT = Duration.ofSeconds(15);

	protected BaseTestSupport()
	{
		super();
	}

	protected SshServer setupTestServer()
	{
		return CoreTestSupportUtils.setupTestServer(getClass());
	}

	protected SshServer setupTestFullSupportServer()
	{
		return CoreTestSupportUtils.setupTestFullSupportServer(setupTestServer());
	}

	protected SshClient setupTestClient()
	{
		return CoreTestSupportUtils.setupTestClient(getClass());
	}

	protected SshClient setupTestFullSupportClient()
	{
		return CoreTestSupportUtils.setupTestFullSupportClient(setupTestClient());
	}

	public static ClientSession createClientSession(String username, SshClient client, int port) throws IOException
	{
		return client.connect(username, TEST_LOCALHOST, port).verify(CONNECT_TIMEOUT).getSession();
	}

	public static ClientSession createAuthenticatedClientSession(String username, SshClient client, int port) throws IOException
	{
		ClientSession session = createClientSession(username, client, port);
		try
		{
			ClientSession authSession = createAuthenticatedClientSession(session, username);
			session = null;     // avoid auto-close at finally clause
			return authSession;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	public static ClientSession createAuthenticatedClientSession(ClientSession session, String username) throws IOException
	{
		session.addPasswordIdentity(username);
		session.auth().verify(AUTH_TIMEOUT);
		return session;
	}

	public static IoServiceFactoryFactory getIoServiceProvider()
	{
		DefaultIoServiceFactoryFactory factory = DefaultIoServiceFactoryFactory.getDefaultIoServiceFactoryFactoryInstance();
		return factory.getIoServiceProvider();
	}
}

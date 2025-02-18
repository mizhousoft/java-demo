package com.mizhousoft.sshd;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.simple.SimpleClient;
import org.apache.sshd.server.SshServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author <a href="mailto:dev@mina.apache.org">Apache MINA SSHD Project</a>
 */
public abstract class BaseSimpleClientTestSupport extends BaseTestSupport
{

	protected SshServer sshd;

	protected SshClient client;

	protected int port;

	protected SimpleClient simple;

	protected BaseSimpleClientTestSupport()
	{
		super();
	}

	@BeforeEach
	public void setUp() throws Exception
	{
		client = setupTestClient();

		simple = SshClient.wrapAsSimpleClient(client);
		simple.setConnectTimeout(CONNECT_TIMEOUT.toMillis());
		simple.setAuthenticationTimeout(AUTH_TIMEOUT.toMillis());
	}

	@AfterEach
	public void tearDown() throws Exception
	{
		if (sshd != null)
		{
			sshd.stop(true);
		}
		if (simple != null)
		{
			simple.close();
		}
		if (client != null)
		{
			client.stop();
		}
	}
}

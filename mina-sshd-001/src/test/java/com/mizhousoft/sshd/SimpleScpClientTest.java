package com.mizhousoft.sshd;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.EnumSet;
import java.util.Properties;

import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.scp.client.CloseableScpClient;
import org.apache.sshd.scp.client.ScpClient;
import org.apache.sshd.scp.client.ScpClientCreator;
import org.apache.sshd.scp.client.SimpleScpClient;
import org.apache.sshd.scp.client.SimpleScpClientImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.mizhousoft.commons.lang.CharEncoding;

@TestMethodOrder(MethodName.class)
public class SimpleScpClientTest extends BaseSimpleClientTestSupport
{
	private SimpleScpClient scpClient;

	private Properties prop;

	@BeforeEach
	@Override
	public void setUp() throws Exception
	{
		Properties prop = new Properties();
		prop.load(SimpleScpClientTest.class.getClassLoader().getResourceAsStream("application.properties"));
		this.prop = prop;

		super.setUp();

		client.start();
		scpClient = new SimpleScpClientImpl(simple);
	}

	@Test
	void sessionClosedWhenClientClosed() throws Exception
	{
		try (CloseableScpClient scp = login())
		{
			assertTrue(scp.isOpen(), "SCP not open");

			Session session = scp.getClientSession();
			assertTrue(session.isOpen(), "Session not open");

			scp.close();
			assertFalse(session.isOpen(), "Session not closed");
			assertFalse(scp.isOpen(), "SCP not closed");
		}
	}

	@Test
	void scpUploadProxy() throws Exception
	{
		try (CloseableScpClient scp = login())
		{
			Path localFile = Path.of(SimpleScpClientTest.class.getClassLoader().getResource("scp-text.txt").toURI());
			String remotePath = "/tmp/scp-text.txt";

			scp.upload(localFile, remotePath);
		}
	}

	@Test
	void scpDownloadProxy() throws Exception
	{
		try (CloseableScpClient scp = login())
		{
			Path localFile = Path.of("./text.txt");
			String remotePath = "/tmp/scp-text.txt";

			scp.download(remotePath, localFile);
		}
	}

	@Test
	void test() throws Exception
	{
		String host = prop.getProperty("host");
		int port = Integer.valueOf(prop.getProperty("port"));
		String username = prop.getProperty("username");
		String password = prop.getProperty("password");

		try (ClientSession session = client.connect(username, host, port).verify(CONNECT_TIMEOUT).getSession())
		{
			session.addPasswordIdentity(password);
			session.auth().verify(AUTH_TIMEOUT);

			ScpClient scpClient = ScpClientCreator.instance().createScpClient(session);

			Path localFile = Path.of("./text.txt");
			String remotePath = "/tmp/scp-text.txt";

			String value = "喀什的房间里";
			scpClient.upload(value.getBytes(CharEncoding.UTF8), remotePath, EnumSet.allOf(PosixFilePermission.class), null);

			scpClient.download(remotePath, localFile);

			byte[] downloaded = scpClient.downloadBytes(remotePath);
			String downloadString = new String(downloaded, CharEncoding.UTF8);

			System.out.println(downloadString);
		}
		finally
		{
			client.stop();
		}
	}

	private CloseableScpClient login() throws IOException
	{
		String host = prop.getProperty("host");
		int port = Integer.valueOf(prop.getProperty("port"));
		String username = prop.getProperty("username");
		String password = prop.getProperty("password");

		return scpClient.scpLogin(host, port, username, password);
	}
}

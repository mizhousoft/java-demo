package com.mizhousoft.sshd;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.util.io.output.LineLevelAppender;
import org.apache.sshd.common.util.io.output.LineLevelAppenderStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mizhousoft.commons.lang.CharEncoding;
import com.mizhousoft.commons.lang.LocalDateTimeUtils;

/**
 * TODO
 *
 * @version
 */
public class ClientTest extends BaseSimpleClientTestSupport
{
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
	}

	@Test
	public void clentTest() throws Exception
	{
		String host = prop.getProperty("host");
		int port = Integer.valueOf(prop.getProperty("port"));
		String username = prop.getProperty("username");
		String password = prop.getProperty("password");

		String cmd = "bash -x /opt/syhy/deploy/deploy.sh install /tmp/game/Game_8_1.properties";

		ClientSession session = client.connect(username, host, port).verify(CONNECT_TIMEOUT).getClientSession();
		session.addPasswordIdentity(password);

		if (!session.auth().verify(AUTH_TIMEOUT).isSuccess())
		{
			System.out.println("auth failed");
		}

		File outputfile = new File("output.txt");

		String lineEnding = System.lineSeparator();
		final byte[] eolBytes = lineEnding.getBytes(CharEncoding.UTF8);

		LineLevelAppender appender = new LineLevelAppender()
		{
			@Override
			public boolean isWriteEnabled()
			{
				return true;
			}

			@Override
			public void writeLineData(CharSequence lineData) throws IOException
			{
				String now = LocalDateTimeUtils.formatYmdhms(LocalDateTime.now());
				String data = now + " " + lineData;

				FileUtils.write(outputfile, data, CharEncoding.UTF8, true);
				FileUtils.writeByteArrayToFile(outputfile, eolBytes, true);
			}

			@Override
			public void close() throws IOException
			{
				// ignored
			}
		};
		LineLevelAppenderStream stream = new LineLevelAppenderStream("utf-8", appender);

		try (ClientChannel channel = session.createExecChannel(cmd))
		{
			channel.setOut(stream);
			channel.setRedirectErrorStream(true);
			channel.open().verify(OPEN_TIMEOUT);

			channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), CLOSE_TIMEOUT);

			Integer exitStatus = channel.getExitStatus();

			channel.close();

			System.out.println(exitStatus);
		}

		client.stop();
	}
}

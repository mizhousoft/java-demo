package com.mizhousoft.sshd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.EnumSet;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;

/**
 * TODO
 *
 * @version
 */
public class Test
{

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		SshClient client = SshClient.setUpDefaultClient();
		client.start();

		// using the client for multiple sessions...
		try (ClientSession session = client.connect("gameuser", "", 22).verify(3 * 1000).getSession())
		{
			session.addPasswordIdentity(""); // for password-based

			session.auth().verify(3 * 1000);
			// start using the session to run commands, do SCP/SFTP, create local/remote port
			// forwarding, etc...

			try (OutputStream stdout = new FileOutputStream(new File("./out.txt"));
			        OutputStream stderr = new FileOutputStream(new File("./err.txt"));
			        ClientChannel channel = session.createExecChannel("ls /"))
			{
				channel.setOut(stdout);
				channel.setErr(stderr);
				channel.open().verify(3 * 1000);
				// Wait (forever) for the channel to close - signalling command finished
				channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), 0L);
			}
		}
	}

}

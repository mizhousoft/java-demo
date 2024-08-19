package com.mizhousoft.sql;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main
 *
 * @version
 */
public class Main
{
	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args)
	{
		String dbUrl = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&autoReconnect=true&failOverReadOnly=false&useSSL=false&allowPublicKeyRetrieval=true";
		String user = "root";
		String password = "mysql123";

		String filePath = "C:\\work\\fota.sql";

		importSQLFile(dbUrl, user, password, filePath);
	}

	public static void importSQLFile(String dbUrl, String user, String password, String filePath)
	{
		try (Connection conn = DriverManager.getConnection(dbUrl, user, password);
		        Reader reader = Files.newBufferedReader(Paths.get(filePath)))
		{
			ScriptRunner sr = new ScriptRunner(conn);
			sr.runScript(reader);

			LOG.info("SQL file imported successfully.");
		}
		catch (Throwable e)
		{
			LOG.error("Import sql file failed", e);
		}
	}
}

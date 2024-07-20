package com.mizhousoft.flowable.boot;

import javax.sql.DataSource;

import org.flowable.common.engine.impl.AbstractEngineConfiguration;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ProcessEngineConfig
 *
 * @version
 */
@Configuration
public class ProcessEngineConfig
{
	@Bean
	public ProcessEngine getProcessEngine(DataSource dataSource)
	{
		ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration();
		cfg.setDataSource(dataSource);
		cfg.setDatabaseSchemaUpdate(AbstractEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

		return cfg.buildProcessEngine();
	}
}

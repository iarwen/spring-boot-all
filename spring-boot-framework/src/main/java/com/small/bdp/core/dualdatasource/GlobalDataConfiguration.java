package com.small.bdp.core.dualdatasource;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

//@Configuration
public class GlobalDataConfiguration {
	// @Bean(name = "dataSource_primary")
	// @Primary
	// @ConfigurationProperties(prefix = "datasource.primary")
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().build();
	}

	// @Bean(name = "sessionFactory_primary")
	// @Primary
	public LocalSessionFactoryBean primarySessionFactory(/*
														 * @Qualifier(
														 * "dataSource_primary")
														 */DataSource ds) {
		LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
		bean.setDataSource(ds);
		bean.getHibernateProperties().put("hibernate.dialect", "com.small.bdp.core.db.hibernate.SQLiteDialect");
		bean.getHibernateProperties().put("hibernate.connection.autocommit", false);
		bean.getHibernateProperties().put("hibernate.autoReconnect", false);
		bean.getHibernateProperties().put("hibernate.format_sql", false);
		bean.getHibernateProperties().put("hibernate.show_sql", false);
		bean.getHibernateProperties().put("hibernate.hbm2ddl.auto", false);
		bean.setPackagesToScan("com.small");
		return bean;
	}

	// @Bean(name = "jdbcTemplate_primary")
	// @Primary
	public JdbcTemplate primaryJdbcTemplate(/* @Qualifier("dataSource_primary") */DataSource ds) {
		JdbcTemplate bean = new JdbcTemplate();
		bean.setDataSource(ds);
		return bean;
	}

	// @Bean(name = "hibernateTemplate_primary")
	// @Primary
	public HibernateTemplate primaryHibernateTemplate(/*
													 * @Qualifier(
													 * "sessionFactory_primary")
													 */SessionFactory sf) {
		HibernateTemplate bean = new HibernateTemplate();
		bean.setSessionFactory(sf);
		return bean;
	}

	// @Bean(name = "tm_primary")
	// @Primary
	public DataSourceTransactionManager primaryDataSourceTransactionManager(/*
																			 * @
																			 * Qualifier
																			 * (
																			 * "dataSource_primary"
																			 * )
																			 */DataSource ds) {
		DataSourceTransactionManager bean = new DataSourceTransactionManager();
		bean.setDataSource(ds);
		return bean;
	}

	// /////////////////////////////////////////////////
	// @Bean(name = "hibernateTemplate_secondaryDataSource")
	// @ConfigurationProperties(prefix = "datasource.secondary")
	public DataSource secondaryDataSource() {
		return DataSourceBuilder.create().build();
	}
}
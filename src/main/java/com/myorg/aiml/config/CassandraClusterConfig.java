package com.myorg.aiml.config;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.config.SessionBuilderConfigurer;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.CassandraCustomConversions;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;

/**
 * Cassandra Cluster Configuration with Spring data Cassandra Repository
 */

@EnableCassandraRepositories
@Configuration
public class CassandraClusterConfig {
	private static final Logger logger = LoggerFactory.getLogger(CassandraClusterConfig.class);
	@Value("${CASSANDRA_HOSTS}")
	private String contactPoints;
	@Value("${CASSANDRA_PORT}")
	private int port;
	@Value("${CASSANDRA_KEYSPACE}")
	private String keyspaceName;
	@Value("${CASSANDRA_USERNAME}")
	private String userName;
	@Value("${CASSANDRA_PASSWORD}")
	private String password;
	@Value("${CASSANDRA_LOCAL_DC}")
	private String localDataCenter;
	
	@Value("${session_bean_consistency_level}")
	private String sessionBeanConsistency;
	
	@Value("${cassandra_query_request_timeout}")
	private String cassandraQueryRequestTimeout;

	public String getKeyspaceName() {
		return this.keyspaceName;
	}

	protected String getContactPoints() {
		return this.contactPoints;
	}

	@Bean("session")
	@Primary
    public CqlSessionFactoryBean session() {
    	CqlSessionFactoryBean session = new CqlSessionFactoryBean();
		List<InetSocketAddress> addressList = getInetSoketAddress(contactPoints);
		DriverConfigLoader driverConfigLoader = DriverConfigLoader.programmaticBuilder()
				.withString(DefaultDriverOption.REQUEST_CONSISTENCY, sessionBeanConsistency.trim())
				.withString(DefaultDriverOption.REQUEST_TIMEOUT, cassandraQueryRequestTimeout)
				.build();
		logger.info("Session Bean consistency level set to: " + sessionBeanConsistency);
		logger.info("cassandraQueryRequestTimeout set to:" + cassandraQueryRequestTimeout);
		SessionBuilderConfigurer sessionBuilderConfigurer = getSessionBuilderConfigurer(addressList, driverConfigLoader,
				localDataCenter);
		session.setSessionBuilderConfigurer(sessionBuilderConfigurer);
		session.setKeyspaceName(getKeyspaceName());
        return session;
    }

    @Bean("cassandraTemplate")
	@Primary
	public CassandraTemplate cassandraTemplate(CqlSessionFactoryBean session, CassandraCustomConversions ccv) {
		CassandraTemplate ct = new CassandraTemplate(session.getObject());
		return ct;
	}

	@Bean
	public CassandraCustomConversions cassandraCustomConversions(DateTimeToInstantConverter dateTimeToInstantConverter,
																 InstantToDateTimeConverter instantToDateTimeConverter) {
		return new CassandraCustomConversions(Arrays.asList(dateTimeToInstantConverter, instantToDateTimeConverter));
	}
	@Component
	@WritingConverter
	public static class DateTimeToInstantConverter implements Converter<DateTime, java.time.Instant> {
		@Override
		public java.time.Instant convert(DateTime source) {
			return source.toDate().toInstant();
		}
	}

	@Component
	@ReadingConverter
	public static class InstantToDateTimeConverter implements Converter<java.time.Instant, DateTime> {
		@Override
		public DateTime convert(java.time.Instant source) {
			return new DateTime(Date.from(source));
		}
	}

	private SessionBuilderConfigurer getSessionBuilderConfigurer(List<InetSocketAddress> addressList, DriverConfigLoader driverConfigLoader, String dataCenter) {
		SessionBuilderConfigurerModel configurerModel = new SessionBuilderConfigurerModel();
		configurerModel.setAddressList(addressList);
		configurerModel.setDataCenter(dataCenter);
		configurerModel.setDriverConfigLoader(driverConfigLoader);
		configurerModel.setKeyspaceName(keyspaceName);
		configurerModel.setPassword(password);
		configurerModel.setUserName(userName);
		return getSessionBuilderConfigurer(configurerModel);
	}

	private SessionBuilderConfigurer getSessionBuilderConfigurer(SessionBuilderConfigurerModel configurerModel) {
		return new SessionBuilderConfigurer() {
			@Override
			public CqlSessionBuilder configure(CqlSessionBuilder sessionBuilder) {
				DriverConfigLoader driverConfigLoader = configurerModel.getDriverConfigLoader();
				sessionBuilder.addContactPoints(configurerModel.getAddressList())
						.withConfigLoader(driverConfigLoader)
						.withLocalDatacenter(configurerModel.getDataCenter())
						.withKeyspace(configurerModel.getKeyspaceName())
						.withAuthCredentials(configurerModel.getUserName(), configurerModel.getPassword());
				return sessionBuilder;
			}
		};
	}
	private List<InetSocketAddress> getInetSoketAddress(String contactPoints) {
		List<InetSocketAddress> addressList = new ArrayList<>();
		StringUtils.commaDelimitedListToSet(contactPoints)
				.forEach(host -> addressList.add(new InetSocketAddress(host, port)));
		return addressList;
	}
}
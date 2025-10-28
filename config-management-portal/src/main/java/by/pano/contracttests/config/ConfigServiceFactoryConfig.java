package by.pano.contracttests.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
@Getter
class ConfigServiceFactoryConfig {

	@Value("${configservice.management.client.connecttimeout:3500}")
	private int connectTimeoutMillis;
	@Value("${configservice.management.client.readtimeout:60000}")
	private int readTimeoutMillis;
	@Value("${configservice.management.client.writetimeout:60000}")
	private int writeTimeoutMillis;
	@Value("${configservice.management.client.responseBodyLimitBytes:16777216}")
	private int responseBodyLimitBytes;
	@Value("${configservice.management.client.configservice.url}")
	private String configServiceUrl;
}

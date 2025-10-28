package by.pano.contracttests.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
@Getter
class ConfigServiceFactoryConfig {

	@Value("${configservice.public.client.connecttimeout:3500}")
	private int connectTimeoutMillis;
	@Value("${configservice.public.client.readtimeout:60000}")
	private int readTimeoutMillis;
	@Value("${configservice.public.client.writetimeout:60000}")
	private int writeTimeoutMillis;
	@Value("${configservice.public.client.responseBodyLimitBytes:16777216}")
	private int responseBodyLimitBytes;
	@Value("${configservice.public.client.configservice.url}")
	private String configServiceUrl;
}

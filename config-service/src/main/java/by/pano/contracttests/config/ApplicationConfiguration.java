package by.pano.contracttests.config;

import java.time.Clock;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.Assert;


@Configuration
@EnableTransactionManagement
@PropertySource(value = "file://${user.dir}/.env", ignoreResourceNotFound = true)
@RequiredArgsConstructor
public class ApplicationConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfiguration.class);
	private final ObjectMapper objectMapper;

	@PostConstruct
	public void initMapper() {
		Assert.notNull(objectMapper, "Object mapper should be configured automatically by Spring.");
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // default setting in spring implementation
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true); // pretty printing
		objectMapper.registerModule(new JavaTimeModule());
		// configure objectMapper here
	}

	@PreDestroy
	public void onDestroy() {
		LOGGER.info("Shutting down service...");
	}

	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}
}

package by.pano.contracttests;

import java.util.List;
import java.util.stream.Collectors;

import com.github.dockerjava.api.model.LogConfig;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;


public class PostgresExtension implements AfterEachCallback, BeforeAllCallback {

	private static final int POSTGRES_CONTAINER_EXPOSED_PORT = 5432;
	private static boolean started;
	private static final String POSTGRES_USER = "postgres";
	private static final String POSTGRES_PASSWORD = "postgres";
	private static final PostgreSQLContainer container = new PostgreSQLContainer<>("postgres:13.6")
			.withDatabaseName("configservice-test")
			.withUsername(POSTGRES_USER)
			.withPassword(POSTGRES_PASSWORD)
			.withEnv("TZ", "Europe/Warsaw")
			.withCreateContainerCmdModifier(cmd -> {
				cmd.getHostConfig().withLogConfig(new LogConfig().setType(LogConfig.LoggingType.JSON_FILE));
				cmd.withName("configservice-test-postgres-" + System.currentTimeMillis());
			}).waitingFor(Wait.forListeningPort())
			.withReuse(true);

	@Override
	public void beforeAll(ExtensionContext context) {
		Class<?> testClass = context.getRequiredTestClass();
		if (!started) {
			container.start();
			started = true;

		}
		System.setProperty("services.postgres.enabled", "true");
		System.setProperty("spring.datasource.url", "jdbc:postgresql://%s:%d/configservice-test?ApplicationName=%s".formatted(getHost(), getPort(), testClass.getSimpleName()));
		System.setProperty("spring.datasource.username", POSTGRES_USER);
		System.setProperty("spring.datasource.password", POSTGRES_PASSWORD);
		System.setProperty("spring.flyway.enabled", "true");
		System.setProperty("spring.flyway.url", "jdbc:postgresql://%s:%d/configservice-test".formatted(getHost(), getPort()));
		System.setProperty("spring.flyway.user", POSTGRES_USER);
		System.setProperty("spring.flyway.password", POSTGRES_PASSWORD);
	}

	private List<String> tables;

	private List<String> listTables(JdbcTemplate jdbc) {
		if (tables == null) {
			tables = jdbc.queryForList("select * from information_schema.tables where table_schema = 'public'")
						 .stream().map(record -> (String)record.get("TABLE_NAME"))
						 .filter(tablename -> !tablename.contains("schema_version"))
						 .collect(Collectors.toList());
		}
		System.out.println(tables.stream().collect(Collectors.joining(", ")));
		return tables;
	}

	@Override
	public void afterEach(ExtensionContext context) {
		ApplicationContext ctx = SpringExtension.getApplicationContext(context);
		JdbcTemplate jdbc = ctx.getBean(JdbcTemplate.class);
		String sql = listTables(jdbc).stream()
									 .map(tableName -> "TRUNCATE TABLE " + tableName + " CASCADE;")
									 .collect(Collectors.joining(System.lineSeparator()));
		jdbc.execute(sql);
	}

	private static String getHost() {
		return container.getHost();
	}

	private static Integer getPort() {
		return container.getMappedPort(POSTGRES_CONTAINER_EXPOSED_PORT);
	}
}

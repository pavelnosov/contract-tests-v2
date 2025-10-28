package by.pano.contracttests.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;


@EnableWebMvc
@Configuration
class WebConfig implements WebMvcConfigurer {

	private final ApplicationContext applicationContext;
	private final boolean cacheActivated;

	public WebConfig(
			ApplicationContext applicationContext,
			@Value("${info.app.enablecache}") boolean cacheActivated
	) {
		this.applicationContext = applicationContext;
		this.cacheActivated = cacheActivated;
	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/img/**").addResourceLocations("classpath:_generated/img/");
		registry.addResourceHandler("/css/**").addResourceLocations("classpath:_generated/css/");
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:_generated/js/");
		registry.addResourceHandler("/webfonts/**").addResourceLocations("classpath:_generated/webfonts/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/config");
	}

	@Bean
	public SpringResourceTemplateResolver templateResolver() {

		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(applicationContext);
		templateResolver.setPrefix("classpath:/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setOrder(1);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCacheable(cacheActivated);
		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setEnableSpringELCompiler(true);
		return templateEngine;
	}

	@Bean
	public ThymeleafViewResolver viewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setCharacterEncoding("UTF-8");
		viewResolver.setOrder(1);
		viewResolver.setViewNames(new String[] {"*.html", "*.xhtml"});
		return viewResolver;
	}

}

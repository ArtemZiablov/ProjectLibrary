package ua.karazin.interfaces.ProjectLibrary.configs;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// hello
@EnableConfigurationProperties({BookProperties.class, StatisticsProperties.class})
@Configuration
public class AppConfig {
}

package com.almis.awe.autoconfigure;

import com.almis.awe.component.AweRoutingDataSource;
import lombok.extern.log4j.Log4j2;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Flyway configuration
 *
 * @author pvidal
 * Created by pvidal on 04/12/2019.
 */
@Configuration
@Log4j2
@ConditionalOnProperty(name = "spring.flyway.enabled", havingValue = "true")
public class FlywayMigrationConfig {

  private DataSource dataSource;

  @Autowired
  private Flyway flyway;

  @Value("${awe.database.migration.prefix:V}")
  private String prefixPattern;

  @Value("${awe.database.migration.repeatable.prefix:R}")
  private String repeatablePrefixPattern;

  @Value("${awe.database.migration.modules}")
  private String[] modulesToMigrate;

  /**
   * Constructor
   *
   * @param dataSource Awe routing datasource
   */
  @Autowired
  public FlywayMigrationConfig(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /**
   * Flyway migration strategy
   *
   * @return FlywayMigrationStrategy
   */
  @Bean
  public FlywayMigrationStrategy flywayMigrationStrategy() {
    return flywayInstance -> {
      // Do nothing
    };
  }

  @PostConstruct
  public void initFlyway() {

    try {
      Integer indexBaseLine = 0;
      log.info("=======  Migrating default database  =======");
      for (String module : modulesToMigrate) {
        Flyway customFlyway = customizeFlywayConfig(module, dataSource, indexBaseLine);

        // Migrate first connection
        log.info("=======  Migrating database of {} module  =======", module);
        customFlyway.migrate();
        log.info("======= Current version of {} module: {}", module, customFlyway.info().current().getVersion());
      }

      if (dataSource instanceof AweRoutingDataSource) {
        // Spread scripts migration
        log.info("========== Migrating databases of [AweDbs] table defined in default database ... ==========");
        for (String module : modulesToMigrate) {
          ((AweRoutingDataSource) dataSource).getContextHolder().getDataSources().forEach((key, value) -> {
                    log.info("========== Migrating database {} for module {} ... ==========", key, module);
                    Flyway customFlyway = customizeFlywayConfig(module, (DataSource) value, indexBaseLine);
                    customFlyway.migrate();
                    log.info("======= Current version of module {} from database {}: {}", module, key, customFlyway.info().current().getVersion());
                  }
          );
        }
      }
    } catch (FlywayException ex) {
      log.error("There was a problem initializing Flyway", ex);
    }
  }


  /**
   * Customize configuration
   *
   * @param module Name of module to apply migration scripts
   */
  private Flyway customizeFlywayConfig(String module, DataSource dataSource, Integer indexBaseLine) {

    String scriptPrefix = !"".equalsIgnoreCase(prefixPattern) ? String.format(prefixPattern, module) : prefixPattern;
    String repeatableScriptPrefix = !"".equalsIgnoreCase(repeatablePrefixPattern) ? String.format(repeatablePrefixPattern, module) : repeatablePrefixPattern;
    FluentConfiguration configuration = new FluentConfiguration()
            .baselineOnMigrate(true)
            .baselineVersion(indexBaseLine.toString())
            .sqlMigrationPrefix(scriptPrefix)
            .repeatableSqlMigrationPrefix(repeatableScriptPrefix)
            .table("flyway_schema_" + module)
            .locations(flyway.getConfiguration().getLocations())
            .dataSource(dataSource);
    return Flyway.configure().configuration(configuration).load();
  }
}

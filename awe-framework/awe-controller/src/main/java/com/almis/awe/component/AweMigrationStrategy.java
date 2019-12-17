package com.almis.awe.component;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;

public class AweMigrationStrategy implements FlywayMigrationStrategy {

  private Flyway flyway;

  @Autowired
  public AweMigrationStrategy(Flyway flyway) {
    this.flyway = flyway;
  }


  @Override
  public void migrate(Flyway flyway) {
    flyway.migrate();
  }
}

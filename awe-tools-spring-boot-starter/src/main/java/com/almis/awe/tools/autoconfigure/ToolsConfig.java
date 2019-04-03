package com.almis.awe.tools.autoconfigure;

import com.almis.awe.service.data.builder.DataListBuilder;
import com.almis.awe.tools.service.FileManagerService;
import com.almis.awe.tools.service.SqlExtractorService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Tool module configuration
 */
@Configuration
public class ToolsConfig {

  /**
   * File manager service
   * @return File manager service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public FileManagerService fileManagerService() {
    return new FileManagerService();
  }

  /**
   * SQL Extractor service
   * @param dataListBuilder Datalist builder
   * @param dataSource Datasource
   * @return SQL Extractor service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public SqlExtractorService sqlExtractorService(DataListBuilder dataListBuilder, DataSource dataSource) {
    return new SqlExtractorService(dataListBuilder, dataSource);
  }
}

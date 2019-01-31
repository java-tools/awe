package com.almis.awe.test.unit;

import org.springframework.test.context.TestPropertySource;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 *
 */
@TestPropertySource("classpath:hsql.properties")
public class QueryHSQLTest extends QueryTest {
}
package com.almis.awe.annotation.util;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Custom Spring Expression Language Implementation
 */
public class SpringExpressionLanguageParser {
  private ExpressionParser parser;
  private StandardEvaluationContext context;

  /**
   * Constructor
   * @param parameterNames Parameter names
   * @param args Arguments
   */
  public SpringExpressionLanguageParser(String[] parameterNames, Object[] args) {
    parser = new SpelExpressionParser();
    context = new StandardEvaluationContext();
    for (int i = 0; i < parameterNames.length; i++) {
      context.setVariable(parameterNames[i], args[i]);
    }
  }

  /**
   * Retrieve dynamic value
   * @param key Key
   * @param clazz Class
   * @param <T> Class type
   * @return Dynamic value
   */
  public <T> T getDynamicValue(String key, Class<T> clazz) {
    return this.parser.parseExpression(key).getValue(context, clazz);
  }
}

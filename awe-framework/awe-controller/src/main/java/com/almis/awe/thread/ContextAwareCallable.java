package com.almis.awe.thread;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;

/**
 * Created by pgarcia on 07/04/2017.
 * @param <T> type of ContextAwareCallable
 */
public class ContextAwareCallable<T> implements Callable<T> {
  private Callable<T> task;
  private RequestAttributes context;

  /**
   * Constructor
   *
   * @param task task
   * @param context context
   */
  public ContextAwareCallable(Callable<T> task, RequestAttributes context) {
    this.task = task;
    this.context = context;
  }

  /**
   * Call function
   *
   * @return launch ContextAwareCallable
   * @throws Exception exception
   */
  @Override
  public T call() throws Exception {
    if (context != null) {
      RequestContextHolder.setRequestAttributes(context);
    }

    try {
      return task.call();
    } finally {
      RequestContextHolder.resetRequestAttributes();
    }
  }
}

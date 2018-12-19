package com.almis.awe.thread;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;

/**
 * Created by pgarcia on 07/04/2017.
 * @param <T>
 */
public class ContextAwareCallable<T> implements Callable<T> {
  private Callable<T> task;
  private RequestAttributes context;

  /**
   * Constructor
   *
   * @param task
   * @param context
   */
  public ContextAwareCallable(Callable<T> task, RequestAttributes context) {
    this.task = task;
    this.context = context;
  }

  /**
   * Call
   *
   * @return
   * @throws Exception
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

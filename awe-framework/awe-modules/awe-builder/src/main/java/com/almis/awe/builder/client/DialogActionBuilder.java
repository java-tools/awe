package com.almis.awe.builder.client;

/**
 * Dialog action builder
 *
 * @author pgarcia
 */
public class DialogActionBuilder extends ClientActionBuilder<DialogActionBuilder> {

  private static final String TYPE = "dialog";

  /**
   * Empty constructor
   */
  public DialogActionBuilder() {
    setType(TYPE);
  }

  /**
   * Constructor with a dialog target
   *
   * @param dialog Dialog to open
   */
  public DialogActionBuilder(String dialog) {
    setType(TYPE)
      .setTarget(dialog);
  }
}

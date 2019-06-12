package com.almis.awe.builder.screen.grid;

import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.screen.base.AbstractColumnBuilder;
import com.almis.awe.builder.screen.component.UploaderAttributes;
import com.almis.awe.model.entities.screen.component.grid.Column;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author pgarcia
 */
@Getter(AccessLevel.PRIVATE)
public class UploaderColumnBuilder extends AbstractColumnBuilder<UploaderColumnBuilder, Column> {

  private UploaderAttributes uploaderAttributes;

  public UploaderColumnBuilder() {
    super();
    this.uploaderAttributes = new UploaderAttributes(this);
  }

  @Override
  public Column build() {
    Column column = (Column) getUploaderAttributes().asUploader(new Column());

    return (Column) super.build(column)
      .setComponentType(Component.UPLOADER.toString());
  }

  /**
   * Set destination folder
   *
   * @param folder folder to store the file
   * @return Builder
   */
  public UploaderColumnBuilder setDestination(String folder) {
    getUploaderAttributes().setDestination(folder);
    return this;
  }
}

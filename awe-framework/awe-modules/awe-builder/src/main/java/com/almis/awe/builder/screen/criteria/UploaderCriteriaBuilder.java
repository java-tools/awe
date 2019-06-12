package com.almis.awe.builder.screen.criteria;

import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.screen.base.AbstractCriteriaBuilder;
import com.almis.awe.builder.screen.component.UploaderAttributes;
import com.almis.awe.model.entities.screen.component.criteria.AbstractCriteria;
import com.almis.awe.model.entities.screen.component.criteria.Criteria;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author pgarcia
 */
@Getter(AccessLevel.PRIVATE)
public class UploaderCriteriaBuilder extends AbstractCriteriaBuilder<UploaderCriteriaBuilder, AbstractCriteria> {

  private UploaderAttributes uploaderAttributes;

  public UploaderCriteriaBuilder() {
    super();
    this.uploaderAttributes = new UploaderAttributes(this);
  }

  @Override
  public AbstractCriteria build() {
    AbstractCriteria criterion = getUploaderAttributes().asUploader(new Criteria());

    return (AbstractCriteria) super.build(criterion)
      .setComponentType(Component.UPLOADER.toString());
  }

  /**
   * Set destination folder
   *
   * @param folder folder to store the file
   * @return Builder
   */
  public UploaderCriteriaBuilder setDestination(String folder) {
    getUploaderAttributes().setDestination(folder);
    return this;
  }
}

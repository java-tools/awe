/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen;

import com.almis.awe.builder.enumerates.OnClose;
import com.almis.awe.builder.screen.base.AbstractTagBuilder;
import com.almis.awe.model.entities.screen.component.Dialog;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author dfuentes
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class DialogBuilder extends AbstractTagBuilder<DialogBuilder, Dialog> {

  private OnClose onClose;
  private String icon;

  @Override
  public Dialog build() {
    return build(new Dialog());
  }

  @Override
  public Dialog build(Dialog dialog) {
    super.build(dialog)
      .setIcon(getIcon());

    if (getOnClose() != null) {
      dialog.setOnClose(getOnClose().toString());
    }

    return dialog;
  }
}

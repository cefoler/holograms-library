package com.cefoler.holograms.model.animation.type;

import com.cefoler.holograms.model.animation.AbstractAnimation;
import com.cefoler.holograms.model.animation.Animation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AnimationType {

  CIRCLE(AbstractAnimation.builder()
      .defaultYaw(0)
      .yawRate(10)
      .delay(2)
      .build());

  private final Animation animation;

}

package com.cefoler.holograms.model.animation.type;

import com.cefoler.holograms.model.animation.Animation;
import com.cefoler.holograms.model.animation.impl.CircleAnimation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AnimationType {

  CIRCLE(new CircleAnimation());

  private final Animation animation;

}

package com.cefoler.holograms.model.animation.type;

import com.cefoler.holograms.model.animation.impl.CircleAnimation;
import com.cefoler.holograms.model.animation.AbstractAnimation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AnimationType {

    CIRCLE(new CircleAnimation());

    private final AbstractAnimation animation;

}

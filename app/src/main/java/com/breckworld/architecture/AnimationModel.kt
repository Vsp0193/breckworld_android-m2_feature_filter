package com.breckworld.architecture

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes


data class AnimationModel(
        @AnimatorRes @AnimRes var enter: Int? = null,
        @AnimatorRes @AnimRes var exit: Int? = null,
        @AnimatorRes @AnimRes var popEnter: Int? = null,
        @AnimatorRes @AnimRes var popExit: Int? = null
)
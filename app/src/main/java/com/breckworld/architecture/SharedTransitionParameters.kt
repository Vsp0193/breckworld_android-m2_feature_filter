package com.breckworld.architecture

import android.view.View

data class SharedTransitionParameters(
        var sharedElements: HashMap<String, View>? = null, // [TransitionName, View element]
        var sharedElementsEnterTransition: Any? = null,
        var sharedElementsReturnTransition: Any? = null,
        var enterTransiton: Any? = null
)
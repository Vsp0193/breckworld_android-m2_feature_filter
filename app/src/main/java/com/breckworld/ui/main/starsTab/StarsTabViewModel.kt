package com.breckworld.ui.main.starsTab

import androidx.lifecycle.MutableLiveData
import com.breckworld.architecture.BaseMVVMViewModel

/**
 * @author Dmytro Bondarenko
 *         Date: 30.05.2019
 *         Time: 11:01
 *         E-mail: bondes87@gmail.com
 */
class StarsTabViewModel : BaseMVVMViewModel<StarsTabFragment.Events>() {
    fun isGuideStars(): Boolean {
        return repository.isGuideStars()
    }

    fun saveGuideStars(isGuideShow: Boolean) {
        repository.saveGuideStars(isGuideShow)
    }
}
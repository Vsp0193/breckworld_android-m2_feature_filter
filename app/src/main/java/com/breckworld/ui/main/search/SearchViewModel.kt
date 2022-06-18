package com.breckworld.ui.main.search

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.breckworld.App
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.extensions.Constants
import com.breckworld.livedata.Event
import com.breckworld.repository.local.model.CityModel
import com.breckworld.repository.local.model.SearchModel

/**
 * @author Dmytro Bondarenko
 *         Date: 04.06.2019
 *         Time: 16:19
 *         E-mail: bondes87@gmail.com
 */
class SearchViewModel : BaseMVVMViewModel<SearchFragment.Events>() {

    val attractions = MutableLiveData<Boolean>()
    val freeFun = MutableLiveData<Boolean>()
    val eatAndDrink = MutableLiveData<Boolean>()
    val isSearchButtonEnabled = MutableLiveData<Boolean>()
    val cities = createCities()
    var selectedCityPosition = 0
    var currentLocation: Location? = null

    fun onSearch() {
        _eventsLiveData.value = Event(SearchFragment.Events.GO_TO_SEARCH_RESULT)
    }

    fun createSearchModel(): SearchModel {
        return SearchModel(
            currentLocation?.latitude, currentLocation?.longitude,
            createTerms(), cities[selectedCityPosition].title, START_PAGE
        )
    }

    fun isGuideSearch(): Boolean {
        return repository.isGuideSearch()
    }

    fun saveGuideSearch(isGuideShow: Boolean) {
        repository.saveGuideSearch(isGuideShow)
    }

    private fun createTerms(): String? {
        var terms: String? = null
        if (attractions.value == true) {
            terms = App.getStringFromRes(R.string.attractions)
        }
        if (freeFun.value == true) {
            terms = if (!terms.isNullOrEmpty()) {
                terms + Constants.PLUS + App.getStringFromRes(R.string.free_fun)
            } else {
                App.getStringFromRes(R.string.free_fun)
            }
        }
        if (eatAndDrink.value == true) {
            terms = if (!terms.isNullOrEmpty()) {
                terms + Constants.PLUS + App.getStringFromRes(R.string.eat_and_drink)
            } else {
                App.getStringFromRes(R.string.eat_and_drink)
            }
        }
        return terms
    }

    private fun createCities(): ArrayList<CityModel> {
        val citiesNames = App.getStringArrayFromRes(R.array.cities_array)
        return arrayListOf(
            CityModel(citiesNames[0], R.drawable.ic_logo_circle),
            CityModel(citiesNames[1], R.drawable.city_thetford),
            CityModel(citiesNames[2], R.drawable.city_dereham),
            CityModel(citiesNames[3], R.drawable.city_swaffham),
            CityModel(citiesNames[4], R.drawable.city_attleborough),
            CityModel(citiesNames[5], R.drawable.city_watton)
        )
    }

    companion object {
        const val START_PAGE = 1
    }
}
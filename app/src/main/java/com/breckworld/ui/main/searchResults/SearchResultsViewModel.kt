package com.breckworld.ui.main.searchResults

import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.livedata.Event
import com.breckworld.repository.local.model.SearchModel
import com.breckworld.repository.remote.http.model.SearchResult

/**
 * @author Dmytro Bondarenko
 *         Date: 05.06.2019
 *         Time: 17:06
 *         E-mail: bondes87@gmail.com
 */
class SearchResultsViewModel : BaseMVVMViewModel<SearchResultsFragment.Events>() {

    var searchModel: SearchModel? = null
    var results = ArrayList<SearchResult>()
    var terms: List<String>? = null

    fun search() {
        showLoading()
        launchCoroutine({
            val response = repository.search(
                searchModel?.lat,
                searchModel?.lng,
                searchModel?.city?.toLowerCase(),
                searchModel?.page?.plus(1),
                searchModel?.term?.replace(" ", "")?.toLowerCase()
            ).await()
            hideLoading()
            if (response.isNotEmpty()) {
                results = ArrayList(response)
                _eventsLiveData.value = Event(SearchResultsFragment.Events.RESULT_LOADED)
            }
        }, { _, throwable ->
            hideLoading()
            showError(throwable.message)
        })
    }

}
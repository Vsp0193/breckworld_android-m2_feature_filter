package com.breckworld.livedata

import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName
import java.lang.NullPointerException

object GlobalEventController {
    private val events: HashMap<String, MutableLiveData<Event<Any>>> = HashMap()

    fun getGlobalEvent(key: String): MutableLiveData<Event<Any>> {
        if (key.isEmpty()) throw NullPointerException()
        if (!events.containsKey(key)) {
            val event = MutableLiveData<Event<Any>>()
            events[key] = event
            return event
        } else {
            return events[key]!!
        }
    }
}
package com.breckworld.repository.remote.http.error

import com.breckworld.App
import com.breckworld.R
import java.io.IOException

class NoNetworkException : IOException(App.applicationContext().getString(R.string.no_network_error))
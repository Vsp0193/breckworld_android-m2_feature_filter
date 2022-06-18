package com.breckworld.webservice

import android.content.Context
import android.os.Build
import android.util.Log
import com.breckworld.utils.Constant
import com.google.gson.GsonBuilder
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object ApiClient {

    private const val DEV_BASE_SERVER_URL = "https://api.geody.app/" //""https://viewing.online/api/v2.0/"
    private const val PROD_BASE_SERVER_URL = "https://api.geody.app/"

    var BASE_SERVER_URL: String = if (Constant.IS_PRODUCTION) PROD_BASE_SERVER_URL else DEV_BASE_SERVER_URL

    /**
     * @param serviceClass interface class
     * @param context      context of activity
     * @param <S>          Type of Class
     * @return interface class objectbui
    </S> */
    fun <S> createService(serviceClass: Class<S>?, context: Context?): S {
        // initialize the request queue, the queue instance will be created when it is accessed for the first time
        val builder = OkHttpClient.Builder()
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()
        builder.connectTimeout(240, TimeUnit.SECONDS)
        builder.readTimeout(240, TimeUnit.SECONDS)
        builder.writeTimeout(240, TimeUnit.SECONDS)
        val httpLoggingInterceptor = HttpLoggingInterceptor()

        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        // add logging as last interceptor
        builder.addInterceptor(httpLoggingInterceptor) // <-- this is the important line!
        val okHttpClient = enableTls12OnPreLollipop(builder).build()
        //OkHttpClient okHttpClient = builder.build();
        var retrofit: Retrofit? = null
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_SERVER_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(serviceClass)
    }

    fun enableTls12OnPreLollipop(client: OkHttpClient.Builder): OkHttpClient.Builder {
        // refer from https://github.com/square/okhttp/issues/2372
        if (Build.VERSION.SDK_INT < 22) {
            try {
                val trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(null as KeyStore?)
                val trustManagers = trustManagerFactory.trustManagers
                check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                    "Unexpected default trust managers:" + Arrays.toString(
                        trustManagers
                    )
                }
                val trustManager = trustManagers[0] as X509TrustManager
                val sc = SSLContext.getInstance("TLSv1.2")
                sc.init(null, arrayOf<TrustManager>(trustManager), null)
                client.sslSocketFactory(Tls12SocketFactory(sc.socketFactory), trustManager)
                val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .build()
                val specs: MutableList<ConnectionSpec> = ArrayList()
                specs.add(cs)
                specs.add(ConnectionSpec.COMPATIBLE_TLS)
                specs.add(ConnectionSpec.CLEARTEXT)
                client.connectionSpecs(specs)
            } catch (exc: Exception) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc)
            }
        }
        return client
    }

}
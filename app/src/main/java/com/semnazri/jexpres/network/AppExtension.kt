package com.semnazri.jexpres.network

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.semnazri.jexpres.BuildConfig
import com.semnazri.jexpres.model.ErrorMessage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

sealed class ResultCall<out T> {
    data class Success<T>(val data: T) : ResultCall<T>()
    data class Failed(val responseCode: Int, val errorMessage: String) : ResultCall<Nothing>()
    data class Error(val errorMessage: String) : ResultCall<Nothing>()
}

private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

suspend fun <T> Response<T>.callAwait(): ResultCall<T> {
    return try {
        val response = this
        if (response.isSuccessful)
            ResultCall.Success<T>(response.body()!!)
        else {
            val errorBody = response.errorBody()
            val errorResponse = Gson().fromJson(errorBody?.charStream(), ErrorMessage::class.java)
            ResultCall.Failed(response.code(), errorResponse.message ?: "")
        }
    } catch (e: HttpException) {
        val error = e.response()
        ResultCall.Failed(error?.code() ?: 0, error?.message() ?: "Internal Server Error")
    } catch (e: JsonParseException) {
        ResultCall.Error(e.message ?: "")
    } catch (e: JsonSyntaxException) {
        ResultCall.Error(e.message ?: "")
    } catch (e: SocketTimeoutException) {
        ResultCall.Error(e.message ?: "")
    } catch (e: SocketException) {
        ResultCall.Error(e.message ?: "")
    } catch (e: IOException) {
        ResultCall.Error(e.message ?: "")
    } catch (e: ConnectException) {
        ResultCall.Error(e.message ?: "")
    }
}

fun <A, B> ResultCall<A>.mapTo(block: (A) -> B): ResultCall<B> {
    return when (this) {
        is ResultCall.Success -> ResultCall.Success(block(this.data))
        is ResultCall.Failed -> ResultCall.Failed(this.responseCode, this.errorMessage)
        is ResultCall.Error -> ResultCall.Error(this.errorMessage)
    }
}

fun createOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(60, TimeUnit.MINUTES)
        .writeTimeout(60, TimeUnit.MINUTES)
        .readTimeout(60, TimeUnit.MINUTES)
        .build()
}

inline fun <reified T> createApi(okHttpClient: OkHttpClient): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASEURL)
        .client(okHttpClient)
        .client(createOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(T::class.java)
}

inline fun <T> LifecycleOwner.subscribeSingleState(
    liveData: LiveData<StateWrapper<T>>,
    crossinline onEventUnhandled: (T) -> Unit
) {
    liveData.observe(this) { it?.getEventIfNotHandled()?.let(onEventUnhandled) }
}

//inline fun <T> LifecycleOwner.subscribeParentState(
//    liveData: LiveData<StateWrapper<T>>,
//    crossinline onEventUnhandled: (T) -> Unit
//) {
//    liveData.observe(this, { it?.peekContent()?.let(onEventUnhandled) })
//}

inline fun <T> Fragment.subscribeParentSingleState(
    liveData: LiveData<StateWrapper<T>>,
    crossinline onEventUnhandled: (T) -> Unit
) {
    liveData.observe(viewLifecycleOwner) { it?.peekContent()?.let(onEventUnhandled) }
}

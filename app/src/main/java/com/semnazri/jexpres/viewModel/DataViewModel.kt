package com.semnazri.jexpres.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.semnazri.jexpres.PreferencesManager
import com.semnazri.jexpres.ProductAdapter
import com.semnazri.jexpres.base.BaseViewModel
import com.semnazri.jexpres.model.MockModelResponse
import com.semnazri.jexpres.model.ProductsItem
import com.semnazri.jexpres.network.NetworkHelper
import com.semnazri.jexpres.network.ResultCall
import com.semnazri.jexpres.network.StateWrapper
import com.semnazri.jexpres.repository.RepositoryData
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.util.*

class DataViewModel(
    private val preferencesManager: PreferencesManager,
    private val repository: RepositoryData,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {
    var dataItem: ArrayList<ProductsItem> = ArrayList()

    companion object {
        private const val DEFAULT_ERROR_MESSAGE = "Error loading result, please try again later"
    }

    sealed class Event {
        object getData : Event()
    }

    sealed class State {
        data class ResultData(val dataResponse: MockModelResponse) : State()
        data class ShowError(val errorMessage: String) : State()
        data class NoNetwork(val errorMessage: String) : State()
    }

    private val _state: MutableLiveData<StateWrapper<State>> = MutableLiveData()
    val state: LiveData<StateWrapper<State>> = _state

    private fun setState(state: State) {
        _state.value = StateWrapper(state)
    }

    fun callEvent(event: Event) {
        when (event) {
            is Event.getData -> getMock()
        }
    }

    private fun getMock() = launch {
        if (networkHelper.isNetworkConnected()) {
            when (val result = repository.requestMockData()) {
                is ResultCall.Error -> setState(State.ShowError(result.errorMessage))
                is ResultCall.Failed -> setState(
                    State.ShowError(
                        generateErrorMessage(
                            result.responseCode,
                            result.errorMessage
                        )
                    )
                )
                is ResultCall.Success -> {
                    dataItem = result.data.products as ArrayList<ProductsItem>
                    preferencesManager.apply {
                        temptdata = result.data
                    }
                    setState(State.ResultData(result.data))
                }
            }
        } else {
            setState(State.NoNetwork("No Network Detected"))
        }
    }

    private fun generateErrorMessage(responseCode: Int, errorMessage: String): String {
        return if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
            DEFAULT_ERROR_MESSAGE
        } else {
            errorMessage
        }
    }

    fun fliter(text: String, adapter: ProductAdapter) {

        val temp: ArrayList<ProductsItem> = ArrayList()
        if (text != "") {
            for (d in dataItem) {
                if (d.name?.toLowerCase(Locale.getDefault())?.contains(text) == true) {
                    temp.add(d)
                }
            }
            adapter.updatelist(temp)
        } else {
            adapter.updatelist(dataItem)
        }
    }
}
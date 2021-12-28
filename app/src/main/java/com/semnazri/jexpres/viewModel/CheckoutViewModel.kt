package com.semnazri.jexpres.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.semnazri.jexpres.PreferencesManager
import com.semnazri.jexpres.base.BaseViewModel
import com.semnazri.jexpres.network.StateWrapper
import kotlinx.coroutines.launch

class CheckoutViewModel(private val preferencesManager: PreferencesManager) : BaseViewModel() {

    sealed class Event {
        data class CalculateVoucher(
            val voucherCode: String,
            val price: Int,
            val ongkir: Int
        ) : Event()
    }

    sealed class State {
        data class ShowResultFinalPrice(val finalPrice: Double) : State()
        object VoucehrNotFOund : State()
    }

    private val _state: MutableLiveData<StateWrapper<State>> = MutableLiveData()
    val state: LiveData<StateWrapper<State>> = _state

    private fun setState(state: State) {
        _state.value = StateWrapper(state)
    }

    fun callEvent(event: Event) {
        when (event) {
            is Event.CalculateVoucher -> doCalculate(event.voucherCode, event.price, event.ongkir)
        }
    }

    private fun doCalculate(voucherCode: String, price: Int, ongkir: Int) = launch {

        val ongkir5 = preferencesManager.temptdata.voucher?.ongkir5
        val disc35 = preferencesManager.temptdata.voucher?.disc35
        if (voucherCode.contains(ongkir5?.kode.toString())) {
            val finalOngkir = ongkir.minus(ongkir5?.potonganMaksimal!!)
            val finalPrices = price.plus(finalOngkir)
            setState(State.ShowResultFinalPrice(finalPrices.toDouble()))
        } else if (voucherCode.contains(disc35?.kode.toString())) {
            val discount = (disc35?.nilaiPersen?.toDouble()?.div(price))?.times(100)
            val finalPrices = discount?.let { price.minus(it) }
            if (finalPrices != null) {
                if (finalPrices > disc35.potonganMaksimal!!.toDouble()) {
                    val maxPrice = price.minus(disc35.potonganMaksimal)
                    setState(State.ShowResultFinalPrice(maxPrice.toDouble()))
                } else {
                    setState(State.ShowResultFinalPrice(finalPrices))
                }
            }
        } else {
            setState(State.VoucehrNotFOund)
        }
    }


}
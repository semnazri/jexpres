package com.semnazri.jexpres.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.semnazri.jexpres.databinding.ActivityCheckoutPageBinding
import com.semnazri.jexpres.network.subscribeSingleState
import com.semnazri.jexpres.viewModel.CheckoutViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutPageBinding
    private val viewModel: CheckoutViewModel by viewModel()
    val ongkirValue = 50000
    val total_price = 300000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

        observeData()
    }

    private fun initView() {
        binding.distanceVal.text = "Ongkos Kirim : $ongkirValue"
        binding.priceBefore.text = "Total Price : $total_price"

        binding.btnPakaiVoucher.setOnClickListener {
            viewModel.callEvent(CheckoutViewModel.Event.CalculateVoucher(binding.voucherCode.text.toString(),total_price,ongkirValue))
        }
    }

    private fun observeData() {
        subscribeSingleState(viewModel.state) {
            when (it) {
                is CheckoutViewModel.State.ShowResultFinalPrice -> binding.totalBayar.text =
                    "Total Bayar : ${it.finalPrice}"
                is CheckoutViewModel.State.VoucehrNotFOund -> Toast.makeText(
                    this,
                    "Voucher Not Found",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
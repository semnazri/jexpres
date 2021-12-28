package com.semnazri.jexpres.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.semnazri.jexpres.ProductAdapter
import com.semnazri.jexpres.databinding.ActivityMainBinding
import com.semnazri.jexpres.network.subscribeSingleState
import com.semnazri.jexpres.viewModel.DataViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity() {
    private val viewModel: DataViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding
    private val prodAdapter by lazy { ProductAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.callEvent(DataViewModel.Event.getData)

        binding.rvProduct.apply {
            setHasFixedSize(false)
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = prodAdapter
        }

        binding.edtSearch.addTextChangedListener (object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                    viewModel.fliter(s.toString().toLowerCase(Locale.getDefault()), prodAdapter)
            }
        })

        binding.btnCheckout.setOnClickListener {
            startActivity(Intent(this, CheckoutActivity::class.java))
        }

        observeData()
    }

    private fun observeData() {
        subscribeSingleState(viewModel.state) {
            when (it) {
                is DataViewModel.State.NoNetwork -> Toast.makeText(
                    this,
                    it.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
                is DataViewModel.State.ResultData -> {
                    prodAdapter.items = it.dataResponse.products.toMutableList()
                }
                is DataViewModel.State.ShowError -> Toast.makeText(
                    this,
                    it.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
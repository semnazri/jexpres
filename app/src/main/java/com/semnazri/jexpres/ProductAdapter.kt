package com.semnazri.jexpres

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.semnazri.jexpres.databinding.ItemProductBinding
import com.semnazri.jexpres.model.ProductsItem

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    var items = mutableListOf<ProductsItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun updatelist(list: ArrayList<ProductsItem>) {
        items = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemBinding: ItemProductBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ItemProductBinding.bind(holder.itemView).apply {
            val dataPos = items[position]
            prodName.text = dataPos.name
            prodPrice.text = dataPos.price.toString()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
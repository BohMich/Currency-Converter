package com.littlebig.testapp.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.littlebig.testapp.R
import androidx.recyclerview.widget.RecyclerView
import com.littlebig.testapp.models.ExchangeValue
import kotlinx.android.synthetic.main.item_view_currency.view.*


// Main adapter used for managing main feed list within the main Recycler View
class RatesListAdapter(val context: Context,
                            //val stringFormatter: StringFormatter = StringFormatter(),
                            val clickListener: (String) -> Unit
) : RecyclerView.Adapter<RatesListAdapter.ViewHolder>() {

    private var items: List<ExchangeValue> = ArrayList()

    fun setItems(items: List<ExchangeValue>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_view_currency, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Prepare fetched data
        val currency = items[position].currencyName
        val rate = items[position].rate

        // Set the data within the view
        (holder as? ItemViewHolder)?.currencyName?.text = currency.toString()
        (holder as? ItemViewHolder)?.currencyRate?.text = rate.toString()

        // Set the onClickListener
        (holder as? ItemViewHolder)?.container?.setOnClickListener{
            val itemId = items[position].currencyName
            clickListener(itemId)
        }
    }

    abstract class ViewHolder (view: View) : RecyclerView.ViewHolder(view)

    inner class ItemViewHolder (view: View) : ViewHolder(view) {
        val container = view.row_container
        val currencyName = view.currency_name_label
        val currencyRate = view.currency_rate_label
    }
}


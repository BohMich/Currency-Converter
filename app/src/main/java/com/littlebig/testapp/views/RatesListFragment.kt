package com.littlebig.testapp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.littlebig.testapp.R
import com.littlebig.testapp.dependencyinjection.FreeAgentTestApp
import com.littlebig.testapp.models.SingleExchange
import com.littlebig.testapp.utils.DataFetchingCallback
import com.littlebig.testapp.viewmodels.DetailedViewViewModel
import kotlinx.android.synthetic.main.rates_view.*
import javax.inject.Inject
import android.widget.TextView
import android.widget.LinearLayout
import java.text.DateFormat
import java.text.SimpleDateFormat


class RatesListFragment : Fragment(), DataFetchingCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: DetailedViewViewModel

    //@Inject
    //lateinit var stringFormatter: StringFormatter

    private val STATE_LOADING_ERROR = "STATE_LOADING_ERROR"
    private val STATE_CONTENT_LOADED = "STATE_CONTENT_LOADED"

    init {
        FreeAgentTestApp.mainComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Initialize ViewModel
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailedViewViewModel::class.java)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.rates_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Fetch selected item from the back-end and load it into the view
        fetchSelectedCurrencies()

        // Setup Cross Button
        val closingOnClickListener = View.OnClickListener{ activity?.onBackPressed() }

        // Setup closing on the grey fields' click
        spacing_top.setOnClickListener(closingOnClickListener)
        spacing_bottom.setOnClickListener(closingOnClickListener)
    }


    // UI setup methods

    private fun setViewState(state: String, currencyObject: List<SingleExchange>? = null) {
        when(state) {
            STATE_LOADING_ERROR -> setupLoadingErrorView()
            STATE_CONTENT_LOADED -> currencyObject?.let { setupContentLoadedView(it) }
        }
    }

    private fun setupLoadingErrorView() {
        (activity as? MainActivity)?.displayErrorDialog {
            fetchSelectedCurrencies()
        }
    }

    private fun setupContentLoadedView(currencySet: List<SingleExchange>) {
        fragmentProgressBar.visibility = View.GONE

        context?.let {

            // Set table headers
            (rates_table_column_date_text as TextView).text = getString(R.string.rates_table_date)
            (rates_view_base_header_value as TextView).text = currencySet[0].baseAmount.toString()
            (rates_view_base_header_name as TextView).text = currencySet[0].base
            (rates_table_column_cur1_header as TextView).text = currencySet[0].currencyArray[0].currencyName
            (rates_table_column_cur2_header as TextView).text = currencySet[0].currencyArray[1].currencyName

            // Populate table
            currencySet.forEach { entry: SingleExchange ->
                val dateFormatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")

                val entry_date = TextView(context)

                entry_date.text = dateFormatter.format(entry.date)
                entry_date.textSize = resources.getDimension(R.dimen.rates_table_values)
                entry_date.setPadding(0,0,0,resources.getDimension(
                                           R.dimen.rates_table_values_padding).toInt())
                (rates_table_column_date as LinearLayout).addView(entry_date)

                val entry_currency_1 = TextView(context)
                entry_currency_1.text = entry.currencyArray[0].rate.toString()
                entry_currency_1.textSize = resources.getDimension(R.dimen.rates_table_values)
                entry_currency_1.setPadding(0,0,0,resources.getDimension(
                                            R.dimen.rates_table_values_padding).toInt())
                (rates_table_column_cur1 as LinearLayout).addView(entry_currency_1)

                val entry_currency_2 = TextView(context)
                entry_currency_2.text = entry.currencyArray[1].rate.toString()
                entry_currency_2.textSize = resources.getDimension(R.dimen.rates_table_values)
                entry_currency_2.setPadding(0,0,0,resources.getDimension(
                                            R.dimen.rates_table_values_padding).toInt())
                (rates_table_column_cur2 as LinearLayout).addView(entry_currency_2)
            }
        }
    }


    // Data fetching methods

    private fun fetchSelectedCurrencies() {
        val currency1 = this.arguments?.getString("currency1")
        val currency2 = this.arguments?.getString("currency2")
        val amount = this.arguments?.getString("selectedAmount")
        viewModel.getCurrencyRates(this, currency1,currency2,amount)
    }

    override fun fetchingDone(payload: Any) {
        if ((payload as? List<SingleExchange>) != null) {
            setViewState(STATE_CONTENT_LOADED, payload)
        } else {
            setViewState(STATE_LOADING_ERROR)
        }
    }

    override fun fetchingError() {
        setViewState(STATE_LOADING_ERROR)
    }
}
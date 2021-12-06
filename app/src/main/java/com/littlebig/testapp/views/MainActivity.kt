package com.littlebig.testapp.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.littlebig.testapp.R
import com.littlebig.testapp.dependencyinjection.FreeAgentTestApp
import com.littlebig.testapp.utils.DataFetchingCallback
import com.littlebig.testapp.viewmodels.MainPageViewModel
import androidx.lifecycle.ViewModelProviders
import com.littlebig.testapp.models.SingleExchange
import com.littlebig.testapp.utils.UserSelectionCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_top_panel.*
import kotlinx.android.synthetic.main.loading_badge.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), DataFetchingCallback, UserSelectionCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainPageViewModel

    private val STATE_FILTERING_IN_PROGRESS = "STATE_FILTERING_IN_PROGRESS"
    private val STATE_LOADING_ERROR = "STATE_LOADING_ERROR"
    private val STATE_CONTENT_LOADED = "STATE_CONTENT_LOADED"

    init {
        FreeAgentTestApp.mainComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ViewModel
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainPageViewModel::class.java)

        // Initialize RecyclerView (feed items)
        setupRecyclerView()

        // Initialize Search Panel
        setupAmountInputField()

        // Fetch feed items from the back-end and load them into the view
        fetchCurrencies()
    }


    // UI setup methods

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        rates_list_recycler.layoutManager = layoutManager
        rates_list_recycler.adapter = RatesListAdapter(this) { selectedCurrency: String ->
            selectValueFromList(selectedCurrency)
        }
    }

    private fun setupAmountInputField() {
        amount_input_field.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                val inputAmount: String = amount_input_field.text.toString()
                updateCurrencies(inputAmount)
            }
        })
    }

    // UI management methods

    private fun displayRatesList(selectedCurrencies: ArrayList<String>) {
        val fragment = RatesListFragment()
        val bundle = Bundle()
        bundle.putString("currency1", selectedCurrencies[0])
        bundle.putString("currency2", selectedCurrencies[1])
        bundle.putString("selectedAmount", amount_input_field.text.toString())
        fragment.arguments = bundle

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.main_content_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun loadItemsIntoList(items: SingleExchange) {
        (rates_list_recycler.adapter as RatesListAdapter).setItems(items.currencyArray)
    }

    private fun selectValueFromList(currency: String){
        viewModel.selectedCurrency(this,currency)
    }

    fun displayErrorDialog(tryAgainAction: () -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.loading_problem_check_the_internet_connection)
        builder.setPositiveButton(R.string.try_again) { _, _ ->
            tryAgainAction()
        }
        builder.create().show()
    }

    private fun setViewState(state: String) {
        when(state) {
            STATE_LOADING_ERROR -> setupLoadingErrorView()
            STATE_CONTENT_LOADED -> setupContentLoadedView()
        }
    }


    private fun setupLoadingErrorView() {
        displayErrorDialog {
            fetchCurrencies()
        }
    }

    private fun setupContentLoadedView() {
        loading_container.visibility = View.GONE
        amount_input_field.isEnabled = true
    }


    // Data fetching methods

    private fun fetchCurrencies() {
        viewModel.getCurrencies(this,null)
    }

    private fun updateCurrencies(value: String)
    {
        viewModel.getCurrencies(this, value)
    }


    // Data Fetching Callback interface methods

    override fun fetchingDone(payload: Any) {
        if ((payload as? SingleExchange) != null) {
            loadItemsIntoList(payload)
            setViewState(STATE_CONTENT_LOADED)
        } else {
            setViewState(STATE_LOADING_ERROR)
        }
    }

    override fun fetchingError() {
        setViewState(STATE_LOADING_ERROR)
    }

    // User Selection Callback interface methods

    override fun currencySetSelected(payload: Any){
        if((payload as? ArrayList<String>) != null){
            displayRatesList(payload)
        } else {
            //TODO handle fail state.
        }
    }
}
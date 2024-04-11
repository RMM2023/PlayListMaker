package com.practicum.playlistmaker

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class SearchActivity : AppCompatActivity() {
    companion object{
        const val PREF_NAME = "pref_name"
    }

    lateinit var trackRecycler : RecyclerView
    lateinit var notFoundLayout : LinearLayout
    lateinit var notConnectedLayout : LinearLayout
    lateinit var searchHistoryLayout: LinearLayout
    lateinit var searchHistoryRecyclerView: RecyclerView
    private var sharedPreferencesHistory: SharedPreferences? = null
    private lateinit var searchHistoryClass: SearchHistoryHelper
    var trackList = mutableListOf<Track>()
    var historyTrackList = mutableListOf<Track>()

    lateinit var searchEditText : EditText
    lateinit var adapter : TrackAdapter

    lateinit var historyAdapter: TrackAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)
        trackRecycler = findViewById(R.id.track_recycler)
        adapter = TrackAdapter(trackList){
                track -> searchHistoryClass.add(track)
            historyTrackList.add(track)
            historyAdapter.updateList(historyTrackList)
        }
        trackRecycler.adapter = adapter
        trackRecycler.layoutManager = LinearLayoutManager(this)

        val clearHistoryButton = findViewById<Button>(R.id.history_clear_button)
        clearHistoryButton.setOnClickListener {
            searchHistoryClass.clear()
            historyTrackList.clear()
            historyAdapter.updateList(historyTrackList)
            searchHistoryLayoutVis(false)
            trackRecyclerViewVis(true)
        }

        //БЛОК НАЧАЛЬНОГО ЗАПОЛНЕНИЯ ИСТОРИИ
        sharedPreferencesHistory = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        searchHistoryClass = SearchHistoryHelper(sharedPreferencesHistory!!)
        searchHistoryLayout = findViewById(R.id.search_history_layout)
        searchHistoryRecyclerView = findViewById(R.id.search_history_recycle_view)
        historyAdapter = TrackAdapter(historyTrackList){
            //Нажатие на трек в истории ничего не делает
        }
        searchHistoryRecyclerView.adapter = historyAdapter
        searchHistoryRecyclerView.layoutManager = LinearLayoutManager(this);
        readHistory()
        if(historyAdapter.trackList.isNotEmpty()){
            searchHistoryLayoutVis(true)
            trackRecyclerViewVis(false)
        } else {
            trackRecyclerViewVis(true)
        }




        val updateButton = findViewById<Button>(R.id.update_button)
        searchEditText = findViewById(R.id.searchEditText)
        val searchClearButton = findViewById<ImageView>(R.id.searchClearButton)
        val searchBackButton = findViewById<Button>(R.id.searchBackButton)
        notFoundLayout = findViewById(R.id.Not_found_layout)
        notConnectedLayout = findViewById(R.id.Not_connected_layout)

        searchBackButton.setOnClickListener{
            finish()
        }
        updateButton.setOnClickListener{
            onUpdate()
        }
        searchClearButton.setOnClickListener{
            searchEditText.setText("")
            hideKeyboard()
            searchEditText.clearFocus()
            adapter.clearList()
            notConnectedLayoutVis(false)
            notFoundLayoutVis(false)
            readHistory()
            if (historyTrackList.isEmpty()){
                searchHistoryLayoutVis(false)
            }else{
                searchHistoryLayoutVis(true)
            }
        }

        searchEditText.setOnEditorActionListener {_, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchHistoryLayoutVis(false)
                trackRecyclerViewVis(true)
                onSearch(searchEditText.text.toString())
                true
            } else {
                false
            }
        }

        val simpleTextWatcher = object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()){
                    searchClearButton.visibility=GONE
                    trackRecyclerViewVis(false)
                    notConnectedLayoutVis(false)
                    notFoundLayoutVis(false)
                    readHistory()
                    if (historyTrackList.isEmpty()){
                        searchHistoryLayoutVis(false)
                    }else{
                        searchHistoryLayoutVis(true)
                    }
                    hideKeyboard()
                }else{
                    searchClearButton.visibility= VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val editText = findViewById<EditText>(R.id.searchEditText)
        outState.putString("SAVE_TEXT", editText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val saveText = savedInstanceState.getString("SAVE_TEXT", "")
        val editText = findViewById<EditText>(R.id.searchEditText)
        editText.setText(saveText)
    }
    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
    fun notFoundLayoutVis(vis : Boolean){
        if (vis == true){
            notFoundLayout.visibility = View.VISIBLE
        }
        else{
            notFoundLayout.visibility = View.GONE
        }
    }
    fun notConnectedLayoutVis(vis : Boolean){
        if (vis == true){
            notConnectedLayout.visibility = View.VISIBLE
        }
        else{
            notConnectedLayout.visibility = View.GONE
        }
    }

    fun searchHistoryLayoutVis(vis: Boolean){
        if (vis == true){
            searchHistoryLayout.visibility = View.VISIBLE
        } else{
            searchHistoryLayout.visibility = View.GONE
        }
    }

    fun trackRecyclerViewVis(vis: Boolean){
        if(vis == true){
            trackRecycler.visibility = View.VISIBLE
        } else{
            trackRecycler.visibility = View.GONE
        }
    }

    fun readHistory() {
        historyTrackList = searchHistoryClass.read().toMutableList()
        historyAdapter.updateList(historyTrackList)
    }

    fun onUpdate(){
        notFoundLayoutVis(false)
        notConnectedLayoutVis(false)
        onSearch(searchEditText.text.toString())
    }
    fun onSearch(query : String){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ITunesApiService :: class.java)
        val call = service.search(query)

        call.enqueue(object : Callback<SearchResponse>{
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful){
                    val searchResponse = response.body()
                    searchResponse?.let {
                        if (it.resultCount > 0){
                            trackList = it.results
                            adapter.updateList(trackList)
                            trackRecycler.scrollToPosition(0)
                        }
                        else{
                            adapter.clearList()
                            notConnectedLayoutVis(false)
                            notFoundLayoutVis(true)
                        }
                    }
                }
                else{
                    notFoundLayoutVis(false)
                    notConnectedLayoutVis(true)
                    adapter.clearList()
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                adapter.clearList()
                notConnectedLayoutVis(true)
                notFoundLayoutVis(false)
            }
        })
    }
}
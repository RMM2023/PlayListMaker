package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val searchClearButton = findViewById<ImageView>(R.id.searchClearButton)
        val searchBackButton = findViewById<Button>(R.id.searchBackButton)

        searchBackButton.setOnClickListener{
            finish()
        }
        searchClearButton.setOnClickListener{
            searchEditText.setText("")
        }
        val simpleTextWatcher = object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()){
                    searchClearButton.visibility=GONE
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
}
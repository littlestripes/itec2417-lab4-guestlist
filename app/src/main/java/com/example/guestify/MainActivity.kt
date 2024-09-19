package com.example.guestify

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider

const val LAST_GUEST_NAME_KEY = "lastguestnamebundlekeyiswhatthisis"

class MainActivity : AppCompatActivity() {

    // var guestNames: MutableList<String> = mutableListOf()
    private val guestListViewModel: GuestListViewModel by lazy {
        ViewModelProvider(this).get(GuestListViewModel::class.java)
    }

    lateinit private var addGuestButton: Button
    lateinit private var newGuestEditText: EditText
    lateinit private var guestList: TextView
    lateinit private var lastGuestAdded: TextView
    lateinit private var clearGuestList: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        addGuestButton = findViewById(R.id.add_guest_button)
        newGuestEditText = findViewById(R.id.new_guest_input)
        guestList = findViewById(R.id.list_of_guests)
        lastGuestAdded = findViewById(R.id.last_guest_added)
        clearGuestList = findViewById(R.id.clear_guests_button)

        addGuestButton.setOnClickListener {
            addNewGuest()
        }

        clearGuestList.setOnClickListener {
            guestListViewModel.clearGuestList()
            lastGuestAdded.text = ""
            updateGuestList()
        }

        val savedLastGuestMessage = savedInstanceState?.getString(LAST_GUEST_NAME_KEY)
        lastGuestAdded.text = savedLastGuestMessage

        updateGuestList()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_GUEST_NAME_KEY, lastGuestAdded.text.toString())
    }

    fun addNewGuest() {
        val newGuestName = newGuestEditText.text.toString()
        if (newGuestName.isNotBlank()) {
            // guestNames.add(newGuestName)
            guestListViewModel.addGuest(newGuestName)
            updateGuestList()
            newGuestEditText.text.clear()
            lastGuestAdded.text = getString(R.string.last_guest_message, newGuestName)
        }
    }

    fun updateGuestList() {
        val guestDisplay = guestListViewModel.getSortedGuestNames().joinToString(separator="\n")
        guestList.text = guestDisplay
    }
}
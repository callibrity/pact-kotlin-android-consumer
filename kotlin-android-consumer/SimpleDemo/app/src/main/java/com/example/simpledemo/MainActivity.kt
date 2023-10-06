package com.example.simpledemo

// JSON parse

// Imports for

// Networking

// View model
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    var textViewTitle: TextView? = null
    var textViewDescription: TextView? = null
    var buttonFetch: Button? = null
    val viewModel: MainActivityViewModel by viewModels()


    private fun fetch(sUrl: String): UserResponse? {
        var userResponse: UserResponse? = null
        lifecycleScope.launch(Dispatchers.IO) {
        val retrofit = Retrofit.Builder()
            .baseUrl(sUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(ClientApi::class.java)
            userResponse = api.getUser("8de28ede-a489-4fff-b190-e59a7ce395b8","Bearer AAAAABCDE").body()

            withContext(Dispatchers.Main) {
                // Update view model
                Log.i("info","printing from info..." + userResponse?.firstName)
                viewModel.title.value = userResponse?.firstName + " " + userResponse?.lastName
                viewModel.description.value = userResponse?.address
            }

        }
        return userResponse
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        textViewTitle = findViewById(R.id.textview_blog_title)
        textViewDescription = findViewById(R.id.textview_blog_description)
        buttonFetch = findViewById(R.id.button_fetch)

        buttonFetch?.setOnClickListener(View.OnClickListener {
            // Launch get request
            // http://10.0.2.2:14448/
            fetch("https://0acc27cb-214c-41b6-bb6f-f314774ca519.mock.pstmn.io/")
        })

        viewModel.title.observe(this, Observer {
            textViewTitle?.text = it
        })

        viewModel.description.observe(this, Observer {
            textViewDescription?.text = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
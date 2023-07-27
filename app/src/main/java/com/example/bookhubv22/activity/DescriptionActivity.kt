package com.example.bookhubv22.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.Volley
import com.example.bookhubv22.R
import org.w3c.dom.Text

class DescriptionActivity : AppCompatActivity() {
    lateinit var txtBookName: TextView
    lateinit var txtBookDesc: TextView
    lateinit var txtBookAuthor : TextView
    lateinit var txtBookPrice : TextView
    lateinit var txtBookRating : TextView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar : ProgressBar
    lateinit var txtBookImage : ImageView
    lateinit var favButton: Button
    var bookId : String? = "100"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookImage = findViewById(R.id.txtBookImage)
        txtBookDesc = findViewById(R.id.bookDescription)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        progressLayout = findViewById(R.id.descriptionProgressLayout)
        progressBar = findViewById(R.id.descriptionProgress)
        favButton = findViewById(R.id.addToFavourites)


        progressLayout.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE

        if(intent !=null){
            bookId = intent.getStringExtra("book_id")
        }
        else{
            Toast.makeText(this@DescriptionActivity,"Some unexpected error occurred",Toast.LENGTH_SHORT).show()
            Log.d("Error", "Intent is null, line 45, DescriptionActivity.kt")
            finish()
        }
        if (bookId == "100"){
            Toast.makeText(this@DescriptionActivity,"Some unexpected error occurred",Toast.LENGTH_SHORT).show()
            Log.d("Error", " book_id is invalid or isn't able to be retrieved, line 46, DescriptionActivity.kt")
            finish()
        }
        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book"


    }
}
package com.example.bookhubv22.activity

import com.example.bookhubv22.database.BookDao
import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookhubv22.R
import com.android.volley.Response
import com.example.bookhubv22.adapter.DashboardRecyclerAdapter
import com.example.bookhubv22.database.BookDatabase
import com.example.bookhubv22.database.BookEntity
import com.example.bookhubv22.utils.Book
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
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
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    var bookId : String? = "100"
    lateinit var bookEntity: BookEntity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        toolbar = findViewById(R.id.toolbarDescription)
        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookImage = findViewById(R.id.txtBookImage)
        txtBookDesc = findViewById(R.id.bookDescription)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        progressLayout = findViewById(R.id.descriptionProgressLayout)
        progressBar = findViewById(R.id.descriptionProgress)
        favButton = findViewById(R.id.addToFavourites)

//        progressLayout.visibility = View.VISIBLE
//        progressBar.visibility = View.VISIBLE

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
        val url = "http://13.235.250.119/v1/book/get_book/"
        val jsonParams = JSONObject()
        jsonParams.put("book_id",bookId)

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonParams,
            Response.Listener {
                try{
                    val success = it.getBoolean("success")
                    if(success){
                        val bookJsonObject = it.getJSONObject("book_data")
                        progressBar.visibility = View.GONE
                        progressLayout.visibility = View.GONE

                        Picasso.get().load(bookJsonObject.getString("image")).into(txtBookImage)
                        txtBookName.text = bookJsonObject.getString("name")
                        txtBookAuthor.text = bookJsonObject.getString("author")
                        txtBookPrice.text = bookJsonObject.getString("price")
                        txtBookRating.text = bookJsonObject.getString("rating")
                        txtBookDesc.text = bookJsonObject.getString("description")
                        val bookImageUrl = bookJsonObject.getString("image")
                        bookEntity = BookEntity(
                            bookId?.toInt() as Int,
                            txtBookName.text.toString(),
                            txtBookAuthor.text.toString(),
                            txtBookPrice.text.toString(),
                            txtBookRating.text.toString(),
                            txtBookDesc.text.toString(),
                            bookImageUrl

                        )

                        val checkFav = DBAsyncTask(applicationContext, bookEntity,1).execute()
                        val isFav = checkFav.get()

                        if(isFav){
                            favButton.text = "Remove from Favourites"
                            val favColor = ContextCompat.getColor(applicationContext,R.color.red)
                            favButton.setBackgroundColor(favColor)
                        }
                        else{
                            favButton.text = "Add to Favourites"
                            val favColor = ContextCompat.getColor(applicationContext, R.color.green)
                            favButton.setBackgroundColor(favColor)
                        }
                        favButton.setOnClickListener {
                            if(!DBAsyncTask(applicationContext,bookEntity,1).execute().get()){
                                val async = DBAsyncTask(applicationContext,bookEntity,2).execute()
                                val result = async.get()
                                if(result){
                                    Toast.makeText(this@DescriptionActivity,"Book added to favourites",Toast.LENGTH_SHORT).show()
                                    favButton.text = "Remove from favourites"
                                    val favColor = ContextCompat.getColor(applicationContext,R.color.red)
                                    favButton.setBackgroundColor(favColor)
                                }else{
                                    Toast.makeText(this@DescriptionActivity,"Some error occured : 4",Toast.LENGTH_SHORT).show()
                                }
                            }
                            else{
                                val async = DBAsyncTask(applicationContext,bookEntity,3).execute()
                                val result = async.get()
                                if(result){
                                    Toast.makeText(this@DescriptionActivity,"Book removed from favourites",Toast.LENGTH_SHORT).show()
                                    favButton.text = "Add to Favourites"
                                    val favColor = ContextCompat.getColor(applicationContext,R.color.green)
                                    favButton.setBackgroundColor(favColor)
                                }
                                else{
                                    Toast.makeText(this@DescriptionActivity,"Some error occurred : 5",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    else{
                        Toast.makeText(this@DescriptionActivity,"Some error occurred 1",Toast.LENGTH_SHORT).show()
                    }
                }catch (_: JSONException){
                    Toast.makeText(this@DescriptionActivity,"Some error occurred 2",Toast.LENGTH_SHORT).show()

                }
            },
            Response.ErrorListener {
                Toast.makeText(this@DescriptionActivity,"Some error occurred 3",Toast.LENGTH_SHORT).show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "b7e158ca8c6cfb"
                return headers
            }
        }
        queue.add(jsonObjectRequest)

    }
    class DBAsyncTask(val context: Context, val bookEntity: BookEntity , val mode : Int) : AsyncTask<Void, Void, Boolean>() {

        //mode 1 -> Check DB if the book is favourite or not
        //mode 2 -> Save the book into DB as favourite
        //mode 3 -> Remove the favourite book

        val db = Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {
            when(mode){
                1 -> {
                    val book: BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book != null
                }

                2 -> {
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }

                3 -> {
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }
}
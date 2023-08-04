package com.example.bookhubv22.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookhubv22.R
import com.example.bookhubv22.database.BookEntity
import com.squareup.picasso.Picasso

class FavouritesRecyclerAdapter(val context: Context, val bookList: List<BookEntity>) : RecyclerView.Adapter<FavouritesRecyclerAdapter.FavouritesViewHolder>() {

    class FavouritesViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtBookName  = view.findViewById<TextView>(R.id.txtFavBookTitle)
        val txtBookAuthor = view.findViewById<TextView>(R.id.txtFavBookAuthor)
        val txtBookPrice = view.findViewById<TextView>(R.id.txtFavBookPrice)
        val txtBookRating = view.findViewById<TextView>(R.id.txtFavBookRating)
        val imgBookImage = view.findViewById<ImageView>(R.id.imgFavBookImage)
        val llContent = view.findViewById<LinearLayout>(R.id.llFavContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favourites_single_row, parent, false)
        return FavouritesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  bookList.size
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        val book = bookList[position]

        holder.txtBookName.text = book.bookName
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.book_app_icon_web).into(holder.imgBookImage)
    }
}
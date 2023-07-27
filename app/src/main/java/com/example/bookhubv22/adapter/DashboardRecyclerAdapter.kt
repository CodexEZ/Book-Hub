package com.example.bookhubv22.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import com.example.bookhubv22.R
import com.example.bookhubv22.activity.DescriptionActivity
import com.example.bookhubv22.utils.Book
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context: Context, val itemList: ArrayList<Book>) : RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>(){
    class DashboardViewHolder(view:View):RecyclerView.ViewHolder(view){
        val bookName = view.findViewById<TextView>(R.id.txtBookName)
        val bookAuthor = view.findViewById<TextView>(R.id.txtAuthorName)
        val bookCost = view.findViewById<TextView>(R.id.txtBookCost)
        val bookRating = view.findViewById<TextView>(R.id.txtBookRating)
        val bookImage = view.findViewById<ImageView>(R.id.imgBookImage)
        val relativeLayout = view.findViewById<RelativeLayout>(R.id.bookInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dashboard_single_row,parent,false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val text = itemList[position]
        holder.bookName.text = text.bookName
        holder.bookAuthor.text = text.bookAuthor
        holder.bookCost.text = text.bookPrice
        holder.bookRating.text = text.bookRating
        Picasso.get().load(text.bookImage).error(R.drawable.ic_book_image).into(holder.bookImage)
        holder.relativeLayout.setOnClickListener {
            val intent = Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id", text.bookId)
            Toast.makeText(context,"Loading description for ${text.bookName}",Toast.LENGTH_SHORT).show()
            context.startActivity(intent)

        }
    }

}
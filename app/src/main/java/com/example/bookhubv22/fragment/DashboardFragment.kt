package com.example.bookhubv22.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookhubv22.ARG_PARAM1
import com.example.bookhubv22.ARG_PARAM2
import com.example.bookhubv22.R
import com.example.bookhubv22.adapter.DashboardRecyclerAdapter
import com.example.bookhubv22.utils.Book
import com.example.bookhubv22.utils.ConnectionManager
import org.json.JSONException
import java.util.Collections

class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var layoutManager: LinearLayoutManager
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var recyclerDashboard : RecyclerView
    var bookInfoList = arrayListOf<Book>()
    var ratingComparator = Comparator<Book> { book1, book2 ->
        if(book1.bookRating.compareTo(book2.bookRating, true)==0){
            book1.bookName.compareTo(book2.bookName,true)
        }
        else{
            book1.bookRating.compareTo(book2.bookRating,true)
        }
    }


    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        setHasOptionsMenu(true)

        recyclerDashboard = view.findViewById<RecyclerView>(R.id.recycle)
        layoutManager = LinearLayoutManager(activity)
        progressLayout = view.findViewById(R.id.loading)
        progressBar = view.findViewById(R.id.progressBar)



        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books"
        progressLayout.visibility = View.VISIBLE

        if(ConnectionManager().checkConnectivity(activity as Context)) {

            val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                Response.Listener {
                    try {
                        progressLayout.visibility = View.GONE
                        val success = it.getBoolean("success")
                        if (success) {
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val bookJsonObjectRequest = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJsonObjectRequest.getString("book_id"),
                                    bookJsonObjectRequest.getString("name"),
                                    bookJsonObjectRequest.getString("author"),
                                    bookJsonObjectRequest.getString("rating"),
                                    bookJsonObjectRequest.getString("price"),
                                    bookJsonObjectRequest.getString("image")
                                )
                                bookInfoList.add(bookObject)
                                recyclerAdapter =
                                    DashboardRecyclerAdapter(activity as Context, bookInfoList)

                                recyclerDashboard.adapter = recyclerAdapter

                                recyclerDashboard.layoutManager = layoutManager


                            }
                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Some error Occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }catch (e:JSONException){
                        Toast.makeText(activity as Context,"Some error occurred!",Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener {
                    if(activity != null){
                        Toast.makeText(activity as Context, "Some error occured!",Toast.LENGTH_SHORT).show()
                        Log.d("Error","Line 116")
                    }
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "b7e158ca8c6cfb"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        }else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("No internet connection")
            dialog.setPositiveButton("Open Settings"){
                text,listener->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text,listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater?.inflate(R.menu.dashboard_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId
        if(id == R.id.action_sort){
            Collections.sort(bookInfoList,ratingComparator)
            bookInfoList.reverse()
        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }
}
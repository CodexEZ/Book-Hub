package com.example.bookhubv22.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookhubv22.ARG_PARAM1
import com.example.bookhubv22.ARG_PARAM2
import com.example.bookhubv22.R
import com.example.bookhubv22.adapter.DashboardRecyclerAdapter

class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var layoutManager: LinearLayoutManager
    val bookList = arrayListOf<String>(
        "P.S I love you",
        "The Great Gatsby",
        "Anna Karenina",
        "Madame Bovary",
        "Lolita",
        "Middlemarch",
        "The Adventures of Huckleberry Finn",
        "Moby-Dick",
        "The Lord of the Rings"
    )
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
        var view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        var recyclerDashboard = view.findViewById<RecyclerView>(R.id.recycle)
        if(recyclerDashboard == null){
            println("recycler is null")
        }
        layoutManager = LinearLayoutManager(activity)

        recyclerAdapter = DashboardRecyclerAdapter(activity as Context,bookList)

        recyclerDashboard.adapter = recyclerAdapter

        recyclerDashboard.layoutManager = layoutManager

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
}
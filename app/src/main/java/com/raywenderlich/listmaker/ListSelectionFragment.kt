package com.raywenderlich.listmaker

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ListSelectionFragment : Fragment(), ListSelectionRecyclerViewAdapter.ListSelectionRecyclerViewClickListener {

    lateinit var listsRecyclerView: RecyclerView

    lateinit var listDataManager: ListDataManager

    private var listener: OnListItemFragmentInteractionListener? = null

    override fun onAttach(context: Context){
        super.onAttach(context)
        if(context is OnListItemFragmentInteractionListener){
            listener = context
            listDataManager = ListDataManager(context)
        } else {
            throw RuntimeException("$context must implement onListItemFragmentInteractionListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_selection, container, false)
    }

    override fun onDetach(){
        super.onDetach()
        listener = null
    }

    interface OnListItemFragmentInteractionListener{
        fun onListItemClicked(list: TaskList)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val lists = listDataManager.readLists()
        view?.let {
            listsRecyclerView = it.findViewById<RecyclerView>(R.id.list_items_recyclerview)
            listsRecyclerView.layoutManager = LinearLayoutManager(activity)
            listsRecyclerView.adapter = ListSelectionRecyclerViewAdapter(lists, this)
        }
    }

    override fun listItemClicked(list: TaskList) {
        listener?.onListItemClicked(list)
    }

    fun addList(list : TaskList){
        listDataManager.saveList(list)

        val recyclerAdapter = listsRecyclerView.adapter as ListSelectionRecyclerViewAdapter
        recyclerAdapter.addList(list)
    }

    fun saveList(list: TaskList){
        listDataManager.saveList(list)
        updateLists()

    }

    private fun updateLists(){
        val lists = listDataManager.readLists()
        listsRecyclerView.adapter = ListSelectionRecyclerViewAdapter(lists, this)
    }

    companion object {
        fun newInstance(): ListSelectionFragment{
            return ListSelectionFragment()
        }
    }
}
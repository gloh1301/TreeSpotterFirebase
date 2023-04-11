package com.example.android.treespotterfirebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TreeListFragment : Fragment() {

    private val treeViewModel: TreeViewModel by lazy {
        ViewModelProvider(requireActivity()).get(TreeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val recyclerView = inflater.inflate(R.layout.fragment_tree_list, container, false)
        if (recyclerView !is RecyclerView) { throw RuntimeException("TreeListFragment view should be recycler view") }

        val trees = listOf<Tree>()
        val adapter = TreeRecyclerViewAdapter(trees) { tree, isFavorite ->
            treeViewModel.setIsFavorite(tree, isFavorite)
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        treeViewModel.latestTress.observe(requireActivity()){ treeList ->
            adapter.trees = treeList
            adapter.notifyDataSetChanged()
        }

        return recyclerView
    }

    companion object {
        @JvmStatic
        fun newInstance() = TreeListFragment()
    }
}
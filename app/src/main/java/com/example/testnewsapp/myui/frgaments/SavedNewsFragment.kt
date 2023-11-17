package com.example.testnewsapp.ui.frgaments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testnewsapp.R
import com.example.testnewsapp.adapters.NewsAdapter
import com.example.testnewsapp.databinding.FragmentBreakingNewsBinding
import com.example.testnewsapp.databinding.FragmentSavedNewsBinding
import com.example.testnewsapp.myui.NewsActivity
import com.example.testnewsapp.myui.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment:Fragment() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentSavedNewsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=(activity as NewsActivity).viewModel
        setUpNewsRecyclerView()
        val itemTouchHelperCallback =object :ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               val postion = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[postion]
                viewModel.delelwArticel(article)
                Snackbar.make(binding.root,"Deleted Sucssefully", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvSavedNews)
        newsAdapter.setOnItemClickLisnere { articale->
            val bundle =Bundle().apply {
                putSerializable("article",articale)
            }
            findNavController().navigate(R.id.action_savedNewsFragment_to_articalNewsFragment,bundle)

        }
        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer {articels->
            newsAdapter.differ.submitList(articels)
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun setUpNewsRecyclerView(){
        newsAdapter= NewsAdapter()
        binding.rvSavedNews.apply {
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)

        }
    }
}
package com.example.testnewsapp.ui.frgaments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testnewsapp.R
import com.example.testnewsapp.adapters.NewsAdapter
import com.example.testnewsapp.databinding.FragmentSearchNewsBinding
import com.example.testnewsapp.myui.NewsActivity
import com.example.testnewsapp.myui.viewmodel.NewsViewModel
import com.example.testnewsapp.utilies.Constans
import com.example.testnewsapp.utilies.Constans.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.testnewsapp.utilies.Resources
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//package:com.example.testnewsapp
class SearchNewsFragment:Fragment() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var binding:FragmentSearchNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=(activity as NewsActivity).viewModel
        setUpNewsRecyclerView()
        newsAdapter.setOnItemClickLisnere { articale->
            val bundle =Bundle().apply {
                putSerializable("article",articale)
            }
            findNavController().navigate(R.id.action_searchNewsFragment_to_articalNewsFragment,bundle)

        }
        var j:Job?=null
        binding.etSearch.addTextChangedListener {editable->
            j?.cancel()
            j= MainScope().launch {
               delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }
        viewModel.SearchNewsLiveData.observe(viewLifecycleOwner, Observer {respons->
            when(respons){
                is Resources.Success->{
                    hideprogressbar()
                    respons.data?.let {newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        var totalpage=newsResponse.totalResults / Constans.QUARY_PAGE_SIZE +2
                        isLastpage =viewModel.SearchNewsPage== totalpage
                        Log.d("ss",newsResponse.articles.toString())
                        if(isLastpage){
                            binding.rvSearchNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resources.Error -> {
                    hideprogressbar()
                    respons.message?.let {
                        Log.d("benz",it)

                    }
                }
                is Resources.Loading -> {
                    showprogressbar()

                }

                else -> {}
            }

        })
    }
    private fun hideprogressbar() {
        binding.paginationProgressBar.visibility=View.INVISIBLE
        isLoadind=false
    }
    private fun showprogressbar() {
        binding.paginationProgressBar.visibility=View.VISIBLE
        isLoadind=true
    }
    var isLoadind=false
    var isLastpage=false
    var isScrolling=false
    val scrollListener= object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val fristVisibleItemPosition=layoutManager.findFirstVisibleItemPosition()
            val visibleItemPosition= layoutManager.childCount
            val totalItemCount= layoutManager.itemCount
            val isNotLoadingAndIsNotLastPage=!isLoadind and !isLastpage
            val isAtLastItem= fristVisibleItemPosition+visibleItemPosition>=totalItemCount
            val isNotAtBeginning =fristVisibleItemPosition>=0
            val isTotalmoreThanVisible = totalItemCount >= Constans.QUARY_PAGE_SIZE
            val shouldPagenaite = isNotLoadingAndIsNotLastPage and isAtLastItem and isNotAtBeginning and isScrolling and isTotalmoreThanVisible
            if(shouldPagenaite){
                viewModel.searchNews(binding.etSearch.toString())
                isScrolling=false
            }
        }
    }
    private fun setUpNewsRecyclerView(){
        newsAdapter= NewsAdapter()
        binding.rvSearchNews.apply {
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scrollListener)

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }
}
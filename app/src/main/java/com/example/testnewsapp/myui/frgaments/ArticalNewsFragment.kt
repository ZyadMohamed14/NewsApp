package com.example.testnewsapp.ui.frgaments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import com.example.testnewsapp.R
import com.example.testnewsapp.databinding.FragmentArticleBinding
import com.example.testnewsapp.databinding.FragmentBreakingNewsBinding
import com.example.testnewsapp.databinding.FragmentSearchNewsBinding
import com.example.testnewsapp.models.Article
import com.example.testnewsapp.myui.NewsActivity
import com.example.testnewsapp.myui.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticalNewsFragment:Fragment() {
    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: FragmentArticleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=(activity as NewsActivity).viewModel
        // Retrieve the argument bundle
        val args = arguments
        // Check if the argument bundle is not null
        if (args != null) {
            // Retrieve the "Article" object from the arguments
            val article = args.getSerializable("Article") as Article

            if (article != null) {
                // Now you can use the "article" object in your fragment
                // You can access other properties of the "article" object as needed
                binding.webView.apply {
                    webViewClient = WebViewClient()
                    loadUrl(article.url!!)

                }
                binding.fab.setOnClickListener {
                    viewModel.saveArticle(article =article )
                    Snackbar.make(binding.root,"Saved Sucssefully",Snackbar.LENGTH_SHORT).show()
                }
            }
        }



    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }
}
package com.example.testnewsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testnewsapp.R
import com.example.testnewsapp.databinding.ItemArticlePreviewBinding
import com.example.testnewsapp.models.Article

class NewsAdapter :RecyclerView.Adapter<NewsAdapter.ArticaleViewHolder>() {

    inner class ArticaleViewHolder(itemview:ItemArticlePreviewBinding):RecyclerView.ViewHolder(itemview.root){
        val binding:ItemArticlePreviewBinding= itemview

    }

    private val differcallback=object :DiffUtil.ItemCallback<Article>(){

        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url==newItem.url
        }


        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
          return oldItem==newItem
        }

    }
    val differ=AsyncListDiffer(this,differcallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticaleViewHolder {
        val view = ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticaleViewHolder(view)
    }


    override fun getItemCount(): Int=differ.currentList.size


    override fun onBindViewHolder(holder: ArticaleViewHolder, position: Int) {
        val artical=differ.currentList[position]

        // Access the Context using requireContext()
        val context = holder.binding.root.context
        Glide.with(context).load(artical.urlToImage).into(holder.binding.ivArticleImage)
        holder.binding.apply {
            tvDescription.text =artical.description
            tvPublishedAt.text=artical.publishedAt
            tvTitle.text=artical.title
            tvSource.text=artical.source?.name



           root.setOnClickListener {
                onitemClickListener?.let { it(artical) }
            }



        }

    }
    private var onitemClickListener:((Article)-> Unit)?=null
    fun setOnItemClickLisnere(listener:((Article)-> Unit)){
        onitemClickListener=listener
    }
    }

/*
in java
private OnItemClickListener onItemClickListener;

interface OnItemClickListener {
    void onItemClick(Article article);
}
onItemClickListener = (article) -> {
    // Code to handle the item click event
};
in kotlin
private var onitemClickListener:((Article)-> Unit)?=null
 */






























/*
inner class ArticaleViewHolder(private val binding: ItemArticlePreviewBinding):RecyclerView.ViewHolder(binding.root){
         val IAPB=binding
        fun bindTv(articale: Article) {
            IAPB.tvDescription.text =articale.description
            IAPB.tvPublishedAt.text=articale.publishedAt
            IAPB.tvTitle.text=articale.title
            IAPB.tvSource.text=articale.source.name
           //

        }

    }
 */ /*
        val IAPB=binding
       fun bindTv(articale: Article) {
            IAPB.tvDescription.text =articale.description
            IAPB.tvPublishedAt.text=articale.publishedAt
            IAPB.tvTitle.text=articale.title
            IAPB.tvSource.text=articale.source.name
           //

        }

         */
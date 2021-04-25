package com.zj.hiapp.biz.detail

import android.os.Bundle
import com.zj.common.ui.component.HiBaseFragment
import com.zj.hiapp.R
import com.zj.hiapp.databinding.FragmentArticleListBinding

class ArticleListFragment : HiBaseFragment<FragmentArticleListBinding>() {

    override fun getLayoutId() = R.layout.fragment_article_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ArticleListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

}
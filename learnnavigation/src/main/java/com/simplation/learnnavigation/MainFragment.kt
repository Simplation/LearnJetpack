package com.simplation.learnnavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.simplation.learnnavigation.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var mViewBinding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewBinding = FragmentMainBinding.inflate(inflater, container, false)

        mViewBinding.btnToSecondFragment.setOnClickListener {
            // 使用 bundle 的方式传递参数
            val bundle = bundleOf("user_name" to "simplation", "age" to 18)

            // 跳转方法 1
            Navigation.findNavController(it).navigate(R.id.action_mainFragment_to_secondFragment, bundle)

            // 跳转方法 2
            // Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_secondFragment)
        }


        return mViewBinding.root
    }
}
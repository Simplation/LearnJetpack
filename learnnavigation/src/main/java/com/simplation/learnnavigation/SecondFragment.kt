package com.simplation.learnnavigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.simplation.learnnavigation.databinding.FragmentSecondBinding


class SecondFragment : Fragment() {

    private lateinit var mViewBinding: FragmentSecondBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewBinding = FragmentSecondBinding.inflate(layoutInflater)
        val bundle = arguments
        if (bundle != null) {
            val user_name = bundle.getString("user_name")
            val age = bundle.getInt("age")

            mViewBinding.tvContent.text = "$user_name----$age"
        }
        return mViewBinding.root
    }
}
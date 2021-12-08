package com.simplation.learnviewpager

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.simplation.learnviewpager.databinding.FragmentTranslateBinding
import java.util.*

// 移动
class TranslateFragment : Fragment() {
    private lateinit var translateBinding: FragmentTranslateBinding
    private lateinit var translateViewModel: TranslateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        translateBinding = FragmentTranslateBinding.inflate(layoutInflater, container, false)
        return translateBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        translateViewModel = ViewModelProvider(this)[TranslateViewModel::class.java]
        translateBinding.imageViewTranslate.translationX = translateViewModel.translateValue

        val objectAnimator =
            ObjectAnimator.ofFloat(translateBinding.imageViewTranslate, "x", 0F, 0F)
        objectAnimator.duration = 500

        translateBinding.imageViewTranslate.setOnClickListener {
            if (!objectAnimator.isRunning) {
                val dx = if (Random().nextBoolean()) 100 else -100
                objectAnimator.setFloatValues(
                    translateBinding.imageViewTranslate.x,
                    translateBinding.imageViewTranslate.x + dx
                )
                translateViewModel.translateValue += dx
                objectAnimator.start()
            }
        }
    }

}
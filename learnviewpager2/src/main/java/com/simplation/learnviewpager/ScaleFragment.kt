package com.simplation.learnviewpager

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.simplation.learnviewpager.databinding.FragmentScaleBinding

// 缩放
class ScaleFragment : Fragment() {
    private lateinit var scaleBinding: FragmentScaleBinding
    private lateinit var scaleViewModel: ScaleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layo ut for this fragment
        scaleBinding = FragmentScaleBinding.inflate(inflater, container, false)
        return scaleBinding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        scaleViewModel = ViewModelProvider(this)[ScaleViewModel::class.java]

        scaleBinding.imageViewScale.scaleY = scaleViewModel.scaleValue
        scaleBinding.imageViewScale.scaleX = scaleViewModel.scaleValue

        val objectAnimatorX = ObjectAnimator.ofFloat(scaleBinding.imageViewScale, "scaleX", 0F, 0F)
        val objectAnimatorY = ObjectAnimator.ofFloat(scaleBinding.imageViewScale, "scaleY", 0F, 0F)
        objectAnimatorX.duration = 500
        objectAnimatorY.duration = 500
        scaleBinding.imageViewScale.setOnClickListener {
            if (!objectAnimatorX.isRunning) {
                objectAnimatorX.setFloatValues(scaleBinding.imageViewScale.scaleX + 0.1F)
                objectAnimatorY.setFloatValues(scaleBinding.imageViewScale.scaleY + 0.1F)
                scaleViewModel.scaleValue += 0.1F
                objectAnimatorX.start()
                objectAnimatorY.start()
            }
        }
    }

}
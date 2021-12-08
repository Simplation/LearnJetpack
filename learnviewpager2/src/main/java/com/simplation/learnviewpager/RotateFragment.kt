package com.simplation.learnviewpager

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.simplation.learnviewpager.databinding.FragmentRotateBinding

// 旋转
class RotateFragment : Fragment() {
    private lateinit var rotateBinding: FragmentRotateBinding
    private lateinit var rotateViewModel: RotateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        rotateBinding = FragmentRotateBinding.inflate(layoutInflater, container, false)
        return rotateBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rotateViewModel = ViewModelProvider(this)[RotateViewModel::class.java]
        rotateBinding.imageViewRotate.rotation = rotateViewModel.rotateValue

        val objectAnimator =
            ObjectAnimator.ofFloat(rotateBinding.imageViewRotate, "rotation", 0F, 0F)
        objectAnimator.duration = 500

        rotateBinding.imageViewRotate.setOnClickListener {
            if (!objectAnimator.isRunning) {
                objectAnimator.setFloatValues(
                    rotateBinding.imageViewRotate.rotation,
                    rotateBinding.imageViewRotate.rotation + 100
                )
                rotateViewModel.rotateValue += 100
                objectAnimator.start()
            }
        }
    }

}
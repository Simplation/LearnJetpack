package com.simplation.learnlifecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.simplation.learnlifecycle.databinding.ActivityMainBinding

/**
 * Fragment 也默认实现了 LifecycleOwner 接口
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mViewBinding:ActivityMainBinding
    private lateinit var myLocationListener: MyLocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)

        myLocationListener = MyLocationListener(this, object : MyLocationListener.OnLocationChangedListener {
            override fun changed(latitude: Double, longitude: Double) {
                Toast.makeText(this@MainActivity, "展示获取到的位置信息", Toast.LENGTH_SHORT).show()
            }
        })

        // 进行绑定操作
        lifecycle.addObserver(myLocationListener)

        // StartBtn
        mViewBinding.btnStartService.setOnClickListener {
            val intent = Intent(this@MainActivity, MyService::class.java)
            startService(intent)
        }

        // StopBtn
        mViewBinding.btnStopService.setOnClickListener {
            val intent = Intent(this@MainActivity, MyService::class.java)
            stopService(intent)
        }

    }
}
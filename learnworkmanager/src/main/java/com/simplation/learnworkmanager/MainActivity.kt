package com.simplation.learnworkmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.*
import com.simplation.learnworkmanager.databinding.ActivityMainBinding
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .build()

        // 传递数据
        // 1.构建传递数据
        val data =
            Data.Builder().putString("input_string", "Hello World！").putInt("input_int", 18).build()

        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(UploadLogWorker::class.java)
            .setConstraints(constraints)     // 设置触发条件
            .setInitialDelay(10, TimeUnit.SECONDS) // 符合条件后，延迟 10s 执行
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                TimeUnit.MILLISECONDS
            )  // 设置指数退避算法
            .addTag("UploadTag")
            .setInputData(data)  // 传递数据，将构建的数据塞进去
            .build()

        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)


        // 取消任务
        WorkManager.getInstance(this).cancelAllWork()


        // WorkManager 通过 LiveData 获取 Worker 返回的数据
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(UUID.fromString("UUID"))
            .observe(this, {
                fun onChange(workInfo: WorkInfo) {
                    if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                        val output = workInfo.outputData.getString("output_data")
                    }
                }
            })

    }
}

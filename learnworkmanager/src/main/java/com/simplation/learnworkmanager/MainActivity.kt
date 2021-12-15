package com.simplation.learnworkmanager

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.*
import com.simplation.learnworkmanager.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var mBinding: ActivityMainBinding
    private val workManager = WorkManager.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val sp = getSharedPreferences(SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE)
        sp.registerOnSharedPreferenceChangeListener(this)  // 注册事件

        updataView()

        mBinding.button.setOnClickListener {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .build()

            // 传递数据
            // 1.构建传递数据
            // val data = Data.Builder().putString(INPUT_STRING_KEY, WORK_A_NAME).putInt(INPUT_INT_KEY, 18).build()

            // workDataOf() 使用 work-runtime-ktx 库里的方法
            val oneTimeWorkRequestA = createWork(constraints, WORK_A_NAME)
            val oneTimeWorkRequestB = createWork(constraints, WORK_B_NAME)

            // 将 request 添加队列
            // workManager.enqueue(oneTimeWorkRequest)

            // 如果存在多个 request，则需要依次添加进去
            workManager.beginWith(oneTimeWorkRequestA).then(oneTimeWorkRequestB).enqueue()

            // 取消任务
            // workManager.cancelAllWork()

            // WorkManager 通过 LiveData 获取 Worker 返回的数据
            /*workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
                .observe(this,
                    {
                        if (it.state == WorkInfo.State.SUCCEEDED) {
                            val output = it.outputData.getString(RETURN_KEY)

                            Log.d(WORK_MANAGER_TAG, "onCreate: $output")
                        }
                    })*/
        }
    }

    private fun createWork(
        constraints: Constraints,
        name: String
    ): OneTimeWorkRequest {
        return OneTimeWorkRequest.Builder(UploadLogWorker::class.java)
            .setConstraints(constraints)     // 设置触发条件
            //.setInitialDelay(10, TimeUnit.SECONDS) // 符合条件后，延迟 10s 执行
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                TimeUnit.MILLISECONDS
            )  // 设置指数退避算法
            .addTag("UploadTag")
            .setInputData(workDataOf(INPUT_INT_KEY to name))  // 传递数据，将构建的数据塞进去
            .build()
    }

    private fun updataView() {
        val sp = getSharedPreferences(SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE)
        val a = sp.getInt(RETURN_KEY, 0).toString()
        val b = sp.getInt(RETURN_KEY, 0).toString()
        mBinding.textViewA.text = a
        mBinding.textViewB.text = b
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        updataView()
    }
}

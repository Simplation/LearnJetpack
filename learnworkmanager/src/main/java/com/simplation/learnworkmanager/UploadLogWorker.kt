package com.simplation.learnworkmanager

import android.content.Context
import androidx.work.Data
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters

class UploadLogWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    //region 耗时任务在 doWork() 方法中执行
    /**
     * 执行成功：Result.success()
     * 执行失败：Result.failure()
     * 重新执行：Result.retry()
     */
    override fun doWork(): Result {
        // 取出传递的数据
        val stringData = inputData.getString("input_string")
        val intData = inputData.getInt("input_int", 0)

        // 任务执行完成后返回数据
        val outputData = Data.Builder().putString("output_data", "Task Success").build()


        return Result.success(outputData)
    }
    //endregion
}
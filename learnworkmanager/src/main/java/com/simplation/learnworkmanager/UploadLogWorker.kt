package com.simplation.learnworkmanager

import android.content.Context
import android.util.Log
import androidx.work.*

class UploadLogWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    //region 耗时任务在 doWork() 方法中执行
    /**
     * 执行成功：Result.success()
     * 执行失败：Result.failure()
     * 重新执行：Result.retry()
     */
    override fun doWork(): Result {
        /*Log.d(WORK_MANAGER_TAG, "doWork: started.")
        Thread.sleep(3000)
        Log.d(WORK_MANAGER_TAG, "doWork: finished.")

        // 取出传递的数据
        val stringData = inputData.getString(INPUT_STRING_KEY)
        val intData = inputData.getInt(INPUT_INT_KEY, 0)

        Log.d(WORK_MANAGER_TAG, "doWork: $stringData - $intData")

        // 任务执行完成后返回数据
        val returnData = workDataOf(RETURN_KEY to "$stringData - $intData")
        return Result.success(returnData)*/

        val name = inputData.getString(INPUT_INT_KEY)
        Thread.sleep(3000)
        val sp =
            applicationContext.getSharedPreferences(SHAREDPREFERENCES_KEY, Context.MODE_PRIVATE)
        var number = sp.getInt(name, 0)
        Log.d(WORK_MANAGER_TAG, "doWork: $number")
        sp.edit().putInt(name, ++number).apply()
        return Result.success(workDataOf(RETURN_KEY to "$name out put"))
    }
    //endregion
}
package com.danieldisu.kollectionview.internal

import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import java.util.concurrent.Executor

internal class UiThreadExecutor : Executor {
    private val mHandler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
        mHandler.post(command)
    }
}

internal fun <T> getDiffConfig(callback: DiffUtil.ItemCallback<T>, diffInMainThread: Boolean): AsyncDifferConfig<T> {
    return if (diffInMainThread) {
        AsyncDifferConfig.Builder<T>(callback)
            .setBackgroundThreadExecutor(UiThreadExecutor())
            .build()
    } else {
        AsyncDifferConfig.Builder<T>(callback)
            .build()
    }
}

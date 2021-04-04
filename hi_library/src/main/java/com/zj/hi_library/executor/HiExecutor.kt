package com.zj.hi_library.executor

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.IntRange
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.Comparable as Comparable

/**
 * 自定义线程池
 * 支持按任务的优先级执行
 * 支持线程池暂停
 * 异步结果回调主线程
 */
object HiExecutor {

    private const val TAG = "HiExecutor"

    private var hiExecutor: ThreadPoolExecutor
    private var lock: ReentrantLock
    private var pauseCondition: Condition
    private var isPaused: Boolean = false
    val minaHandler = Handler(Looper.getMainLooper())

    init {
        val cpuCount = Runtime.getRuntime().availableProcessors()
        val corePoolSize = cpuCount + 1
        val maxPoolSize = cpuCount * 2 + 1
        val blockingQueue: PriorityBlockingQueue<out Runnable> = PriorityBlockingQueue()
        val keepAliveTime = 30L
        val unit = TimeUnit.SECONDS

        val seq = AtomicLong()
        val threadFactory = ThreadFactory {
            val thread = Thread(it)
            thread.name = "hi-executor-" + seq.getAndIncrement()
            return@ThreadFactory thread
        }


        lock = ReentrantLock()
        pauseCondition = lock.newCondition()

        hiExecutor = object : ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            keepAliveTime,
            unit,
            blockingQueue as BlockingQueue<Runnable>,
            threadFactory
        ) {
            override fun beforeExecute(t: Thread?, r: Runnable?) {
                super.beforeExecute(t, r)
                if (isPaused) {
                    lock.lock()
                    try {
                        pauseCondition.await()
                    } finally {
                        lock.unlock()
                    }
                }
            }

            override fun afterExecute(r: Runnable?, t: Throwable?) {
                super.afterExecute(r, t)
                //监控线程池耗时任务、线程创建数量、正在运行的数量
                Log.e(TAG, "已执行完任务的优先级是" + (r as PriorityRunnable).priority)
            }
        }
    }

    @JvmOverloads
    fun execute(@IntRange(from = 0, to = 10) priority: Int = 0, runnable: Runnable) {
        hiExecutor.execute(PriorityRunnable(priority, runnable))
    }

    fun execute(@IntRange(from = 0, to = 10) priority: Int = 0, runnable: Callable<*>) {
        hiExecutor.execute(PriorityRunnable(priority, runnable))
    }

    @Synchronized
    fun pause() {
        isPaused = true
        Log.e(TAG, "线程池暂停")
    }

    @Synchronized
    fun resume() {
        isPaused = false
        Log.e(TAG, "线程池恢复")
        lock.lock()
        try {
            pauseCondition.signalAll()
        } finally {
            lock.unlock()
        }
    }

    abstract class Callable<T> : Runnable {
        override fun run() {
            minaHandler.post { onPrepare() }
            val t = onBackground()
            minaHandler.post { onCompleted(t) }
        }

        open fun onPrepare() {}

        abstract fun onBackground(): T

        abstract fun onCompleted(t: T)
    }

    class PriorityRunnable(val priority: Int, private val runnable: Runnable) : Runnable,
        Comparable<PriorityRunnable> {
        override fun run() {
            runnable.run()
        }

        override fun compareTo(other: PriorityRunnable): Int {
            return if (this.priority < other.priority) 1 else if (this.priority > other.priority) -1 else 0
        }
    }

}
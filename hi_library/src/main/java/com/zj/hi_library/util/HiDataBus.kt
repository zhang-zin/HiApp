package com.zj.hi_library.util

import androidx.lifecycle.*
import java.util.concurrent.ConcurrentHashMap

/**
 * 事件总线，可以控制是否发送黏性事件
 * 并且拥有当宿主生命周期销毁时反注册的能力
 */
object HiDataBus {

    private val eventMap = ConcurrentHashMap<String, StickLiveData<*>>()

    /**
     * 基于事件名称 订阅、分发消息
     * 由于一个LiveData只能发送一种数据类型
     * 所以不同的event事件，需要使用不同的LiveData实例去分发
     *
     */
    fun <T> with(eventName: String): StickLiveData<T> {
        var stickLiveData = eventMap[eventName]
        if (stickLiveData == null) {
            stickLiveData = StickLiveData<T>(eventName)
            eventMap[eventName] = stickLiveData
        }
        return stickLiveData as StickLiveData<T>
    }

    class StickLiveData<T>(private val eventName: String) : LiveData<T>() {

        var mStickValue: T? = null
        var mVersion: Int = 0

        /**
         * 主线程发送数据
         */
        fun setStickValue(value: T) {
            setValue(value)
        }

        /**
         * 子线程发送数据
         */
        fun postStickValue(value: T) {
            postValue(value)
        }

        /**
         * 允许指定注册的观察者，是否需要关系黏性事件
         * [stick] stick=true 如果之前存在已经发送的数据，这个observer会收到之前的黏性事件
         */
        fun stickObserve(owner: LifecycleOwner, stick: Boolean, observer: Observer<in T>) {

            owner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    // 监听数组 发生销毁事件，主动把LiveData移除
                    eventMap.remove(eventName)
                }
            })
            super.observe(owner, StickObserver(this, stick, observer))
        }

        override fun setValue(value: T) {
            mVersion++
            mStickValue = value
            super.setValue(value)
        }

        override fun postValue(value: T) {
            mVersion++
            mStickValue = value
            super.postValue(value)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            stickObserve(owner, false, observer)
        }
    }

    class StickObserver<T>(
        private val stickLiveData: StickLiveData<T>,
        private val stick: Boolean,
        private val observer: Observer<in T>
    ) : Observer<T> {

        private var lastVersion = stickLiveData.mVersion

        override fun onChanged(t: T) {
            if (lastVersion >= stickLiveData.mVersion) {
                // StickLiveData没有更新的数据需要发送
                if (stick && stickLiveData.mStickValue != null) {
                    observer.onChanged(stickLiveData.mStickValue)
                }
                return
            }
            lastVersion = stickLiveData.mVersion
            observer.onChanged(t)
        }
    }
}
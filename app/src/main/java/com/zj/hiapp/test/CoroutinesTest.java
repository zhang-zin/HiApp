package com.zj.hiapp.test;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlinx.coroutines.DelayKt;

/**
 * 协程挂起和等待Java还原
 *
 * @author 张锦
 */
public class CoroutinesTest {

    public static final String TAG = "zhang";

    public static Object request1(Continuation preContinuation) {

        ContinuationImpl requestCallback;

        if (!((preContinuation instanceof ContinuationImpl))
                || (((ContinuationImpl) preContinuation).label & Integer.MIN_VALUE) == 0) {
            requestCallback = new ContinuationImpl(preContinuation) {
                @Override
                Object invokeSuspend(Object resumeResult) {
                    this.result = resumeResult;
                    this.label |= Integer.MIN_VALUE;
                    Log.e(TAG, "request1 has resumed");
                    return request1(this);
                }
            };
        } else {
            requestCallback = (ContinuationImpl<String>) preContinuation;
        }

        switch (requestCallback.label) {
            case 0:
                Object request2 = request2(requestCallback);
                if (request2 == IntrinsicsKt.getCOROUTINE_SUSPENDED()){
                    Log.e(TAG, "request1 has suspended");
                    return IntrinsicsKt.getCOROUTINE_SUSPENDED();
                }
                break;
            default:
                break;
        }

        Log.e(TAG, "request1 complete");
        return "result from request1" + requestCallback.result;
    }

    public static Object request2(Continuation preContinuation) {

        ContinuationImpl requestCallback;

        if (!((preContinuation instanceof ContinuationImpl))
                || (((ContinuationImpl) preContinuation).label & Integer.MIN_VALUE) == 0) {
            requestCallback = new ContinuationImpl(preContinuation) {
                @Override
                Object invokeSuspend(Object resumeResult) {
                    this.result = resumeResult;
                    this.label |= Integer.MIN_VALUE;
                    Log.e(TAG, "request2 has resumed");
                    return request2(this);
                }
            };
        } else {
            requestCallback = (ContinuationImpl<String>) preContinuation;
        }

        switch (requestCallback.label) {
            case 0:
                Object delay = DelayKt.delay(2 * 1000, requestCallback);
                if (delay == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                    Log.e(TAG, "request2 has suspended");
                    return IntrinsicsKt.getCOROUTINE_SUSPENDED();
                }
                break;
            default:
                break;
        }

        Log.e(TAG, "request2 complete");
        return "result from request2";
    }

    abstract static class ContinuationImpl<T> implements Continuation<T> {

        private final Continuation preCallback;
        public int label;
        public Object result;

        ContinuationImpl(Continuation preCallback) {
            this.preCallback = preCallback;
        }

        @NotNull
        @Override
        public CoroutineContext getContext() {
            return preCallback.getContext();
        }

        @Override
        public void resumeWith(@NotNull Object resumeResult) {
            Object suspend = invokeSuspend(resumeResult);
            if (suspend == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                return;
            }

            preCallback.resumeWith(suspend);
        }

        /**
         * 唤起方法回调
         *
         * @param resumeResult 唤醒执行结果
         * @return 执行结果
         */
        abstract Object invokeSuspend(Object resumeResult);
    }
}

package com.zj.hiapp.biz.detail

import androidx.lifecycle.*
import com.zj.hi_library.restful.serverData
import com.zj.hiapp.http.ApiFactory
import com.zj.hiapp.http.api.GoodsApi
import com.zj.hiapp.http.model.GoodDetailModel
import com.zj.hiapp.http.model.GoodsList
import kotlinx.coroutines.launch

class GoodDetailViewModel() : ViewModel() {

    companion object {
        private class DetailViewModelFactory(val goodsSign: String) :
            ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                try {
                    return modelClass.getConstructor(String::class.java).newInstance(goodsSign)
                } catch (e: Exception) {
                    //ignore
                }

                return super.create(modelClass)
            }
        }

        fun get(viewModelStoreOwner: ViewModelStoreOwner, goodsSign: String): GoodDetailViewModel {
            return ViewModelProvider(viewModelStoreOwner, DetailViewModelFactory(goodsSign))
                .get(GoodDetailViewModel::class.java)
        }

        val apiFactory = ApiFactory.create(GoodsApi::class.java)
    }

    val goodsListModel = MutableLiveData<GoodsList>()

    fun getGoodsDetail(goodsSign: String): LiveData<GoodDetailModel> {
        val liveData = MutableLiveData<GoodDetailModel>()
        viewModelScope.launch {
            val serverData = apiFactory.getGoodsDetail().serverData()
            val goodsList = apiFactory.getGoodsRecommend().serverData()
            goodsListModel.value = goodsList.data
            val goodDetailModel = serverData.data
            goodDetailModel?.run {
                liveData.postValue(this)
            }
        }
        return liveData
    }

}
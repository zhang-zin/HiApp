package com.zj.hiapp.http.model

import java.io.Serializable

/**
 * 体系分类
 */
/*{
    "children":[
    {
        "children":[

        ],
        "courseId":13,
        "id":60,
        "name":"Android Studio相关",
        "order":1000,
        "parentChapterId":150,
        "userControlSetTop":false,
        "visible":1
    }
    ],
    "courseId":13,
    "id":150,
    "name":"开发环境",
    "order":1,
    "parentChapterId":0,
    "userControlSetTop":false,
    "visible":1
}*/
data class CategoryModel(
    val children: List<ChildCategory>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
) : Serializable

data class ChildCategory(
    val children: List<Any>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
) : Serializable
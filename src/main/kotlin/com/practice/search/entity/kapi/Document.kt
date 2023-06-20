package com.practice.search.entity.kapi

import com.practice.search.entity.napi.Item

data class Document(
    val title: String = "",
    val contents: String = "",
    val url: String = "",
    val blogname: String = "",
    val thumbnail: String = "",
    val datetime: String = ""
)
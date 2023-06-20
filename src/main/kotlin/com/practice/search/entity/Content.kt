package com.practice.search.entity

import com.practice.search.entity.kapi.Document
import com.practice.search.entity.napi.Item

data class Content(
    val title: String = "",
    val contents: String = "",
    val url: String = "",
    val blogname: String = "",
    val thumbnail: String = "",
    val datetime: String = ""
) {
    companion object {
        fun of(document: Document) = Content(
            title = document.title,
            contents = document.contents,
            url = document.url,
            blogname = document.blogname,
            thumbnail = document.thumbnail,
            datetime = document.datetime
        )

        fun of(item: Item) = Content(
            title = item.title,
            contents = item.description,
            url = item.bloggerlink,
            blogname = item.bloggername,
            thumbnail = item.link,
            datetime = item.postdate
        )
    }
}
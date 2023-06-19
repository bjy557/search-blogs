package com.practice.search.app.entity.search

import com.practice.search.app.entity.search.alternative.Item

data class Document(
    val title: String = "",
    val contents: String = "",
    val url: String = "",
    val blogname: String = "",
    val thumbnail: String = "",
    val datetime: String = ""
) {
    companion object {
        fun of(item: Item) = Document(
            title = item.title,
            contents = item.description,
            url = item.bloggerlink,
            blogname = item.bloggername,
            thumbnail = item.link,
            datetime = item.postdate
        )
    }
}
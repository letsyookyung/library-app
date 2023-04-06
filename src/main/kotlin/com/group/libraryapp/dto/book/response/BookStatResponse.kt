package com.group.libraryapp.dto.book.response

import com.group.libraryapp.domain.book.BookType

data class BookStatResponse(
    val type: BookType,
    val count: Long, // 이것도 val로 유지할 수 있요
) {
//    fun plusOne() {
//        count++
//    }
}

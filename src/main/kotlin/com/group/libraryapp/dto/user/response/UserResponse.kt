package com.group.libraryapp.dto.user.response

import com.group.libraryapp.domain.user.User

// 인텔리제이가 java -> 코틀린 해줌
//class UserResponse(user: User) {
//    val id: Long
//    val name: String
//    val age: Int?
//
//    init {
//        id = user.id!!
//        name = user.name
//        age = user.age
//    }
//}

// 더 좋은 코드
//class UserResponse(
//    val id: Long,
//    val name: String,
//    val age: Int?,
//) {
//    constructor(user: User): this(
//        id = user.id!!, // 처음엔 null이지만, 다음엔 반드시 null이 아님을
//        name = user.name,
//        age = user.age
//    )
//}

// 더 더 좋은 코드
data class UserResponse(
    val id: Long,
    val name: String,
    val age: Int?,
) {
    companion object { //정적 팩토리 method
        fun of(user: User): UserResponse {
            return UserResponse(
                id = user.id!!, // 처음엔 null이지만, 다음엔 반드시 null이 아님을
                name = user.name,
                age = user.age
            )
        }
    }
}
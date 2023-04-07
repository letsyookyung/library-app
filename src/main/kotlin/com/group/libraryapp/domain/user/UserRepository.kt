package com.group.libraryapp.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface UserRepository: JpaRepository<User, Long>, UserRepositoryCustom {
    fun findByName(name: String): User?

    // tip SQL 구문 여러 옵션 공부해보기
//    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.userLoanHistories")
//    fun findAllWithHistories(): List<User>

    // tip querydsl로 바꾸기

}
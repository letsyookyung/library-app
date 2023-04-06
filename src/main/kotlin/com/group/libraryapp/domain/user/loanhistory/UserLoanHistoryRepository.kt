package com.group.libraryapp.domain.user.loanhistory

import org.apache.catalina.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserLoanHistoryRepository: JpaRepository<UserLoanHistory, Long> {
    fun findByBookNameAndStatus(bookName: String, status: UserLoanStatus): UserLoanHistory?

    // tip 둘 중 어떤게 좋은 코드일까?
    fun findAllByStatus(status: UserLoanStatus): List<UserLoanHistory>

    fun countByStatus(status: UserLoanStatus): Long // sql의 count 쿼리를 이용할 수 있음
}
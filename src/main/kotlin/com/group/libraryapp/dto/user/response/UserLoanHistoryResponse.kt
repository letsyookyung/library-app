package com.group.libraryapp.dto.user.response

import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory

import com.group.libraryapp.domain.user.User

data class UserLoanHistoryResponse(
    val name: String, // user 이름
    val books: List<BookHistoryResponse>
) {

    companion object {
        fun of(user: User): UserLoanHistoryResponse {
            return UserLoanHistoryResponse(
                name = user.name,
                books = user.userLoanHistories.map(BookHistoryResponse::of)
            )
        }
    }
}

/*
정적 factory method:
이렇게 정적 factory method를 사용하면 클래스 생성자를 직접 호출하는 대신
create() 메서드를 호출하여 객체를 생성할 수 있습니다.
정적 factory method를 사용하여 객체 생성 로직을 클래스 내부로 캡슐화할 수 있습니다.
이를 통해 코드의 가독성과 유지 보수성을 높일 수 있습니다.
 */

data class BookHistoryResponse(
    val name: String, // 책의 이름
    val isReturn: Boolean,
) { // tip 정적 factory method 활용
    companion object {
        fun of(history: UserLoanHistory): BookHistoryResponse {
            return BookHistoryResponse(
                name = history.bookName,
                isReturn = history.isReturn
            )
        }
    }
}
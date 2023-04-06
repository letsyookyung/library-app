package com.group.libraryapp.domain.user

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import javax.persistence.*

@Entity
//class User ( 이렇게해서 User를 클릭하면 User가 사용되는 모든 곳이 나오고
class User constructor ( // tip 이렇게 constructor 지시어를 명시적으로 넣으면, 실제 constructor가 호출되는 특정 지점만을 볼 수 있음, 해당 entity가 쓰이는 곳만 딱 볼 수 있음!!!
    var name: String,

    val age: Int? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val userLoanHistories: MutableList<UserLoanHistory> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    ) {
    init {
        if (name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다.")
        }
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun loanBook(book: Book) {
        this.userLoanHistories.add(UserLoanHistory.fixture(this, book.name))
    }

    fun returnBook(bookName: String) { // 뒤의 조건이 일치하는 첫번째 원소가 걸림
        this.userLoanHistories.first { a -> a.bookName == bookName }.doReturn() // history는 userLoanHistories의 각 배열을 나타냄
    }


}
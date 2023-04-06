package com.group.libraryapp.service.book

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.lang.IllegalArgumentException


@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val bookService: BookService,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {
    @AfterEach
    fun deleteAll() {
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("책 등록이 정상 동작합니다.")
    fun saveBookTest() {
        //given
        val request = BookRequest("금요일", BookType.COMPUTER)

        //when
        bookService.saveBook(request)

        //then
        val results = bookRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("금요일")
        assertThat(results[0].type).isEqualTo(BookType.COMPUTER)

    }


    @Test
    @DisplayName("책 대출이 정상 동작합니다.")
    fun loanBookTest() {
        //given
        bookRepository.save(Book.fixture("금요일"))
        val savedUser = userRepository.save(User("이유경", 10))
        val request = BookLoanRequest("이유경", "금요일")

        //when
        bookService.loanBook(request)

        //then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].bookName).isEqualTo("금요일")
        assertThat(results[0].user.id).isEqualTo(savedUser.id)
        assertThat(results[0].status).isEqualTo(UserLoanStatus.LOANED)

    }

    @Test
    @DisplayName("책이 진작 대출되어 있다면, 신규 대출이 실패합니다.")
    fun loanBookFailTest() {
        //given
        bookRepository.save(Book.fixture("금요일"))
        val savedUser = userRepository.save(User("이유경", 10))
        userLoanHistoryRepository.save(
            UserLoanHistory.fixture(
                savedUser,
                "금요일",
            )
        )
        val request = BookLoanRequest("이유경", "금요일")

        //when & then
//        val message = assertThrows<IllegalArgumentException> {
//            bookService.loanBook(request)
//        }.message
//        assertThat(message).isEqualTo("진작 대출이 되어 있는 책입니다.")


        assertThrows<IllegalArgumentException> {
            bookService.loanBook(request)
        }.apply {
            assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")
        }

    }

    @Test
    @DisplayName("책 반납이 정상 동작합니다.")
    fun returnBookTest() {
        //given
        bookRepository.save(Book.fixture("금요일"))
        val savedUser = userRepository.save(User("이유경", 10))
        userLoanHistoryRepository.save(
            UserLoanHistory.fixture(
                savedUser,
                "금요일",
            )
        )
        val request = BookReturnRequest("이유경", "금요일")

        //when
        bookService.returnBook(request)

        //then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].status).isEqualTo(UserLoanStatus.RETURNED)
    }

}
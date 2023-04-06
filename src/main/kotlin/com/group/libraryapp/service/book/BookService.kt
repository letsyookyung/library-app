package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.book.response.BookStatResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.IllegalArgumentException

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository
) {
    @Transactional
    fun saveBook(request: BookRequest) {
        val newBook = Book(request.name, request.type)
        bookRepository.save(newBook)
    }

    @Transactional
    fun loanBook(request: BookLoanRequest) {
//        val book = bookRepository.findByName(request.bookName).orElseThrow(::IllegalArgumentException) // 아래거와 같은 것
        val book = bookRepository.findByName(request.bookName) ?: throw IllegalArgumentException("책이 없습니다") // optional을 빼버리면 이렇게
        if (userLoanHistoryRepository.findByBookNameAndStatus(request.bookName, UserLoanStatus.LOANED) != null) { // tip 바꿔서 하면 왜 안되나?
            throw IllegalArgumentException("진작 대출되어 있는 책입니다")
        }

        val user = userRepository.findByName(request.userName) ?: throw IllegalArgumentException()
        user.loanBook(book)
    }


    @Transactional
    fun returnBook(request: BookReturnRequest) {
        val user = userRepository.findByName(request.userName) ?: throw IllegalArgumentException("해당 회원 없습니다")
        user.returnBook(request.bookName)
    }

    @Transactional(readOnly = true)
    fun countLoanedBook(): Int {
//        return userLoanHistoryRepository.findAllByStatus(UserLoanStatus.LOANED).size
        return userLoanHistoryRepository.countByStatus(UserLoanStatus.LOANED).toInt()
    }

    @Transactional(readOnly = true)
    fun getBookStatistics(): List<BookStatResponse> {
        return bookRepository.getStats()

    }

//  sql 의 groupBy를 사용하는것이 더 나음
//        return bookRepository.findAll() // List<Book> 이 나옴
//            .groupBy { book -> book.type } // Map<BookType, List<Book>> 이 나옴
//            .map { (type, books) -> BookStatResponse(type, books.size) } // List<BookStatResponse>
//    } // 이렇게 하면 밑에서 쓰였던 plusOne()도 필요없음

//        tip 이것도 좋은 코드가 아님, 이유는 콜 연산자가 많아서 이해하고 유지보수하기 어려움, 그리고 mutable같이 가변리스트의 경우 실수가 생길수있음
//        val results = mutableListOf<BookStatResponse>()
//        val books = bookRepository.findAll()
//        for (book in books) {
//            results.firstOrNull { dto -> book.type == dto.type }?.plusOne()
//                ?: results.add(BookStatResponse(book.type, 1))

//            위와 같은거
//            val targetDto = results.firstOrNull { dto -> book.type == dto.type}
//            if (targetDto == null) {
//                results.add(BookStatResponse(book.type,1))
//            } else {
//                targetDto.plusOne()
//            }


}


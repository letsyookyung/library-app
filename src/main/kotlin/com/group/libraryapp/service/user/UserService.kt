package com.group.libraryapp.service.user

import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.dto.user.response.BookHistoryResponse
import com.group.libraryapp.dto.user.response.UserLoanHistoryResponse
import com.group.libraryapp.dto.user.response.UserResponse
import com.group.libraryapp.util.fail
import com.group.libraryapp.util.findByOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class UserService(
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository,
) {
    @Transactional // 기본적으로 override가 안된, CLASS에도
    fun saveUser(request: UserCreateRequest) {
        val newUser = User(request.name, request.age)
        userRepository.save(newUser)
    }

    @Transactional(readOnly = true)
    fun getUsers(): List<UserResponse> {
        return userRepository.findAll()
            .map { it -> UserResponse.of(it)} // it 이든 ,user든 각 row를 나타내는거고, UserResponse에 담아서
//            .map { user -> UserResponse(user)}
//            .map(::UserResponse) // 이것도 같은 뜻임
    }

    @Transactional
    fun updateUserName(request: UserUpdateRequest) { // orElseThrow를 하는 이유는 userRepository에서 Optional을 썼기 때문에
//        val user = userRepository.findById(request.id).orElseThrow(::IllegalArgumentException) // tip :: class의 생성자를 부를 때 이렇게 씀
//        val user = userRepository.findById(request.id)  ?: throw IllegalArgumentException()
//        val user = userRepository.findByIdOrNull(request.id) ?: fail()
        val user = userRepository.findByOrThrow(request.id)
        user.updateName(request.name)

    }

    @Transactional
    fun deleteUser(name: String) {
        val user = userRepository.findByName(name) ?: fail()
        userRepository.delete(user)
    }

    @Transactional(readOnly = true)
    fun getUserLoanHistories(): List<UserLoanHistoryResponse> {
        val users = userRepository.findAllWithHistories()
        return users.map(UserLoanHistoryResponse::of)
//        return users.map { user -> UserLoanHistoryResponse.of(user) }
//            UserLoanHistoryResponse(
//                name = user.name,
//                books = user.userLoanHistories.map(BookHistoryResponse::of)
//                            //{
//                        //history -> BookHistoryResponse.of(history) // 같은거
//                //}
//            )
        }

    @Transactional
    fun saveUserAndLoanTwoBooks() {
        val newUser = User("ㅁ", 123)
        val books = bookRepository.saveAll(listOf(
            Book("책1", BookType.COMPUTER),
            Book("책2", BookType.COMPUTER)
        ))
        books.forEach { book -> newUser.loanBook(book) }
        userRepository.save(newUser)

    }



    }

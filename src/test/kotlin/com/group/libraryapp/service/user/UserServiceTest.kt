package com.group.libraryapp.service.user

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    // UserServiceTest 안에 userRepository의존성을 주입해줘야함, 그래서 @Autowired 함
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {
    @AfterEach //db를 공유하니, 테스트가 제대로 안되서 각 기능 뒤에 다시 깨끗하게 지우자
    fun clean() {
        println("클린 시작")
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("유저 저장이 정상 동작합니다.")
    fun saveUserTest() {
        //given
        val request = UserCreateRequest("이유경",null)

        //when
        userService.saveUser(request)

        //then
        val results = userRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("이유경")
        assertThat(results[0].age).isNull()

    }

    @Test
    @DisplayName("유저 조회가 정상 동작합니다.")
    fun getUsersTest() {
        //given
        userRepository.saveAll(listOf(
            User("A", 31),
            User("B", 10)
        )
            )

        //when
        val results = userService.getUsers()

        //then
        assertThat(results).hasSize(2)
        assertThat(results).extracting("name").containsExactlyInAnyOrder("A", "B")
        assertThat(results).extracting("age").containsExactlyInAnyOrder(31, 10)

    }


    @Test
    @DisplayName("유저 업데이트가 정상 동작합니다.")
    fun updateUserNameTest() {
        //given
        val savedUser = userRepository.save(User("A", 10))
        val request = UserUpdateRequest(savedUser.id!!, "B") // !! 연산자는 변수가 null일 가능성이 없는 경우에만 사용하는 것이 좋습니다.

        //when
        userService.updateUserName(request)

        //then
        val result = userRepository.findAll()[0]
        assertThat(result.name).isEqualTo("B")
    }
    @Test
    @DisplayName("유저 삭제가 정상 동작합니다.")
    fun deleteUserTest() {
        //given
        userRepository.save(User("A", 10))

        //when
        userService.deleteUser("A")

        //then
        val result = userRepository.findAll()
        assertThat(result).hasSize(0)
        assertThat(result).isEmpty()

    }


    @Test
    @DisplayName("대출 기록이 없는 유저도 응답에 포함된다")
    fun getUserLoanHistoriesTest1() {
        //given
        userRepository.save(User("A", null))

        //when
        val results = userService.getUserLoanHistories()

        //then
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("A")
        assertThat(results[0].books).isEmpty()
    }

    @Test
    @DisplayName("대출 기록이 많은 유저의 응답이 정상 작동한다")
    fun getUserLoanHistoriesTest2() {
        //given
        val savedUser = userRepository.save(User("A", null))
        userLoanHistoryRepository.saveAll(listOf(
            UserLoanHistory.fixture(savedUser, "책1", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(savedUser, "책2", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(savedUser, "책3", UserLoanStatus.RETURNED),
        ))

        //when
        val results = userService.getUserLoanHistories()

        //then
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("A")
        assertThat(results[0].books).hasSize(3)
        assertThat(results[0].books).extracting("name")
            .containsExactlyInAnyOrder("책1", "책2", "책3")
        assertThat(results[0].books).extracting("isReturn")
            .containsExactlyInAnyOrder(false, false, true)
    }


    @Test
    @DisplayName("모두 합쳐진 기능 확인 테스트") // 이런식으로 안하는게 좋음, 이해하기 어렵고, 첫번째 기능이 에러가 나면 두번째 기능 테스트는 아예 불가함
    fun getUserLoanHistoriesTest3() {
        //given
        val savedUsers = userRepository.saveAll(listOf(
            User("A", null),
            User("B", null),
            ))
        userLoanHistoryRepository.saveAll(listOf(
            UserLoanHistory.fixture(savedUsers[0], "책1", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(savedUsers[0], "책2", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(savedUsers[0], "책3", UserLoanStatus.RETURNED),
        ))

        userLoanHistoryRepository.saveAll(listOf(
            UserLoanHistory.fixture(savedUsers[1], "책4", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(savedUsers[1], "책5", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(savedUsers[1], "책6", UserLoanStatus.RETURNED),
        ))

        //when
        val results = userService.getUserLoanHistories()

        //then
        assertThat(results).hasSize(2)
        val userAResult = results.first { user -> user.name == "A"}
        assertThat(userAResult.name).isEqualTo("A")
        assertThat(userAResult.books).hasSize(3)
        assertThat(userAResult.books).extracting("name")
            .containsExactlyInAnyOrder("책1", "책2", "책3")
        assertThat(userAResult.books).extracting("isReturn")
            .containsExactlyInAnyOrder(false, false, true)

        val userBResult = results.first { user -> user.name == "B"}
        assertThat(userBResult.name).isEqualTo("B")
        assertThat(userBResult.books).hasSize(3)
        assertThat(userBResult.books).extracting("name")
            .containsExactlyInAnyOrder("책4", "책5", "책6")
        assertThat(userBResult.books).extracting("isReturn")
            .containsExactlyInAnyOrder(false, false, true)
    }



}


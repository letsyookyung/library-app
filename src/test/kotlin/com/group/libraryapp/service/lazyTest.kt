package com.group.libraryapp.service

import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.service.user.UserService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class lazyTest @Autowired constructor(
    private val userService: UserService,
    private val userRepository: UserRepository,
){
    @Transactional
    @Test
    fun oneUserAndTwoBooks() {
        //when
        userService.saveUserAndLoanTwoBooks()
        
        //then
        val users = userRepository.findAll()
        assertThat(users).hasSize(1)
        assertThat(users[0].userLoanHistories).hasSize(2)
        
    }
}
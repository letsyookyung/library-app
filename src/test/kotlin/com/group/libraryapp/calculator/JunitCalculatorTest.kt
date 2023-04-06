package com.group.libraryapp.calculator

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException

// assertThat
// isEqualTo
// isTrue, isFalse
// hasSize
// containsExactly(InAnyOrder)

// assertThrows<예외>


class JunitCalculatorTest {

    @Test
    fun addTest() {
        //given
        val calculator = Calculator(5)

        //when
        calculator.add(3)

        //then
        assertThat(calculator.number).isEqualTo(8) //단언문
    }

    @Test
    fun minusTest() {
        //given
        val calculator = Calculator(6)

        //when
        calculator.minus(3)

        //then
        assertThat(calculator.number).isEqualTo(3) //단언문
    }

    @Test
    fun multiplyTest() {
        //given
        val calculator = Calculator(5)

        //when
        calculator.multiply(3)

        //then
        assertThat(calculator.number).isEqualTo(15) //단언문
    }

    @Test
    fun divideTest() {
        //given
        val calculator = Calculator(5)

        //when
        calculator.divide(3)

        //then
        assertThat(calculator.number).isEqualTo(1) //단언문
    }

    @Test
    fun divideExceptionTest() {
        //given
        val calculator = Calculator(5)

//        //when & then
//        val message = assertThrows<IllegalArgumentException> {
//            calculator.divide(0)
//        }.message
//        assertThat(message).isEqualTo("0으로 나눌 수 없습니다")

        //when & then (scope function 쓴 버전)
        assertThrows<IllegalArgumentException> {
            calculator.divide(0)
        }.apply {
            assertThat(message).isEqualTo("0으로 나눌 수 없습니다")
        }
    }



}
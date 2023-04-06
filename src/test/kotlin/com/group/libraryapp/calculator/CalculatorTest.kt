package com.group.libraryapp.calculator

import java.lang.IllegalArgumentException

// 테스트 하는 방법 1: 본 클래스를 data class로 바꿔서
// 테스트 하는 방법 2: 본 클래스의 number 변수를 public으로 바꿔서 바로 가져옴
// 테스트 하는 방법 3: 본 클래스의 number 변수는 private으로 둔 채 배킹 프로버티 생성하여, getter를 열어서
// 하지만 이 강의에서는 간결하게 하기 위헤 private -> public으로 바꾸되 직접 접근하지 않도록 하면서 사용
//
fun main() {
    val calculatorTest = CalculatorTest()
    calculatorTest.addTest()
    calculatorTest.minustTest()
    calculatorTest.multiplyTest()
    calculatorTest.divideTest()
    calculatorTest.divideExceptionTest()
}

class CalculatorTest {

    fun addTest() {
        // 1) 테스트 하고 싶은 대상 만듦 = given
        val calculator = Calculator(5)
        // 2) 테스트 하고 싶은 기능을 호출 = when
        calculator.add(3)

        // 3) 테스트 후 의도한대로 나왔는지 확인 = then

        // 테스트 방법 1
//        val expectedCalculator = Calculator(8)
//        if (calculator != expectedCalculator) {
//            throw IllegalStateException()
//        }
        // 테스트 방법 2,3
        if (calculator.number != 8) {
            throw IllegalStateException()
        }
    }

    fun minustTest() {
        //given
        val calculator = Calculator(5)

        //when
        calculator.minus(5)

        //then
        if (calculator.number != 0) {
            throw IllegalStateException()
        }
    }

    fun multiplyTest() {
        //given
        val calculator = Calculator(10)

        //when
        calculator.multiply(2)

        //then
        if (calculator.number != 20) {
            throw IllegalStateException()
        }
    }

    fun divideTest() {
        //given
        val calculator = Calculator(10)

        //when
        calculator.divide(2)

        //then
        if (calculator.number != 5) {
            throw IllegalStateException()
        }
    }

    fun divideExceptionTest() {
        //given
        val calculator = Calculator(5)

        //when
        try {
            calculator.divide(0)
        } catch (e: IllegalArgumentException) {
            // 테스트 성공!
            if (e.message != "0으로 나눌 수 없습니다") {
                throw IllegalStateException("메시지가 다릅니다.")
            }
            return
        } catch (e: Exception) {
            throw IllegalStateException()
        }
        throw IllegalStateException("기대하지 않는 예외가 발생하지 않았습니다.")
    }

}
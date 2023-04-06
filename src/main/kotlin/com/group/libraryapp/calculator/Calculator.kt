package com.group.libraryapp.calculator

class Calculator(
//    private var _number: Int //공식 컨벤션
    var number: Int
) {

    // 방법 3
//    val number: Int
//        get() = this._number

    fun add(operand: Int) {
        this.number += operand
    }

    fun minus(operand: Int) {
        this.number -= operand
    }

    fun multiply(operand: Int) {
        this.number *= operand
    }

    fun divide(operand: Int) {
        if (operand == 0) {
            throw IllegalArgumentException("0으로 나눌 수 없습니다")
        }
        this.number /= operand
    }

}
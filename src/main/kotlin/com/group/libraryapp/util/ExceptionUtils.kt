package com.group.libraryapp.util

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull

/*
fun fail(): Nothing은 함수가 호출된 후 반환할 값이 없음을 나타내는 특별한 유형 Nothing을 반환합니다.
이는 일반적으로 예외를 던지거나 무한 루프 등에서 사용됩니다. 즉, 이 함수는 예외를 던지면서 아무것도 반환하지 않습니다.

fun fail()는 반환 유형을 선언하지 않았으며, 컴파일러는 이 함수가 Unit이라는 기본 반환 유형을 가지고 있음을 추론합니다.
이 함수는 예외를 던지면서 Unit을 반환합니다.

반환 유형을 명시적으로 선언하여 코드를 명확하게 만드는 것이 좋으나,
함수가 예외를 던지는 경우에는 Nothing을 반환 유형으로 사용하는 것이 좋습니다.
 */


fun fail(): Nothing { // tip Nothing 은 unit과 뭐가 다른가?
    throw IllegalArgumentException()
}


// crud의 확장함수 이용
fun <T, ID> CrudRepository<T, ID>.findByOrThrow(id: ID): T {
    // 기존에 Java에 있던 crudrep 인터페이스를 원본으로 하여, findByOrThrow가 마치 crudrepository 안에 있는 것처럼 사용하
    return this.findByIdOrNull(id) ?: fail() // this는 crudrepository를 가르키고,
}

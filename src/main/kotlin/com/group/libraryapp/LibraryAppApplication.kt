package com.group.libraryapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class LibraryAppApplication

// 이런식으로 쓰면 함수들이 static으로 감시됨
fun main(args: Array<String>) {
    runApplication<LibraryAppApplication>(*args)
}
package com.group.libraryapp.domain.book

import javax.persistence.*

// 두 생성자가 존재하고, name과 id로 받고 있음
// 그래서 args를 아무것도 받지 않는 기본 생성자가 없어서 에러가 남, 그래서 jpa plugin 추가하니 해결됨
@Entity
class Book constructor(
    val name: String,

    // enum class를 쓰면 숫자가 db로 쌓여서, 순서가 바뀌거나 그러면 난리날 수 있음
    @Enumerated(EnumType.STRING)
    val type: BookType,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {
    init {
        if (name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다")
        }
    }

    // 테스트 코드만을 위한 것을 미리 만들어둠 OBJECT MOTHER PATTERN, TEST FIXTURE
    companion object {
        fun fixture(
            name: String = "책 이름",
            type: BookType = BookType.COMPUTER,
            id: Long? = null,
        ) : Book {
            return Book(
                name = name,
                type = type,
                id = id,
            )
        }
    }


}

package at.paukl.springDataPlay.db

import org.springframework.data.annotation.Id
import java.time.LocalDateTime

data class User(
    @Id var id: Long? = null,
    var name: String,
    val signupDateTime: LocalDateTime
)
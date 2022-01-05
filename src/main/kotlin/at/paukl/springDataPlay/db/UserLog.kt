package at.paukl.springDataPlay.db

import org.springframework.data.annotation.Id
import java.time.LocalDateTime

data class UserLog(
    @Id var id: Long? = null,
    val userId: Long,
    val dateTime: LocalDateTime,
    val log: String
)
package at.paukl.springDataPlay

import at.paukl.springDataPlay.db.User
import at.paukl.springDataPlay.db.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userLogService: UserLogService
) {

    @Transactional
    fun signup(name: String) {
        LOG.info("user signup start")

        val user = userRepository.save(
            User(
                name = name,
                signupDateTime = LocalDateTime.now()
            )
        )

        userLogService.logUserAction(user.id!!, "User signed up")

        LOG.info("user signup finish")
    }

    @Transactional
    fun signupFixed(name: String) {
        LOG.info("user signup start")

        val user = userRepository.save(
            User(
                name = name,
                signupDateTime = LocalDateTime.now()
            )
        )

        userLogService.logUserActionFixed(user.id!!, "User signed up")

        LOG.info("user signup finish")
    }

    @Transactional(readOnly = true)
    fun countUsers(): Long {
        return userRepository.count()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DemoController.javaClass)
    }
}
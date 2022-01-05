package at.paukl.springDataPlay

import at.paukl.springDataPlay.db.UserLog
import at.paukl.springDataPlay.db.UserLogRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager
import java.time.LocalDateTime

@Service
class UserLogService(
    private val userLogRepository: UserLogRepository
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun logUserAction(userId: Long, action: String) {
        LOG.info("logUserAction start for userId {} and action {}", userId, action)

        userLogRepository.save(
            UserLog(
                userId = userId,
                dateTime = LocalDateTime.now(),
                log = action
            )
        )

        LOG.info("logUserAction done")
    }

    // Propagation.MANDATORY makes sure a transaction is ongoing so we have something to "sync to"
    @Transactional(propagation = Propagation.MANDATORY)
    fun logUserActionFixed(userId: Long, action: String) {
        LOG.info("logUserActionFixed start for userId {} and action {}", userId, action)

        TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {
            override fun afterCompletion(status: Int) {
                // NOTE: we could even check if the initial transaction failed or succeeded via the status parameter
                LOG.info("starting actual logging to repository")
                userLogRepository.save(
                    UserLog(
                        userId = userId,
                        dateTime = LocalDateTime.now(),
                        log = action
                    )
                )
                LOG.info("actual logging to repository done")
            }
        })

        LOG.info("logUserActionFixed done (no actual logging yet)")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DemoController.javaClass)
    }
}

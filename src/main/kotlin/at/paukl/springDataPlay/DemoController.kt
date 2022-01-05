package at.paukl.springDataPlay

import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DemoController(
    private val userService: UserService
) {

    @GetMapping("/")
    fun index() = "send a POST request to /signup (for deadlock demo) or GET /db for a readonly db operation"

    @GetMapping("/db")
    fun dbRead() = userService.countUsers()

    @PostMapping("/signup")
    fun signup(): String {
        LOG.info("signup before service call")

        val name = createUserName()
        userService.signup(name)

        LOG.info("signup after service call")
        return "You successfully signed up as ${name}"
    }

    @PostMapping("/signupFixed")
    fun signupFixed(): String {
        LOG.info("signupFixed before service call")

        val name = createUserName()
        userService.signupFixed(name)

        LOG.info("signupFixed after service call")
        return "You successfully signed up as ${name}"
    }

    // Users have already requested to be able to pick custom names, but this is good enough for
    //  our MVP
    private fun createUserName() = "User-${System.currentTimeMillis()}"

    companion object {
        private val LOG = LoggerFactory.getLogger(DemoController.javaClass)
    }
}
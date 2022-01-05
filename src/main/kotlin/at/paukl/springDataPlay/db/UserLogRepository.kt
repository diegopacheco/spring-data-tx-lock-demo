package at.paukl.springDataPlay.db

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface UserLogRepository : PagingAndSortingRepository<UserLog, Long> {


}
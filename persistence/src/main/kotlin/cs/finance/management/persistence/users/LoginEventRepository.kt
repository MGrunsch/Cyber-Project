package cs.finance.management.persistence.users

import org.springframework.data.jpa.repository.JpaRepository


interface LoginEventRepository : JpaRepository<LoginEvent, Long>
package cs.finance.management.business.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import cs.finance.management.persistence.users.User as PersistedUser
import org.springframework.security.core.userdetails.User as SpringUser


class MyUserDetails private constructor(
    username: String,
    password: String,
    authorities: Collection<GrantedAuthority> = AuthorityUtils.createAuthorityList("ROLE_GUEST")
): SpringUser(
    username,
    password,
    authorities,
) {

    constructor(user: PersistedUser, vararg authorities: String = arrayOf("ROLE_USER")) : this(
        username = user.mail,
        password = user.password,
        authorities = AuthorityUtils.createAuthorityList(*authorities)
    )

}
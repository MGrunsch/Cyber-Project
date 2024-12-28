package cs.finance.management.business.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import cs.finance.management.persistence.users.User as PersistedUser
import org.springframework.security.core.userdetails.User as SpringUser


class MyUserDetails private constructor(
    username: String,
    password: String,
    enabled: Boolean = true,
    accountNonExpired: Boolean = true,
    credentialsNonExpired: Boolean = true,
    accountNonLocked: Boolean = true,
    authorities: Collection<GrantedAuthority> = AuthorityUtils.createAuthorityList("ROLE_USER")
): SpringUser(
    username,
    password,
    enabled,
    accountNonExpired,
    credentialsNonExpired,
    accountNonLocked,
    authorities,
) {

    constructor(user: PersistedUser, vararg authorities: String = arrayOf("ROLE_USER")) : this(
        username = user.mail,
        password = user.password,
        enabled = user.enabled,
        authorities = AuthorityUtils.createAuthorityList(*authorities)
    )

}
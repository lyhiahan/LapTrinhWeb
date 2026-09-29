package vn.iotstar.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.iotstar.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {
    private static final long serialVersionUID = 1L;
    private final Long id;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final boolean locked;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.enabled = user.isEnabled() && user.isEmailVerified();
        this.locked = user.isLocked();
        List<GrantedAuthority> auths = new ArrayList<>();
        if (user.getRole() != null && user.getRole().getName() != null) {
            String raw = user.getRole().getName().trim();
            auths.add(new SimpleGrantedAuthority(raw));
            String upper = raw.toUpperCase();
            if (!upper.startsWith("ROLE_")) {
                auths.add(new SimpleGrantedAuthority("ROLE_" + upper));
            } else {
                auths.add(new SimpleGrantedAuthority(upper.substring(5).toLowerCase()));
            }
        } else {
            auths.add(new SimpleGrantedAuthority("ROLE_USER"));
            auths.add(new SimpleGrantedAuthority("user"));
        }
        this.authorities = auths;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }
}

package org.boot.registersession.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.boot.registersession.model.user.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "\"user\"",
indexes = {
        @Index(name = "user_username_idx", columnList = "username", unique = true),
})
@Getter
@Setter
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column
    private ZonedDateTime createdDateTime;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role.equals(Role.ADMIN)) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_" + Role.ADMIN.name()),
                    new SimpleGrantedAuthority(Role.ADMIN.name()),
                    new SimpleGrantedAuthority("ROLE_" + Role.USER.name()),
                    new SimpleGrantedAuthority(Role.USER.name())
            );
        } else {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_" + Role.USER.name()),
                    new SimpleGrantedAuthority(Role.USER.name())
            );
        }
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
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(userId, that.userId) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(name, that.name) && Objects.equals(email, that.email) && role == that.role && Objects.equals(createdDateTime, that.createdDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, name, email, role, createdDateTime);
    }

    public static UserEntity of(String username, String password, String name, String email) {
        var UserEntity = new UserEntity();
        UserEntity.setUsername(username);
        UserEntity.setPassword(password);
        UserEntity.setName(name);
        UserEntity.setEmail(email);
        UserEntity.setRole(Role.USER);
        return UserEntity;
    }

    @PrePersist
    private void prePersist() {
        this.createdDateTime = ZonedDateTime.now();
    }
}

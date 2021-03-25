package ir.maktab.investment.model;


import com.google.common.collect.Sets;
import ir.maktab.constraint.ValidPassword;
import ir.maktab.investment.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class UserSecurity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "must not be blank.")
    @Size(min = 6, max = 50, message = "must have 2 to 50 characters.")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "must not be blank.")
    @ValidPassword
    private String password;

//    @Enumerated(EnumType.STRING)
//    @ElementCollection(fetch = FetchType.EAGER)
//    private Set<Role> roles;

    @Enumerated(EnumType.STRING)
//    @ElementCollection(fetch = FetchType.EAGER)
    private Role role;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean isEnabled;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean isDeleted;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Sets.newHashSet(new SimpleGrantedAuthority("ROLE_"+role.name()));
//        Set<GrantedAuthority> authorities = new HashSet<>();
//        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_"+role.name())));
//        return authorities;
    }

//    public void addRole(Role role) {
//        if (roles == null)
//            roles = new HashSet<>();
//        roles.add(role);
//    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.isDeleted;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

//    @Override
//    public boolean isAccountNonExpired() {
//        return this.isAccountNonExpired;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return this.isAccountNonLocked;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return this.isCredentialsNonExpired;
//    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}

package com.potential.api.auth;

import com.potential.api.model.BaseEntity;
import java.util.Collection;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@NoArgsConstructor
@AllArgsConstructor
public class OAuthUser extends BaseEntity implements OAuth2User {
    private String id;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return this.id;
    }
}
package com.potential.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "post")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Post extends BaseEntity {
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_name", referencedColumnName = "username", nullable = false)
    private User user;

    public void incrementViewCount() {
        this.viewCount += 1;
    }
}

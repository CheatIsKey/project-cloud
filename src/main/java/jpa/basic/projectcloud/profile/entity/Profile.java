package jpa.basic.projectcloud.profile.entity;

import jakarta.persistence.*;
import jpa.basic.projectcloud.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "profiles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String s3Key;

    @Column(nullable = false)
    private String originalName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Profile(User user, String s3Key, String originalName) {
        this.user = user;
        this.s3Key = s3Key;
        this.originalName = originalName;
    }
}

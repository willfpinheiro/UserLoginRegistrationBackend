package com.example.userLogin.registration.token;

import com.example.userLogin.appuser.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {
    @SequenceGenerator(
            name = "confirmation_token_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1

    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence"
    )
    private Long id;

    @Column(nullable = false) //nao pode ser null
    private String token;

    @Column(nullable = false)
    private LocalDateTime creatAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;

    public ConfirmationToken(String token,
                             LocalDateTime creatAt,
                             LocalDateTime expiresAt,
                             AppUser appUser) {
        this.token = token;
        this.creatAt = creatAt;
        this.expiresAt = expiresAt;
        this.appUser = appUser;
    }
}

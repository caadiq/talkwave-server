package mcnc.talkwave.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id
    @Column(name = "user_id")
    private String userId;

    private String password;

    private String name;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Departure departure;

    @Builder
    public User(String userId, String password, String name, Departure departure) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.departure = departure;
    }

    public static User createUser(String userId, String password, String name, Departure departure) {
        return User.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .departure(departure)
                .build();
    }
}

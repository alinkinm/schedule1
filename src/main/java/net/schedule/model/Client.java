package net.schedule.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="guest")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="login")
    private String login;
}

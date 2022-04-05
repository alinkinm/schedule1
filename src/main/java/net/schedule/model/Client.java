package net.schedule.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="guest")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client extends BaseEntity{

    @Id
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="login")
    private String login;
}

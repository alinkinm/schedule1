package net.schedule.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalTime;

@Entity
@Table(name="event")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="client_id")
    private Long client_id;

    @Column(name="start_time")
    private LocalTime start_time;

    @Column(name="finish_time")
    private LocalTime finish_time;

    @Column(name="day")
    private Date day;
}

package com.todo.todoapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "todo")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String task;
    private LocalDate dateoftask;
    private LocalTime timeoftask;

    private boolean isPerformed = false;



    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", task='" + task + '\'' +
                ", dateoftask='" + dateoftask + '\'' +
                ", timeoftask='" + timeoftask + '\'' +
                ", isPerformed='" + isPerformed + '\'' +
                '}';
    }
        /*
    @OneToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;*/

}


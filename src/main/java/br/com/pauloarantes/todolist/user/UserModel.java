package br.com.pauloarantes.todolist.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name="tb_user")
public class UserModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String name;
    @Column(unique = true)
    private String username;
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;


}

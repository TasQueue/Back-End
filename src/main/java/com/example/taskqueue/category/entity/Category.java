package com.example.taskqueue.category.entity;

import com.example.taskqueue.user.entity.User;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "deleted = false")
@SQLDelete(sql = "update category set deleted = true where category_id = ?")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column
    private String name;

    @Column
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private boolean deleted = false;

    @Builder
    public Category (
            User user,
            String name,
            String color
    ){
        this.user = user;
        this.name = name;
        this.color = color;
    }

    public void updateDeleted(boolean deleted) {
        this.deleted = deleted;}
    public void updateName(String name) {
        this.name = name;
    }

    public void updateColor(String color) {
        this.color = color;
    }

}

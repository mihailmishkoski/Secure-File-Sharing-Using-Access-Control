package mk.ukim.finki.ib_project.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "document")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    List<User> userIds;
    private String fileName;

    public Permission(List<User> userIds, String fileName) {
        this.userIds = userIds;
        this.fileName = fileName;
    }

    public Permission(){}
}

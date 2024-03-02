package mk.ukim.finki.ib_project.model;

import jakarta.persistence.*;
import lombok.Data;

import javax.crypto.SecretKey;
import java.util.List;

@Entity
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private SecretKey decryptionKey;
    @ManyToMany
    List<User> users;
    private String fileName;
    public Permission(List<User> users, String fileName) {
        this.users = users;
        this.fileName = fileName;
    }

    public Permission(){}
}

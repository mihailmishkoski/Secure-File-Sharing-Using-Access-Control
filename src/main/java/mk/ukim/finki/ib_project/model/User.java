package mk.ukim.finki.ib_project.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;
    private String surname;
    private String email;
    private String password;
    private int twoFactorCode;
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Document> documentList;
    public User(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }
    public User(){}
}

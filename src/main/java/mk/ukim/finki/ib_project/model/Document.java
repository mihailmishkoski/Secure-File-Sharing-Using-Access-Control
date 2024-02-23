package mk.ukim.finki.ib_project.model;

import jakarta.persistence.*;

import lombok.NoArgsConstructor;

import javax.print.Doc;

@Entity
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String postMessage;

    public Document(String postMessage) {
        this.postMessage = postMessage;
    }
    public Document(){}
}

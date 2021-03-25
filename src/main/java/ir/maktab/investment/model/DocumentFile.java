package ir.maktab.investment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
public class DocumentFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String name;

    @Lob
    private byte[] content;

    private Long size;

    private Date uploadDate;

    @ManyToOne
    @JoinColumn(name = "fk_request")
    private Request request;
}

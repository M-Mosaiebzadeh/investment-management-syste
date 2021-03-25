package ir.maktab.investment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String describtion;

    private Date date;

    @ManyToOne()
    @JoinColumn(name = "fk_responsible")
    private User responsible;

    @OneToOne
    @JoinColumn(name = "fk_request")
    private Request request;
}

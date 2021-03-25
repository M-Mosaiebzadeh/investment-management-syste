package ir.maktab.investment.model;

import ir.maktab.investment.model.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_subject")
    private RequestSubject subject;


    // describe is a reserved word in mysql
    @Column(length = 4000)
    private String describtion;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @OneToMany(mappedBy = "request")
    private Set<DocumentFile> files;

    private Date date;

    @ManyToOne()
    @JoinColumn(name = "fk_user")
    private User user;

    @OneToOne
    @JoinColumn(name = "fk_response")
    private Response response;

}

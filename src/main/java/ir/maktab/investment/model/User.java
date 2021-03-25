package ir.maktab.investment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
//@MappedSuperclass
public class User extends UserSecurity {

    @NotBlank(message = "must not be blank.")
    @Size(min = 2, max = 50, message = "must have 2 to 50 characters.")
    private String firstname;

    @NotBlank(message = "must not be blank.")
    @Size(min = 2, max = 50, message = "must have 2 to 50 characters.")
    private String lastname;

    @NotBlank(message = "must not be blank.")
    @Size(min = 11, max = 11, message = "Must be in the correct format.")
    @Column(unique = true)
    private String phone;

    @NotBlank(message = "must not be blank.")
    @Size(min = 10, max = 10, message = "Must be in the correct format.")
    @Column(unique = true)
    private String nationalCode;

    @NotBlank(message = "must not be blank.")
    @Email(message = "Must be in the correct format.")
    @Column(unique = true)
    private String email;

//    @Column(columnDefinition = "TINYINT(1)")
//    private Boolean isDeleted;

    @OneToMany(mappedBy = "user")
    private Set<Request> userRequests;

    @OneToMany(mappedBy = "responsible")
    private Set<Response> responsibleAnswers;

    @OneToOne
    @JoinColumn(name = "fk_verificationCode")
    private VerificationCode verificationCode;
}

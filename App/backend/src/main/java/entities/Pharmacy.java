package entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pharmacies", schema = "pharmacy-app")
public class Pharmacy {
    @Id
    @Column(name = "id")
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id")
    private User users;


    @Column(name = "pharmacy_name")
    private String pharmacyName;


    @Column(name = "matricule_fiscale")
    private String matriculeFiscale;


    @Column(name = "address")
    private String address;


    @Column(name = "latitude")
    private BigDecimal latitude;


    @Column(name = "longitude")
    private BigDecimal longitude;


    @Column(name = "phone")
    private String phone;


    @Column(name = "verified")
    private Boolean verified;


    @Column(name = "created_at")
    private Instant createdAt;
}
package entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pharmacy")
public class Pharmacy {
    @Id
    @Column(name = "id")
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id")
    private User user;


    @Column(name = "pharmacy_name")
    private String pharmacyName;


    @Column(name = "tax_id")
    private String taxId;

    @Column(name ="email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private BigDecimal latitude;


    @Column(name = "longitude")
    private BigDecimal longitude;


    @Column(name = "schedule")
    private String schedule;

<<<<<<< HEAD
=======
    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false)
    private PharmacyApprovalStatus approvalStatus;

>>>>>>> origin/yassine
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
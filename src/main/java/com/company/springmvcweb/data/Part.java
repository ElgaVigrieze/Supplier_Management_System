package com.company.springmvcweb.data;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "part")
public class Part {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "number")
    private String number;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "default_supplier_id", referencedColumnName = "id", nullable = false)
    private Supplier defaultSupplier;

}

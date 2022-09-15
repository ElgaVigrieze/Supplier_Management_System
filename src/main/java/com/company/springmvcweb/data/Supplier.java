package com.company.springmvcweb.data;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "supplier")
public class Supplier {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name="category", columnDefinition="enum('Fasteners','Raw material','Turning/milling')")
    private String category;
    @Column(name="VAT_number")
    private String VATNo;
    @Column(name="e_mail")
    private String eMail;
    @Transient
    private double supplierPerformance;

    public Supplier(int id, String name, String category, String VATNo, String eMail) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.VATNo = VATNo;
        this.eMail = eMail;
    }

    public Double getSupplierPerformance() {
        var repo = new DeliveryPerformance();
        var dpm = repo.getSupplierPerformancePerSupplier(id);
        if(dpm == null){
            return null;
        }
        return Math.round(dpm*100.0)/100.0*100.0;
    }

    public Double getSupplierPerformanceGeneral() {
        var repo = new DeliveryPerformance();
        var dpm = repo.getSupplierPerformanceGeneral();
        if(dpm == null){
            return null;
        }
        return Math.round(dpm*100.0)/100.0*100.0;
    }

    public Double getSupplierPerformancePerMonth(int supplierId, int month){
        var repo = new DeliveryPerformance();
        var dpm = repo.getSupplierPerformancePerMonthPerSupplier(supplierId, month);
        if(dpm == null){
            return null;
        }
        return Math.round(dpm*100.0)/100.0*100.0;
    }

    public Double getSupplierPerformancePerMonth(int month){
        var repo = new DeliveryPerformance();
        var dpm = repo.getSupplierPerformancePerMonth(month);
        if(dpm == null){
            return null;
        }
        return Math.round(dpm*100.0)/100.0*100.0;
    }

}

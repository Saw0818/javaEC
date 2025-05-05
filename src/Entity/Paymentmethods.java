/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author User
 */
@Entity
@Table(name = "PAYMENTMETHODS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Paymentmethods.findAll", query = "SELECT p FROM PAYMENTMETHODS p"),
    @NamedQuery(name = "Paymentmethods.findByMethodid", query = "SELECT p FROM PAYMENTMETHODS p WHERE p.METHODID = :METHODID"),
    @NamedQuery(name = "Paymentmethods.findByMethodname", query = "SELECT p FROM PAYMENTMETHODS p WHERE p.METHODNAME = :METHODNAME")})
public class Paymentmethods implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "METHODNAME")
    private String methodname;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "METHODID")
    private String methodid;

    public Paymentmethods() {
    }

    public Paymentmethods(String methodid) {
        this.methodid = methodid;
    }

    public Paymentmethods(String methodid, String methodname) {
        this.methodid = methodid;
        this.methodname = methodname;
    }

    public String getMethodid() {
        return methodid;
    }

    public void setMethodid(String methodid) {
        this.methodid = methodid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (methodid != null ? methodid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Paymentmethods)) {
            return false;
        }
        Paymentmethods other = (Paymentmethods) object;
        if ((this.methodid == null && other.methodid != null) || (this.methodid != null && !this.methodid.equals(other.methodid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Paymentmethods[ methodid=" + methodid + " ]";
    }

    public String getMethodname() {
        return methodname;
    }

    public void setMethodname(String methodname) {
        this.methodname = methodname;
    }
    
}

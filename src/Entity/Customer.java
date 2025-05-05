/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author User
 */
@Entity
@Table(name = "CUSTOMER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Customer.findAll", query = "SELECT c FROM CUSTOMER c Order BY c.CUSTOMERID"),
    @NamedQuery(name = "Customer.findByCustomerid", query = "SELECT c FROM CUSTOMER c WHERE c.CUSTOMERID = :CUSTOMERID"),
    @NamedQuery(name = "Customer.findAllCustomerIds", query = "SELECT c.CUSTOMERID FROM CUSTOMER c ORDER BY c.CUSTOMERID ASC"),
    @NamedQuery(name = "Customer.findByCustomername", query = "SELECT c FROM CUSTOMER c WHERE c.CUSTOMERNAME = :CUSTOMERNAME"),
    @NamedQuery(name = "Customer.findByIc", query = "SELECT c FROM CUSTOMER c WHERE c.IC = :IC"),
    @NamedQuery(name = "Customer.findByPhoneno", query = "SELECT c FROM CUSTOMER c WHERE c.PHONENO = :PHONENO"),
    @NamedQuery(name = "Customer.findByCustomerPhone", query = "SELECT c.PHONENO FROM CUSTOMER c WHERE c.PHONENO = :PHONE"),
    @NamedQuery(name = "Customer.findByHomeaddress", query = "SELECT c FROM CUSTOMER c WHERE c.HOMEADDRESS = :HOMEADDRESS"),
    @NamedQuery(name = "Customer.findByEmail", query = "SELECT c FROM CUSTOMER c WHERE c.EMAIL = :EMAIL"),
    @NamedQuery(name = "Customer.findByPassword", query = "SELECT c FROM CUSTOMER c WHERE c.PASSWORD = :PASSWORD"),
    @NamedQuery(name = "Customer.findProfileByID", query = "SELECT c.PROFILEIMAGE FROM CUSTOMER c WHERE c.CUSTOMERID = :CUSTOMERID"),
    @NamedQuery(name = "Customer.findAllOrdered", query = "SELECT c FROM CUSTOMER c ORDER BY c.CUSTOMERID ASC")})
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
        @Id
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "CUSTOMERID")
    private String customerid;
    @Column(name = "PROFILEIMAGE")
    private String profileImage;
    @Size(max = 50)
    @Column(name = "CUSTOMERNAME")
    private String customername;
    
    @Size(max = 12)
    @Column(name = "IC")
    private String ic;
    
    @Size(max = 13)
    @Column(name = "PHONENO")
    private String phoneno;
    
    @Size(max = 100)
    @Column(name = "HOMEADDRESS")
    private String homeaddress;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "EMAIL")
    private String email;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "PASSWORD")
    private String password;
    
    public Customer() {
    }

    public Customer(String customerid) {
        this.customerid = customerid;
    }

    public Customer(String customerid, String customerName,String ic,String phoneno,String homeaddress,String email,String password,String profile){
        this.customerid = customerid;
        this.customername = customerName;
        this.ic = ic;
        this.phoneno = phoneno;
        this.email = email;
        this.password = password;
        this.homeaddress = homeaddress;
        this.profileImage = profile;
    }

    public String getCustomerid() {
        return customerid;
    }
    public String getProfileImage() {
    return profileImage;
    }

// Setter
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getHomeaddress() {
        return homeaddress;
    }

    public void setHomeaddress(String homeaddress) {
        this.homeaddress = homeaddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerid != null ? customerid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerid == null && other.customerid != null) || (this.customerid != null && !this.customerid.equals(other.customerid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerid='" + customerid + '\'' +
                ", customername='" + customername + '\'' +
                ", ic='" + ic + '\'' +
                ", phoneno='" + phoneno + '\'' +
                ", homeaddress='" + homeaddress + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
    
}

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
@Table(name = "STAFF")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Staff.findAll", query = "SELECT s FROM STAFF s"),
    @NamedQuery(name = "Staff.findByStaffid", query = "SELECT s FROM STAFF s WHERE s.STAFFID = :STAFFID"),
    @NamedQuery(name = "Staff.findByStaffname", query = "SELECT s FROM STAFF s WHERE s.STAFFNAME = :STAFFNAME"),
    @NamedQuery(name = "Staff.findByIc", query = "SELECT s FROM STAFF s WHERE s.IC = :IC"),
    @NamedQuery(name = "Staff.findByPhoneno", query = "SELECT s FROM STAFF s WHERE s.PHONENO = :PHONENO"),
    @NamedQuery(name = "Staff.findByHomeaddress", query = "SELECT s FROM STAFF s WHERE s.HOMEADDRESS = :HOMEADDRESS"),
    @NamedQuery(name = "Staff.findByEmail", query = "SELECT s FROM STAFF s WHERE s.EMAIL= :EMAIL"),
    @NamedQuery(name = "Staff.findByPassword", query = "SELECT s FROM STAFF s WHERE s.PASSWORD = :PASSWORD"),
    @NamedQuery(name = "Staff.findByIsfirstlogin", query = "SELECT s FROM STAFF s WHERE s.ISFIRSTLOGIN = :ISFIRSTLOGIN")})
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "STAFFID")
    private String staffid;
    @Size(max = 50)
    @Column(name = "STAFFNAME")
    private String staffname;
    @Size(max = 12)
    @Column(name = "IC")
    private String ic;
    @Size(max = 13)
    @Column(name = "PHONENO")
    private String phoneno;
    @Size(max = 100)
    @Column(name = "HOMEADDRESS")
    private String homeaddress;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "ISFIRSTLOGIN")
    private Boolean isfirstlogin;

    public Staff() {
    }

    public Staff(String staffid) {
        this.staffid = staffid;
    }

    public Staff(String staffid, String email, String password) {
        this.staffid = staffid;
        this.email = email;
        this.password = password;
    }

    public String getStaffid() {
        return staffid;
    }

    public void setStaffid(String staffid) {
        this.staffid = staffid;
    }

    public String getStaffname() {
        return staffname;
    }

    public void setStaffname(String staffname) {
        this.staffname = staffname;
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

    public Boolean getIsfirstlogin() {
        return isfirstlogin;
    }

    public void setIsfirstlogin(Boolean isfirstlogin) {
        this.isfirstlogin = isfirstlogin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (staffid != null ? staffid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Staff)) {
            return false;
        }
        Staff other = (Staff) object;
        if ((this.staffid == null && other.staffid != null) || (this.staffid != null && !this.staffid.equals(other.staffid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Staff[ staffid=" + staffid + " ]";
    }
    
}

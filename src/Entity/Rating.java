package Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "RATING")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rating.findAll", query = "SELECT r FROM RATING r"),
    @NamedQuery(name = "Rating.findByRatingid", query = "SELECT r FROM RATING r WHERE r.RATINGID = :RATINGID"),
    @NamedQuery(name = "Rating.findByRatingstar", query = "SELECT r FROM RATING r WHERE r.RATINGSTAR = :RATINGSTAR"),
    @NamedQuery(name = "Rating.findByRatingdesc", query = "SELECT r FROM RATING r WHERE r.RATINGDESC = :RATINGDESC"),
    @NamedQuery(name = "Rating.findByRatingtime", query = "SELECT r FROM RATING r WHERE r.RATINGTIME = :RATINGTIME")})
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "RATINGID")
    private String ratingid;
    @Column(name = "RATINGSTAR")
    private Integer ratingstar;
    @Size(max = 300)
    @Column(name = "RATINGDESC")
    private String ratingdesc;
    @Size(max = 50)
    @Column(name = "RATINGTIME")
    private String ratingtime;
    @Size(max = 6)
    @Column(name = "PRODUCTID")
    private String customerid;
    @Size(max = 6)
    @Column(name = "CUSTOMERID")
    private String productid;

    public Rating() {
    }

    public Rating(String ratingid, int ratingstar, String ratingdesc, String ratingtime, String productid, String customerid) {
        this.ratingid = ratingid;
        this.ratingstar = ratingstar;
        this.ratingdesc = ratingdesc;
        this.ratingtime = ratingtime;
        this.productid = productid;
        this.customerid = customerid;
    }

    public String getRatingid() {
        return ratingid;
    }

    public void setRatingid(String ratingid) {
        this.ratingid = ratingid;
    }

    public Integer getRatingstar() {
        return ratingstar;
    }

    public void setRatingstar(Integer ratingstar) {
        this.ratingstar = ratingstar;
    }

    public String getRatingdesc() {
        return ratingdesc;
    }

    public void setRatingdesc(String ratingdesc) {
        this.ratingdesc = ratingdesc;
    }

    public String getRatingtime() {
        return ratingtime;
    }

    public void setRatingtime(String ratingtime) {
        this.ratingtime = ratingtime;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ratingid != null ? ratingid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rating)) {
            return false;
        }
        Rating other = (Rating) object;
        if ((this.ratingid == null && other.ratingid != null) || (this.ratingid != null && !this.ratingid.equals(other.ratingid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Rating[ ratingid=" + ratingid + " ]";
    }
    
}

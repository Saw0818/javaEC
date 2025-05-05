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
@Table(name = "PRODUCT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM PRODUCT p"),
    @NamedQuery(name = "Product.findByProductid", query = "SELECT p FROM PRODUCT p WHERE p.PRODUCTID = :PRODUCTID"),
    @NamedQuery(name = "Product.findByProductname", query = "SELECT p FROM PRODUCT p WHERE p.PRODUCTNAME = :PRODUCTNAME"),
    @NamedQuery(name = "Product.findByProductprice", query = "SELECT p FROM PRODUCT p WHERE p.PRODUCTPRICE = :PRODUCTPRICE"),
    @NamedQuery(name = "Product.findByProductdesc", query = "SELECT p FROM PRODUCT p WHERE p.PRODUCTDESC = :PRODUCTDESC"),
    @NamedQuery(name = "Product.findByProductimg", query = "SELECT p FROM PRODUCT p WHERE p.PRODUCTIMG = :PRODUCTIMG"),
    @NamedQuery(name = "Product.findByProductlongdesc", query = "SELECT p FROM PRODUCT p WHERE p.PRODUCTLONGDESC = :PRODUCTLONGDESC")})
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "PRODUCTID")
    private String productid;
    @Size(max = 50)
    @Column(name = "PRODUCTNAME")
    private String productname;
    @Column(name = "PRODUCTPRICE")
    private Integer productprice;
    @Size(max = 100)
    @Column(name = "PRODUCTDESC")
    private String productdesc;
    @Size(max = 100)
    @Column(name = "PRODUCTIMG")
    private String productimg;
    @Size(max = 300)
    @Column(name = "PRODUCTLONGDESC")
    private String productlongdesc;
    @Size(max = 6)
    @Column(name = "ROOMID")
    private String roomid;

    public Product() {
    }
            
    public Product(String productid, String productname, int productprice, String productdesc, String productimg){
        this.productid = productid;
        this.productname = productname;
        this.productprice = productprice;
        this.productdesc = productdesc;
        this.productimg = productimg;
    }

    public Product(String productid, String productname, int productprice, String productdesc, String productimg, String productlongdesc, String roomid) {
        this.productid = productid;
        this.productname = productname;
        this.productprice = productprice;
        this.productdesc = productdesc;
        this.productimg = productimg;
        this.productlongdesc = productlongdesc;
        this.roomid = roomid;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public Integer getProductprice() {
        return productprice;
    }

    public void setProductprice(Integer productprice) {
        this.productprice = productprice;
    }

    public String getProductdesc() {
        return productdesc;
    }

    public void setProductdesc(String productdesc) {
        this.productdesc = productdesc;
    }

    public String getProductimg() {
        return productimg;
    }

    public void setProductimg(String productimg) {
        this.productimg = productimg;
    }

    public String getProductlongdesc() {
        return productlongdesc;
    }

    public void setProductlongdesc(String productlongdesc) {
        this.productlongdesc = productlongdesc;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productid != null ? productid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.productid == null && other.productid != null) || (this.productid != null && !this.productid.equals(other.productid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Product[ productid=" + productid + " ]";
    }
    
}

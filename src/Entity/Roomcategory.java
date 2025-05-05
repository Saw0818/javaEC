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

@Entity
@Table(name = "ROOMCATEGORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Roomcategory.findAll", query = "SELECT r FROM ROOMCATEGORY r"),
    @NamedQuery(name = "Roomcategory.findByRoomid", query = "SELECT r FROM ROOMCATEGORY r WHERE r.ROOMID = :ROOMID"),
    @NamedQuery(name = "Roomcategory.findByRoomname", query = "SELECT r FROM ROOMCATEGORY r WHERE r.ROOMNAME = :ROOMNAME"),
    @NamedQuery(name = "Roomcategory.findByCategory", query = "SELECT r FROM ROOMCATEGORY r WHERE r.CATEGORY = :CATEGORY"),
    @NamedQuery(name = "Roomcategory.findByRoomimage", query = "SELECT r FROM ROOMCATEGORY r WHERE r.ROOMIMAGE = :ROOMIMAGE")})
public class Roomcategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "ROOMID")
    private String roomid;
    @Size(max = 100)
    @Column(name = "ROOMNAME")
    private String roomname;
    @Size(max = 20)
    @Column(name = "CATEGORY")
    private String category;
    @Size(max = 100)
    @Column(name = "ROOMIMAGE")
    private String roomimage;

    public Roomcategory() {
    }

    public Roomcategory(String roomid, String roomname, String category, String roomimage){
        this.roomid = roomid;
        this.roomname = roomname;
        this.category = category;
        this.roomimage = roomimage;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRoomimage() {
        return roomimage;
    }

    public void setRoomimage(String roomimage) {
        this.roomimage = roomimage;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomid != null ? roomid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Roomcategory)) {
            return false;
        }
        Roomcategory other = (Roomcategory) object;
        if ((this.roomid == null && other.roomid != null) || (this.roomid != null && !this.roomid.equals(other.roomid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Roomcategory[ roomid=" + roomid + " ]";
    }
    
}

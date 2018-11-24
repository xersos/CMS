package be.zwaldeck.zcms.repository.rmdbs.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "page_tbl")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDB {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "path", length = 1500, nullable = false)
    private String path;

    @Column(name = "published", nullable = false)
    private boolean published = false;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "parent_id", nullable = true)
    private PageDB parent;

    @OneToMany(mappedBy = "parent")
    private List<PageDB> children;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "site_id", nullable = false)
    private SiteDB site;

//    @OneToOne(cascade = CascadeType.MERGE, optional = false)
//    @JoinColumn(name = "root_node", nullable = false)
//    private NodeDB rootNode;

    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date();
    }
}

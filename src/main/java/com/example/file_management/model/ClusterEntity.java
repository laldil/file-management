package com.example.file_management.model;

import com.example.file_management.model.enums.ClusterAccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author aldi
 * @since 17.06.2024
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cluster")
public class ClusterEntity extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "bucket_name")
    private String bucketName;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_type")
    private ClusterAccessType accessType;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private UserEntity owner;

    /**
     * Capacity in megabytes
     */
    @Column(name = "capacity")
    private BigDecimal capacity;

    /**
     * Capacity in megabytes
     */
    @Column(name = "max_capacity")
    private BigDecimal maxCapacity;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "cluster_files",
            joinColumns = @JoinColumn(name = "cluster_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "file_id", referencedColumnName = "id")
    )
    private List<FileEntity> files = new ArrayList<>();

    @OneToMany(mappedBy = "cluster", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClusterUsersEntity> clusterUsers;

    public BigDecimal getFreeCapacity() {
        if (capacity.compareTo(maxCapacity) >= 0) {
            return BigDecimal.ZERO;
        }
        return maxCapacity.subtract(capacity);
    }

}

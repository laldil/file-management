package com.example.file_management.model;

import com.example.file_management.model.enums.MemberRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * @author aldi
 * @since 17.06.2024
 */

@Getter
@Setter
@Entity
@Table(name = "cluster_user")
public class ClusterUsersEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "cluster_id", referencedColumnName = "id")
    private ClusterEntity cluster;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role")
    private MemberRole memberRole;
}

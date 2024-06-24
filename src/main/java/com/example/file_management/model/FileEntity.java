package com.example.file_management.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author aldi
 * @since 17.06.2024
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "files")
public class FileEntity extends BaseEntity {

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "name")
    private String name;

    /**
     * Size in megabyte
     */
    @Column(name = "size")
    private BigDecimal size;

    @Column(name = "path")
    private String path;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private UserEntity owner;
}

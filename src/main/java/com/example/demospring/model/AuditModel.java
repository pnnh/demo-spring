package com.example.demospring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
 
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdTime", "updatedTime"},
        allowGetters = true
)
@Data
@Getter
@Setter
@ToString
public abstract class AuditModel implements Serializable {
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", nullable = false, updatable = false)
    @CreatedDate
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_time", nullable = false)
    @LastModifiedDate
    private Date updatedTime;

}
package com.example.keitpanel.common;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        Class<?> thisType = (this instanceof HibernateProxy hp)
                ? hp.getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        Class<?> thatType = (o instanceof HibernateProxy hp)
                ? hp.getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();

        if (!thisType.equals(thatType)) return false;

        return id != null && id.equals(((BaseEntity) o).getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }

}

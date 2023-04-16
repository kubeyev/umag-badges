package com.badgers.umag.modules.supplies.models.entities;

import com.badgers.umag.core.models.AuditModel;
import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.SQLDelete;
import com.badgers.umag.modules.sales.models.entites.Sale;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.badgers.umag.modules.supplies.models.entities.Supply.TABLE_NAME;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TABLE_NAME)
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(
        name = "seq",
        sequenceName = "s_" + TABLE_NAME,
        initialValue = 1,
        allocationSize = 1
)
public class Supply extends AuditModel {
    public static final String TABLE_NAME = "supply";

    @Column(nullable = false, name = "barcode")
    private Long barcode;

    @Column(nullable = false, name = "quantity")
    private Integer quantity;

    @Column(nullable = false, name = "price")
    private Integer price;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "time", nullable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime time = LocalDateTime.now();
}

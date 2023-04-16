package com.badgers.umag.modules.pointers.models.entities;

import com.badgers.umag.core.models.AuditModel;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.time.LocalDateTime;

import static com.badgers.umag.modules.pointers.models.entities.Pointer.TABLE_NAME;

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
public class Pointer extends AuditModel {
    public static final String TABLE_NAME = "pointer";

    @Column(nullable = false, name = "barcode")
    private Long barcode;

    @Column(nullable = false, name = "start_value")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startValue;

    @Column(nullable = false, name = "end_value")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endValue;

    @Column(nullable = false, name = "sum")
    private Integer sum;
}

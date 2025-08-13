package com.evan.wearesikgu.domain.colors;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull(message = "색상 이름은 필수입니다.")
    private String name;

    @Column(unique = true)
    @NotNull(message = "색상코드는 필수입니다.")
    private String hexCode;

}

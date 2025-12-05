package com.ESD.ecomm.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewUpdateDTO {

    private Long id;

    @Min(1)
    @Max(5)
    private Integer rating;

    @Size(max = 200)
    private String title;

    private String comment;

    private Boolean isApproved;
}

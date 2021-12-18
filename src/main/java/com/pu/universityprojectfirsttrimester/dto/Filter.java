package com.pu.universityprojectfirsttrimester.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Filter {
    private Integer teacherId;
    private Double priceLessThan;
    private Double priceGreaterThan;
}

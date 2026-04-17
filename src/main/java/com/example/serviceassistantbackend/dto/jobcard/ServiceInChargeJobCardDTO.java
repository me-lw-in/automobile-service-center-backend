package com.example.serviceassistantbackend.dto.jobcard;

import com.example.serviceassistantbackend.enums.JobCardStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceInChargeJobCardDTO {
    private Long id;
    private String jobCardNumber;
    private String vehicleNumber;
    private String customerName;
    private String serviceType;
    private BigDecimal price;
    private JobCardStatus status;
}

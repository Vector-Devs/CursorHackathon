package com.enterpriseservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "shipment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Shipment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String shipmentItem;
	private BigDecimal quantity;
	private String shipNumber;
	private String status;
	private Instant receiveDate;

	@ManyToMany(mappedBy = "shipments", fetch = FetchType.LAZY)
	@Builder.Default
	@JsonIgnoreProperties({"plants", "shipments"})
	private Set<Supplier> suppliers = new HashSet<>();

	@ManyToMany(mappedBy = "plantShipments", fetch = FetchType.LAZY)
	@Builder.Default
	@JsonIgnoreProperties({"plants", "suppliers"})
	private Set<Plant> plants = new HashSet<>();
}

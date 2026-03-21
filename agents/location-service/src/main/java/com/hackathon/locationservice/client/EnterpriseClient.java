package com.hackathon.locationservice.client;

import com.hackathon.locationservice.dto.PlantDto;
import com.hackathon.locationservice.dto.ShipmentDto;
import com.hackathon.locationservice.dto.SupplierDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class EnterpriseClient {

	private final RestClient restClient;

	public EnterpriseClient(@Qualifier("enterpriseRestClient") RestClient restClient) {
		this.restClient = restClient;
	}

	public List<PlantDto> listPlants() {
		List<PlantDto> result = restClient.get()
				.uri("/api/v1/plants")
				.retrieve()
				.body(new ParameterizedTypeReference<>() {});
		return result != null ? result : List.of();
	}

	public List<SupplierDto> listSuppliers() {
		List<SupplierDto> result = restClient.get()
				.uri("/api/v1/suppliers")
				.retrieve()
				.body(new ParameterizedTypeReference<>() {});
		return result != null ? result : List.of();
	}

	public List<ShipmentDto> listShipments() {
		List<ShipmentDto> result = restClient.get()
				.uri("/api/v1/shipments")
				.retrieve()
				.body(new ParameterizedTypeReference<>() {});
		return result != null ? result : List.of();
	}
}

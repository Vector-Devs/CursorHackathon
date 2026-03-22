import type { Plant, Supplier, Shipment, SupplyChainRiskReportResponse, ProbabilityResponse } from './types';

async function json<T>(path: string): Promise<T> {
  const res = await fetch(path);
  if (!res.ok) {
    const text = await res.text();
    throw new Error(`${res.status} ${path}${text ? `: ${text.slice(0, 200)}` : ''}`);
  }
  return res.json() as Promise<T>;
}

/** Master data — enterpriseservice (:8085). */
export const enterpriseApi = {
  listPlants: () => json<Plant[]>('/api/v1/plants'),
  listSuppliers: () => json<Supplier[]>('/api/v1/suppliers'),
  listShipments: () => json<Shipment[]>('/api/v1/shipments'),
};

/**
 * Event probability — probability-service (:8097).
 * Pushed via WebSocket; REST fallback returns cached value.
 */
export const probabilityApi = {
  getProbabilities: () => json<ProbabilityResponse>('/api/probability'),
};

/**
 * Simulation / portfolio analytics — supply-chain-risk-agent (:8094).
 * Composes enterprise + upstream reasoning internally; the UI does not call reasoning-agent directly.
 */
export const SIMULATION_REPORT_POLL_MS = 2000;

export const simulationApi = {
  supplyChainRiskReport: () =>
    json<SupplyChainRiskReportResponse>('/api/agent/supply-chain-risk-report'),
};

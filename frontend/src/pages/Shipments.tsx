import { Layout } from '../components/Layout';
import { HeaderBar } from '../components/HeaderBar';
import { KPICard } from '../components/KPICard';

const SHIPMENTS = [
  {
    shipmentNo: 'SHP-2024-001',
    item: 'Sodium Hydroxide',
    quantity: '5,000 kg',
    supplier: 'ChemCorp Asia',
    plant: 'Frankfurt',
    shipNumber: 'VSL-88421',
    status: 'In Transit' as const,
    receiveDate: '2024-03-25',
  },
  {
    shipmentNo: 'SHP-2024-002',
    item: 'Ethylene Glycol',
    quantity: '12,000 L',
    supplier: 'Dow Chemical Europe',
    plant: 'Shanghai',
    shipNumber: 'VSL-77234',
    status: 'Delivered' as const,
    receiveDate: '2024-03-20',
  },
  {
    shipmentNo: 'SHP-2024-003',
    item: 'Ammonia Solution',
    quantity: '8,500 kg',
    supplier: 'BASF',
    plant: 'Houston',
    shipNumber: 'VSL-99102',
    status: 'Delayed' as const,
    receiveDate: '2024-03-28',
  },
  {
    shipmentNo: 'SHP-2024-004',
    item: 'Titanium Dioxide',
    quantity: '15,000 kg',
    supplier: 'ChemCorp Asia',
    plant: 'Rotterdam',
    shipNumber: 'VSL-55321',
    status: 'In Transit' as const,
    receiveDate: '2024-03-30',
  },
  {
    shipmentNo: 'SHP-2024-005',
    item: 'Sulfuric Acid',
    quantity: '20,000 L',
    supplier: 'Solvay',
    plant: 'Frankfurt',
    shipNumber: 'VSL-44218',
    status: 'Pending' as const,
    receiveDate: '2024-04-02',
  },
  {
    shipmentNo: 'SHP-2024-006',
    item: 'Methanol',
    quantity: '10,000 L',
    supplier: 'Methanex',
    plant: 'Shanghai',
    shipNumber: 'VSL-66543',
    status: 'Delivered' as const,
    receiveDate: '2024-03-18',
  },
];

const statusColors: Record<string, string> = {
  'In Transit': 'var(--info)',
  Delivered: 'var(--success)',
  Delayed: 'var(--error)',
  Pending: 'var(--warning)',
};

export function Shipments() {
  return (
    <Layout>
      <HeaderBar
        title="SHIPMENTS"
        subtitle="Track shipments, deliveries, and logistics across your supply chain"
        showLive={false}
      />
      <div
        style={{
          flex: 1,
          display: 'flex',
          flexDirection: 'column',
          gap: 20,
          padding: 24,
          overflow: 'auto',
        }}
      >
        <div
          style={{
            display: 'flex',
            gap: 16,
            flexWrap: 'wrap',
          }}
        >
          <KPICard
            label="TOTAL SHIPMENTS"
            value="24"
            valueColor="info"
          />
          <KPICard
            label="IN TRANSIT"
            value="12"
            valueColor="primary"
          />
          <KPICard
            label="ON TIME RATE"
            value="91%"
            valueColor="success"
          />
          <KPICard
            label="DELAYED"
            value="3"
            valueColor="warning"
          />
        </div>

        <div
          style={{
            flex: 1,
            background: 'var(--bg-card)',
            borderRadius: 4,
            overflow: 'hidden',
            display: 'flex',
            flexDirection: 'column',
            minHeight: 0,
          }}
        >
          <div
            style={{
              padding: '12px 16px',
              borderBottom: '1px solid var(--border)',
            }}
          >
            <span
              style={{
                fontFamily: 'var(--font-mono)',
                fontSize: 11,
                fontWeight: 600,
                letterSpacing: 2,
                color: 'var(--text-tertiary)',
              }}
            >
              SHIPMENT DIRECTORY
            </span>
          </div>
          <div style={{ flex: 1, overflow: 'auto' }}>
            <table
              style={{
                width: '100%',
                borderCollapse: 'collapse',
                fontFamily: 'var(--font-sans)',
                fontSize: 13,
              }}
            >
              <thead>
                <tr>
                  <th
                    style={{
                      padding: '12px 16px',
                      textAlign: 'left',
                      fontFamily: 'var(--font-mono)',
                      fontSize: 10,
                      fontWeight: 600,
                      letterSpacing: 1.5,
                      color: 'var(--text-tertiary)',
                      borderBottom: '1px solid var(--border)',
                    }}
                  >
                    SHIPMENT NO.
                  </th>
                  <th
                    style={{
                      padding: '12px 16px',
                      textAlign: 'left',
                      fontFamily: 'var(--font-mono)',
                      fontSize: 10,
                      fontWeight: 600,
                      letterSpacing: 1.5,
                      color: 'var(--text-tertiary)',
                      borderBottom: '1px solid var(--border)',
                    }}
                  >
                    SHIPMENT ITEM
                  </th>
                  <th
                    style={{
                      padding: '12px 16px',
                      textAlign: 'left',
                      fontFamily: 'var(--font-mono)',
                      fontSize: 10,
                      fontWeight: 600,
                      letterSpacing: 1.5,
                      color: 'var(--text-tertiary)',
                      borderBottom: '1px solid var(--border)',
                    }}
                  >
                    QUANTITY
                  </th>
                  <th
                    style={{
                      padding: '12px 16px',
                      textAlign: 'left',
                      fontFamily: 'var(--font-mono)',
                      fontSize: 10,
                      fontWeight: 600,
                      letterSpacing: 1.5,
                      color: 'var(--text-tertiary)',
                      borderBottom: '1px solid var(--border)',
                    }}
                  >
                    SUPPLIER
                  </th>
                  <th
                    style={{
                      padding: '12px 16px',
                      textAlign: 'left',
                      fontFamily: 'var(--font-mono)',
                      fontSize: 10,
                      fontWeight: 600,
                      letterSpacing: 1.5,
                      color: 'var(--text-tertiary)',
                      borderBottom: '1px solid var(--border)',
                    }}
                  >
                    PLANT
                  </th>
                  <th
                    style={{
                      padding: '12px 16px',
                      textAlign: 'left',
                      fontFamily: 'var(--font-mono)',
                      fontSize: 10,
                      fontWeight: 600,
                      letterSpacing: 1.5,
                      color: 'var(--text-tertiary)',
                      borderBottom: '1px solid var(--border)',
                    }}
                  >
                    SHIP NUMBER
                  </th>
                  <th
                    style={{
                      padding: '12px 16px',
                      textAlign: 'left',
                      fontFamily: 'var(--font-mono)',
                      fontSize: 10,
                      fontWeight: 600,
                      letterSpacing: 1.5,
                      color: 'var(--text-tertiary)',
                      borderBottom: '1px solid var(--border)',
                    }}
                  >
                    STATUS
                  </th>
                  <th
                    style={{
                      padding: '12px 16px',
                      textAlign: 'left',
                      fontFamily: 'var(--font-mono)',
                      fontSize: 10,
                      fontWeight: 600,
                      letterSpacing: 1.5,
                      color: 'var(--text-tertiary)',
                      borderBottom: '1px solid var(--border)',
                    }}
                  >
                    RECEIVE DATE
                  </th>
                </tr>
              </thead>
              <tbody>
                {SHIPMENTS.map((row) => (
                  <tr
                    key={row.shipmentNo}
                    style={{ borderBottom: '1px solid var(--border)' }}
                  >
                    <td
                      style={{
                        padding: '12px 16px',
                        fontFamily: 'var(--font-mono)',
                        fontSize: 12,
                      }}
                    >
                      <span
                        role="button"
                        tabIndex={0}
                        style={{
                          color: 'var(--accent)',
                          textDecoration: 'none',
                          fontWeight: 600,
                          cursor: 'pointer',
                        }}
                        onMouseOver={(e) => {
                          e.currentTarget.style.textDecoration = 'underline';
                        }}
                        onMouseOut={(e) => {
                          e.currentTarget.style.textDecoration = 'none';
                        }}
                        onFocus={(e) => {
                          e.currentTarget.style.textDecoration = 'underline';
                        }}
                        onBlur={(e) => {
                          e.currentTarget.style.textDecoration = 'none';
                        }}
                      >
                        {row.shipmentNo}
                      </span>
                    </td>
                    <td
                      style={{
                        padding: '12px 16px',
                        color: 'var(--text-primary)',
                        fontWeight: 500,
                      }}
                    >
                      {row.item}
                    </td>
                    <td
                      style={{
                        padding: '12px 16px',
                        color: 'var(--text-secondary)',
                        fontFamily: 'var(--font-mono)',
                        fontSize: 11,
                      }}
                    >
                      {row.quantity}
                    </td>
                    <td
                      style={{
                        padding: '12px 16px',
                        color: 'var(--text-secondary)',
                      }}
                    >
                      {row.supplier}
                    </td>
                    <td
                      style={{
                        padding: '12px 16px',
                        color: 'var(--text-secondary)',
                      }}
                    >
                      {row.plant}
                    </td>
                    <td
                      style={{
                        padding: '12px 16px',
                        color: 'var(--text-secondary)',
                        fontFamily: 'var(--font-mono)',
                        fontSize: 11,
                      }}
                    >
                      {row.shipNumber}
                    </td>
                    <td style={{ padding: '12px 16px' }}>
                      <span
                        style={{
                          padding: '4px 10px',
                          borderRadius: 9999,
                          fontFamily: 'var(--font-mono)',
                          fontSize: 10,
                          fontWeight: 600,
                          background: `${statusColors[row.status]}20`,
                          color: statusColors[row.status],
                        }}
                      >
                        {row.status}
                      </span>
                    </td>
                    <td
                      style={{
                        padding: '12px 16px',
                        color: 'var(--text-secondary)',
                        fontFamily: 'var(--font-mono)',
                        fontSize: 11,
                      }}
                    >
                      {row.receiveDate}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </Layout>
  );
}

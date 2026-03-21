import { Layout } from '../components/Layout';
import { HeaderBar } from '../components/HeaderBar';
import { KPICard } from '../components/KPICard';

const SUPPLIERS = [
  { name: 'Port Jebel Ali', type: 'PORT', region: 'UAE', status: 'active', orders: 156 },
  { name: 'Port of Singapore', type: 'PORT', region: 'Singapore', status: 'active', orders: 234 },
  { name: 'Port of Suez', type: 'PORT', region: 'Egypt', status: 'active', orders: 89 },
  { name: 'Khalifa Port', type: 'PORT', region: 'UAE', status: 'active', orders: 112 },
  { name: 'Port of Cape Town', type: 'PORT', region: 'South Africa', status: 'active', orders: 67 },
  { name: 'Port of Cristobal', type: 'PORT', region: 'Panama', status: 'active', orders: 98 },
  { name: 'Port of Khasab', type: 'PORT', region: 'Oman', status: 'active', orders: 45 },
  { name: 'Khalifa Bin Salman Port', type: 'PORT', region: 'Bahrain', status: 'active', orders: 34 },
  { name: 'Dubai City Hub', type: 'CITY', region: 'UAE', status: 'active', orders: 201 },
  { name: 'Singapore City Hub', type: 'CITY', region: 'Singapore', status: 'active', orders: 278 },
  { name: 'Frankfurt Logistics', type: 'CITY', region: 'Germany', status: 'active', orders: 189 },
  { name: 'Houston Distribution', type: 'CITY', region: 'USA', status: 'active', orders: 145 },
];

export function Suppliers() {
  return (
    <Layout>
      <HeaderBar
        title="SUPPLIERS"
        showLive
        searchPlaceholder="Search suppliers..."
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
          <KPICard label="ACTIVE SUPPLIERS" value="847" valueColor="primary" />
          <KPICard label="PORTS" value="312" valueColor="primary" />
          <KPICard label="CITY HUBS" value="535" valueColor="primary" />
          <KPICard label="PENDING ORDERS" value="1,648" valueColor="primary" />
        </div>

        <div
          style={{
            flex: 1,
            background: 'var(--bg-card)',
            borderRadius: 4,
            overflow: 'hidden',
            display: 'flex',
            flexDirection: 'column',
            minWidth: 0,
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
              SUPPLIER REGISTRY
            </span>
          </div>
          <div
            style={{
              flex: 1,
              overflow: 'auto',
            }}
          >
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
                    TYPE
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
                    REGION
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
                      textAlign: 'right',
                      fontFamily: 'var(--font-mono)',
                      fontSize: 10,
                      fontWeight: 600,
                      letterSpacing: 1.5,
                      color: 'var(--text-tertiary)',
                      borderBottom: '1px solid var(--border)',
                    }}
                  >
                    ORDERS (30D)
                  </th>
                </tr>
              </thead>
              <tbody>
                {SUPPLIERS.map((supplier) => (
                  <tr
                    key={supplier.name}
                    style={{
                      borderBottom: '1px solid var(--border)',
                    }}
                  >
                    <td
                      style={{
                        padding: '12px 16px',
                        color: 'var(--text-primary)',
                        fontWeight: 600,
                      }}
                    >
                      {supplier.name}
                    </td>
                    <td
                      style={{
                        padding: '12px 16px',
                        color: 'var(--text-secondary)',
                        fontFamily: 'var(--font-mono)',
                        fontSize: 11,
                      }}
                    >
                      {supplier.type}
                    </td>
                    <td
                      style={{
                        padding: '12px 16px',
                        color: 'var(--text-secondary)',
                        fontFamily: 'var(--font-mono)',
                        fontSize: 11,
                      }}
                    >
                      {supplier.region}
                    </td>
                    <td
                      style={{
                        padding: '12px 16px',
                      }}
                    >
                      <span
                        style={{
                          padding: '2px 8px',
                          borderRadius: 4,
                          fontFamily: 'var(--font-mono)',
                          fontSize: 10,
                          fontWeight: 600,
                          background: 'var(--success-dim)',
                          color: 'var(--success)',
                        }}
                      >
                        {supplier.status.toUpperCase()}
                      </span>
                    </td>
                    <td
                      style={{
                        padding: '12px 16px',
                        textAlign: 'right',
                        color: 'var(--text-secondary)',
                        fontFamily: 'var(--font-mono)',
                        fontSize: 11,
                      }}
                    >
                      {supplier.orders}
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

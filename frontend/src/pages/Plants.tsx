import { Layout } from '../components/Layout';
import { HeaderBar } from '../components/HeaderBar';
import { KPICard } from '../components/KPICard';
import { WorldMap } from '../components/WorldMap';

const PLANTS = [
  { name: 'Frankfurt', region: 'Europe', status: 'active', capacity: '12,500 MT/yr' },
  { name: 'Shanghai', region: 'APAC', status: 'active', capacity: '18,200 MT/yr' },
  { name: 'Houston', region: 'Americas', status: 'active', capacity: '15,800 MT/yr' },
  { name: 'Mumbai', region: 'APAC', status: 'active', capacity: '9,400 MT/yr' },
  { name: 'São Paulo', region: 'Americas', status: 'active', capacity: '11,200 MT/yr' },
  { name: 'Singapore', region: 'APAC', status: 'active', capacity: '8,900 MT/yr' },
  { name: 'Dubai', region: 'MEA', status: 'active', capacity: '7,600 MT/yr' },
  { name: 'Rotterdam', region: 'Europe', status: 'active', capacity: '14,100 MT/yr' },
  { name: 'Tokyo', region: 'APAC', status: 'maintenance', capacity: '6,200 MT/yr' },
  { name: 'Mexico City', region: 'Americas', status: 'active', capacity: '5,800 MT/yr' },
  { name: 'Cape Town', region: 'Africa', status: 'active', capacity: '4,500 MT/yr' },
  { name: 'Sydney', region: 'APAC', status: 'active', capacity: '3,200 MT/yr' },
];

export function Plants() {
  return (
    <Layout>
      <HeaderBar
        title="PLANTS"
        showLive
        searchPlaceholder="Search plants..."
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
          <KPICard label="TOTAL PLANTS" value="12" valueColor="primary" />
          <KPICard label="ACTIVE" value="11" valueColor="success" />
          <KPICard label="MAINTENANCE" value="1" valueColor="error" />
          <KPICard label="TOTAL CAPACITY" value="117.6k MT/yr" valueColor="primary" />
        </div>

        <div
          style={{
            flex: 1,
            display: 'flex',
            gap: 16,
            minHeight: 0,
          }}
        >
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
                PLANT REGISTRY
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
                      CAPACITY
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {PLANTS.map((plant) => (
                    <tr
                      key={plant.name}
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
                        {plant.name}
                      </td>
                      <td
                        style={{
                          padding: '12px 16px',
                          color: 'var(--text-secondary)',
                          fontFamily: 'var(--font-mono)',
                          fontSize: 11,
                        }}
                      >
                        {plant.region}
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
                            background:
                              plant.status === 'active'
                                ? 'var(--success-dim)'
                                : 'var(--error-dim)',
                            color:
                              plant.status === 'active'
                                ? 'var(--success)'
                                : 'var(--error)',
                          }}
                        >
                          {plant.status.toUpperCase()}
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
                        {plant.capacity}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          <div style={{ width: 400, minWidth: 400 }}>
            <WorldMap title="PLANT LOCATIONS" />
          </div>
        </div>
      </div>
    </Layout>
  );
}

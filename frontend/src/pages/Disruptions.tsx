import { Filter, FileDown } from 'lucide-react';
import { Layout } from '../components/Layout';
import { HeaderBar } from '../components/HeaderBar';
import { KPICard } from '../components/KPICard';
import { SeverityBreakdown } from '../components/SeverityBreakdown';
import { RecentActivity } from '../components/RecentActivity';

const DISRUPTIONS = [
  {
    incident: 'Shanghai Port Congestion',
    type: 'Logistics',
    severity: 'Critical' as const,
    status: 'Investigating',
    statusColor: 'var(--warning)',
    impact: '$840K',
  },
  {
    incident: 'European Rail Strike',
    type: 'Transport',
    severity: 'Critical' as const,
    status: 'Mitigating',
    statusColor: 'var(--info)',
    impact: '$620K',
  },
  {
    incident: 'TiO2 Raw Material Shortage',
    type: 'Supply',
    severity: 'Critical' as const,
    status: 'Escalated',
    statusColor: 'var(--error)',
    impact: '$940K',
  },
  {
    incident: 'Mumbai Monsoon Delays',
    type: 'Weather',
    severity: 'High' as const,
    status: 'Monitoring',
    statusColor: 'var(--accent)',
    impact: '$310K',
  },
  {
    incident: 'Customs Clearance Backlog',
    type: 'Regulatory',
    severity: 'High' as const,
    status: 'Mitigating',
    statusColor: 'var(--info)',
    impact: '$180K',
  },
  {
    incident: 'Solvent Supplier Bankruptcy',
    type: 'Supplier',
    severity: 'High' as const,
    status: 'Resolved',
    statusColor: 'var(--success)',
    impact: '$150K',
  },
  {
    incident: 'Houston Plant Power Outage',
    type: 'Facility',
    severity: 'Medium' as const,
    status: 'Resolved',
    statusColor: 'var(--success)',
    impact: '$85K',
  },
];

const severityColors = {
  Critical: 'var(--error)',
  High: 'var(--warning)',
  Medium: 'var(--success)',
};

export function Disruptions() {
  return (
    <Layout>
      <HeaderBar
        title="DISRUPTIONS"
        subtitle="Active supply chain disruption tracking and incident response"
        showLive={false}
        rightContent={
          <div style={{ display: 'flex', gap: 12, alignItems: 'center' }}>
            <button
              type="button"
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: 8,
                padding: '8px 16px',
                background: 'var(--bg-card)',
                border: '1px solid var(--border)',
                borderRadius: 6,
                color: 'var(--text-secondary)',
                fontFamily: 'var(--font-sans)',
                fontSize: 13,
                fontWeight: 500,
                cursor: 'pointer',
              }}
            >
              <Filter size={16} />
              Filter
            </button>
            <button
              type="button"
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: 8,
                padding: '8px 16px',
                background: 'var(--accent)',
                border: 'none',
                borderRadius: 6,
                color: 'var(--bg-page)',
                fontFamily: 'var(--font-sans)',
                fontSize: 13,
                fontWeight: 600,
                cursor: 'pointer',
              }}
            >
              <FileDown size={16} />
              Export Report
            </button>
          </div>
        }
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
            label="ACTIVE DISRUPTIONS"
            value="7"
            valueColor="error"
            subtext="+2 this week"
            subtextColor="error"
          />
          <KPICard
            label="CRITICAL SEVERITY"
            value={
              <span style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                {'3'}
                <span
                  style={{
                    width: 8,
                    height: 8,
                    borderRadius: '50%',
                    background: 'var(--error)',
                    animation: 'pulse 2s infinite',
                  }}
                />
              </span>
            }
            valueColor="primary"
          />
          <KPICard
            label="AVG RESOLUTION TIME"
            value="4.2d"
            valueColor="primary"
            subtext="-0.8d vs avg"
            subtextColor="success"
          />
          <KPICard
            label="EST. REVENUE IMPACT"
            value="$2.4M"
            valueColor="info"
            subtext="monthly exposure"
            subtextColor="primary"
          />
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
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
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
                ACTIVE DISRUPTION LOG
              </span>
              <div
                style={{
                  background: 'var(--error-dim)',
                  borderRadius: 4,
                  padding: '2px 8px',
                }}
              >
                <span
                  style={{
                    fontFamily: 'var(--font-mono)',
                    fontSize: 11,
                    fontWeight: 700,
                    color: 'var(--error)',
                  }}
                >
                  7 Active
                </span>
              </div>
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
                      INCIDENT
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
                      SEVERITY
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
                      IMPACT
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {DISRUPTIONS.map((row) => (
                    <tr
                      key={row.incident}
                      style={{ borderBottom: '1px solid var(--border)' }}
                    >
                      <td
                        style={{
                          padding: '12px 16px',
                          color: 'var(--text-primary)',
                          fontWeight: 600,
                        }}
                      >
                        {row.incident}
                      </td>
                      <td
                        style={{
                          padding: '12px 16px',
                          color: 'var(--text-secondary)',
                          fontFamily: 'var(--font-mono)',
                          fontSize: 11,
                        }}
                      >
                        {row.type}
                      </td>
                      <td style={{ padding: '12px 16px' }}>
                        <span
                          style={{
                            padding: '2px 8px',
                            borderRadius: 4,
                            fontFamily: 'var(--font-mono)',
                            fontSize: 10,
                            fontWeight: 600,
                            background: `${severityColors[row.severity]}20`,
                            color: severityColors[row.severity],
                          }}
                        >
                          {row.severity}
                        </span>
                      </td>
                      <td
                        style={{
                          padding: '12px 16px',
                          fontFamily: 'var(--font-mono)',
                          fontSize: 11,
                          color: row.statusColor,
                        }}
                      >
                        {row.status}
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
                        {row.impact}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          <div
            style={{
              width: 320,
              minWidth: 320,
              display: 'flex',
              flexDirection: 'column',
              gap: 16,
            }}
          >
            <SeverityBreakdown />
            <RecentActivity />
          </div>
        </div>
      </div>
    </Layout>
  );
}

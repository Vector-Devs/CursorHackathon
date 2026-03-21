import { NavLink } from 'react-router-dom';
import { Home, BarChart3, Factory, Truck, TriangleAlert, Package, Settings } from 'lucide-react';

const navItems = [
  { icon: Home, label: 'Dashboard', to: '/' },
  { icon: BarChart3, label: 'Predictions & Simulation', to: '/predictions' },
  { icon: Factory, label: 'Plants', to: '/plants' },
  { icon: Truck, label: 'Suppliers', to: '/suppliers' },
  { icon: TriangleAlert, label: 'Disruptions', to: '/disruptions' },
  { icon: Package, label: 'Shipments', to: '/shipments' },
  { icon: Settings, label: 'Settings', to: '/settings' },
];

export function Sidebar() {
  return (
    <aside
      style={{
        width: 240,
        minWidth: 240,
        height: '100%',
        background: 'var(--bg-surface)',
        borderRight: '1px solid var(--border)',
        display: 'flex',
        flexDirection: 'column',
        gap: 32,
        padding: '32px 20px 20px 20px',
      }}
    >
      <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
        <span
          style={{
            fontFamily: 'var(--font-mono)',
            fontSize: 14,
            fontWeight: 700,
            letterSpacing: 2,
            color: 'var(--accent)',
          }}
        >
          BSS CHEMICALS
        </span>
        <span
          style={{
            fontFamily: 'var(--font-mono)',
            fontSize: 9,
            fontWeight: 500,
            letterSpacing: 1.5,
            color: 'var(--text-secondary)',
          }}
        >
          SUPPLY CHAIN CONTROL TOWER
        </span>
      </div>

      <nav style={{ display: 'flex', flexDirection: 'column', gap: 4, flex: 1, minHeight: 0 }}>
        {navItems.map(({ icon: Icon, label, to }) => (
          <NavLink
            key={to}
            to={to}
            end={to === '/'}
            style={({ isActive }) => ({
              display: 'flex',
              alignItems: 'center',
              gap: 10,
              padding: '10px 12px',
              borderRadius: 6,
              background: isActive ? 'var(--accent)' : 'transparent',
              cursor: 'pointer',
              textDecoration: 'none',
            })}
          >
            {({ isActive }) => (
              <>
                <Icon
                  size={18}
                  color={isActive ? 'var(--bg-page)' : 'var(--text-tertiary)'}
                  strokeWidth={2}
                />
                <span
                  style={{
                    fontFamily: 'var(--font-sans)',
                    fontSize: 13,
                    fontWeight: isActive ? 600 : 500,
                    color: isActive ? 'var(--bg-page)' : 'var(--text-secondary)',
                  }}
                >
                  {label}
                </span>
              </>
            )}
          </NavLink>
        ))}
      </nav>
      <div
        style={{
          paddingTop: 16,
          borderTop: '1px solid var(--border)',
          display: 'flex',
          flexDirection: 'column',
          gap: 4,
        }}
      >
        <span
          style={{
            fontFamily: 'var(--font-mono)',
            fontSize: 9,
            fontWeight: 600,
            letterSpacing: 1.5,
            color: 'var(--text-tertiary)',
          }}
        >
          AI ENGINE STATUS
        </span>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <span
            style={{
              width: 6,
              height: 6,
              borderRadius: '50%',
              background: 'var(--success)',
            }}
          />
          <span
            style={{
              fontFamily: 'var(--font-mono)',
              fontSize: 10,
              fontWeight: 500,
              color: 'var(--success)',
            }}
          >
            Online - Model v3.2.1
          </span>
        </div>
      </div>
    </aside>
  );
}

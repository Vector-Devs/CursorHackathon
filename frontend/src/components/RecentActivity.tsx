const ACTIVITY_ITEMS = [
  {
    color: 'var(--error)',
    text: 'Shanghai Port - Status escalated to Critical',
    time: '2 hours ago',
  },
  {
    color: 'var(--warning)',
    text: 'TiO2 Shortage - Alternate supplier identified',
    time: '5 hours ago',
  },
  {
    color: 'var(--success)',
    text: 'Houston Power Outage - Resolved, ops normal',
    time: '1 day ago',
  },
  {
    color: 'var(--info)',
    text: 'Rail Strike - Mitigation plan approved by ops',
    time: '1 day ago',
  },
];

export function RecentActivity() {
  return (
    <div
      style={{
        background: 'var(--bg-card)',
        borderRadius: 4,
        padding: 16,
        display: 'flex',
        flexDirection: 'column',
        gap: 12,
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
        RECENT ACTIVITY
      </span>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 0 }}>
        {ACTIVITY_ITEMS.map((item, i) => (
          <div
            key={i}
            style={{
              display: 'flex',
              gap: 12,
              padding: '12px 0',
              borderBottom:
                i < ACTIVITY_ITEMS.length - 1 ? '1px solid var(--border)' : 'none',
            }}
          >
            <span
              style={{
                width: 8,
                height: 8,
                borderRadius: '50%',
                background: item.color,
                flexShrink: 0,
                marginTop: 6,
              }}
            />
            <div style={{ flex: 1, minWidth: 0 }}>
              <span
                style={{
                  fontFamily: 'var(--font-sans)',
                  fontSize: 13,
                  fontWeight: 500,
                  color: 'var(--text-primary)',
                }}
              >
                {item.text}
              </span>
              <div
                style={{
                  fontFamily: 'var(--font-mono)',
                  fontSize: 10,
                  fontWeight: 500,
                  color: 'var(--text-muted)',
                  marginTop: 2,
                }}
              >
                {item.time}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

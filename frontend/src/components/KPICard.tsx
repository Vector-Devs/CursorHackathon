import type { ReactNode } from 'react';

interface KPICardProps {
  label: string;
  value: string | number | ReactNode;
  valueColor?: 'primary' | 'success' | 'error' | 'warning' | 'info';
  subtext?: string;
  subtextColor?: 'primary' | 'success' | 'error' | 'warning' | 'info';
}

export function KPICard({
  label,
  value,
  valueColor = 'primary',
  subtext,
  subtextColor = 'primary',
}: Readonly<KPICardProps>) {
  const colorMap = {
    primary: 'var(--text-primary)',
    success: 'var(--success)',
    error: 'var(--error)',
    warning: 'var(--warning)',
    info: 'var(--info)',
  };

  return (
    <div
      style={{
        flex: 1,
        minWidth: 0,
        background: 'var(--bg-card)',
        borderRadius: 4,
        padding: '16px 20px',
        display: 'flex',
        flexDirection: 'column',
        gap: 8,
      }}
    >
      <span
        style={{
          fontFamily: 'var(--font-sans)',
          fontSize: 11,
          fontWeight: 600,
          letterSpacing: 2,
          color: 'var(--text-tertiary)',
        }}
      >
        {label}
      </span>
      <span
        style={{
          fontFamily: 'var(--font-mono)',
          fontSize: 32,
          fontWeight: 700,
          color: colorMap[valueColor],
        }}
      >
        {value}
      </span>
      {subtext && (
        <span
          style={{
            fontFamily: 'var(--font-mono)',
            fontSize: 11,
            fontWeight: 500,
            color: colorMap[subtextColor],
          }}
        >
          {subtext}
        </span>
      )}
    </div>
  );
}

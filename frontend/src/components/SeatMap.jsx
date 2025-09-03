import React from 'react';

export default function SeatMap({ seats = [], selectedSeatNumbers = [], onToggle }) {
  const selectedSet = React.useMemo(() => new Set(selectedSeatNumbers), [selectedSeatNumbers]);
  const normalized = React.useMemo(() => seats.map(s => ({
    ...s,
    seatNumber: (s.seatNumber || '').toString().toUpperCase(),
  })), [seats]);

  return (
    <>
      <div className="d-flex gap-3 mb-2 align-items-center">
        <Legend color="#ffffff" label="Available" border="#0d6efd" />
        <Legend color="#0d6efd" label="Selected" />
        <Legend color="#d6d6d6" label="Booked" />
      </div>
      <div className="d-grid" style={{ gridTemplateColumns: 'repeat(4, 56px)', gap: '8px' }}>
        {normalized.map((s) => {
          const isSelected = selectedSet.has(s.seatNumber);
          const baseStyle = {
            height: 44, borderRadius: 6, fontWeight: 600
          };
          const style = s.booked
            ? { background: '#d6d6d6', color: '#777', cursor: 'not-allowed', ...baseStyle }
            : isSelected
            ? { background: '#0d6efd', color: '#fff', ...baseStyle }
            : { background: '#fff', color: '#0d6efd', border: '1px solid #0d6efd', ...baseStyle };
          return (
            <button
              key={s.seatNumber}
              className="btn btn-sm"
              style={style}
              disabled={s.booked}
              aria-pressed={isSelected}
              onClick={() => onToggle?.(s)}
            >
              {s.seatNumber}
            </button>
          );
        })}
      </div>
    </>
  );
}

function Legend({ color, label, border }) {
  return (
    <div className="d-flex align-items-center gap-2">
      <span style={{ display: 'inline-block', width: 18, height: 18, background: color, border: border? `1px solid ${border}`: 'none', borderRadius: 3 }} />
      <small className="text-muted">{label}</small>
    </div>
  );
}

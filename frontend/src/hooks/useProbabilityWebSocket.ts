import { useCallback, useEffect, useRef, useState } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import type { ProbabilityResponse } from '../api/types';

function buildWebSocketUrl(): string {
  const base = typeof window === 'undefined' ? '' : window.location.origin;
  return `${base}/ws`;
}

export function useProbabilityWebSocket(): {
  data: ProbabilityResponse | null;
  loading: boolean;
  error: string | null;
} {
  const [data, setData] = useState<ProbabilityResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const clientRef = useRef<Client | null>(null);

  const connect = useCallback(() => {
    const url = buildWebSocketUrl();
    const client = new Client({
      webSocketFactory: () => new SockJS(url) as unknown as WebSocket,
      reconnectDelay: 5000,
      heartbeatIncoming: 10000,
      heartbeatOutgoing: 10000,
      onConnect: () => {
        setError(null);
        setLoading(false);
        client.subscribe('/topic/probability', (message) => {
          try {
            const body = JSON.parse(message.body) as ProbabilityResponse;
            setData(body);
            setError(null);
          } catch {
            setError('Invalid probability message');
          }
        });
      },
      onStompError: (frame) => {
        setError(frame.headers?.message ?? 'WebSocket error');
      },
      onWebSocketClose: () => {
        setLoading(true);
      },
    });

    client.activate();
    clientRef.current = client;
  }, []);

  useEffect(() => {
    connect();
    return () => {
      clientRef.current?.deactivate();
      clientRef.current = null;
    };
  }, [connect]);

  return { data, loading, error };
}

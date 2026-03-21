import { useCallback, useEffect, useRef, useState } from 'react';

export type UseAsyncOptions = {
  /**
   * When set, re-runs the loader on this interval. Background refreshes keep showing the last
   * successful data (no full-page loading flash).
   */
  pollIntervalMs?: number;
};

export function useAsync<T>(
  loader: () => Promise<T>,
  deps: unknown[] = [],
  options?: UseAsyncOptions
): { data: T | null; loading: boolean; error: string | null; reload: () => void } {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [tick, setTick] = useState(0);
  const loaderRef = useRef(loader);
  loaderRef.current = loader;

  const pollMs = options?.pollIntervalMs ?? 0;
  const depsKey = JSON.stringify(deps);
  const prevDepsKeyRef = useRef<string | null>(null);
  const dataRef = useRef<T | null>(null);
  dataRef.current = data;

  const reload = useCallback(() => setTick((t) => t + 1), []);

  useEffect(() => {
    if (pollMs > 0) {
      const id = setInterval(() => setTick((t) => t + 1), pollMs);
      return () => clearInterval(id);
    }
    return undefined;
  }, [pollMs]);

  useEffect(() => {
    let cancelled = false;
    const prevKey = prevDepsKeyRef.current;
    const depsChanged = prevKey !== null && prevKey !== depsKey;
    prevDepsKeyRef.current = depsKey;

    const silentRefresh = pollMs > 0 && dataRef.current !== null && !depsChanged;
    if (!silentRefresh) {
      setLoading(true);
    }
    setError(null);
    loaderRef
      .current()
      .then((d) => {
        if (!cancelled) {
          setData(d);
          setError(null);
        }
      })
      .catch((e: unknown) => {
        if (!cancelled) setError(e instanceof Error ? e.message : String(e));
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });
    return () => {
      cancelled = true;
    };
  }, [tick, pollMs, depsKey]);

  return { data, loading, error, reload };
}

import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { OperationalDashboard } from './pages/OperationalDashboard';
import { PredictionsSimulation } from './pages/PredictionsSimulation';
import { Plants } from './pages/Plants';
import { Suppliers } from './pages/Suppliers';
import { Disruptions } from './pages/Disruptions';
import { Shipments } from './pages/Shipments';
import { PlaceholderPage } from './pages/PlaceholderPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<OperationalDashboard />} />
        <Route path="/predictions" element={<PredictionsSimulation />} />
        <Route path="/plants" element={<Plants />} />
        <Route path="/suppliers" element={<Suppliers />} />
        <Route path="/disruptions" element={<Disruptions />} />
        <Route path="/shipments" element={<Shipments />} />
        <Route path="/settings" element={<PlaceholderPage title="SETTINGS" />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

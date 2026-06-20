import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';

export default function PrivateRoute() {
    const { user } = useAuth();
    const token = localStorage.getItem("token");
    return user && token ? <Outlet /> : <Navigate to="/login" replace />;
}

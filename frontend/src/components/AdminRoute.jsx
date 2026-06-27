import { Navigate, Outlet } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'

export default function AdminRoute() {
    const { user } = useAuth()
    const token = localStorage.getItem('token')

    if (!user || !token) return <Navigate to="/login" replace />
    if (!user.isAdmin) return <Navigate to="/home" replace />

    return <Outlet />
}

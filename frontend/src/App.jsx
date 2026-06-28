import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext.jsx'
import PrivateRoute from './components/PrivateRoute.jsx'
import AdminRoute from './components/AdminRoute.jsx'
import LoginPage from './pages/LoginPage.jsx'
import AdminPage from './pages/AdminPage.jsx'
import RegisterPage from './pages/RegisterPage.jsx'
import HomePage from './pages/HomePage.jsx'
import Layout from './components/Layout.jsx'

function App() {
  return (
    <AuthProvider>
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />

                <Route element={<PrivateRoute />}>
                    <Route element={<Layout />}>
                        <Route path="/home" element={<HomePage />} />

                        <Route element={<AdminRoute />}>
                            <Route path="/admin" element={<AdminPage />} />
                        </Route>
                    </Route>
                </Route>

                <Route path="*" element={<Navigate to="/login" replace />} />
            </Routes>
        </BrowserRouter>
    </AuthProvider>
  )
}

export default App

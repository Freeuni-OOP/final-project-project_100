import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext.jsx'
import PrivateRoute from './components/PrivateRoute.jsx'
import LoginPage from './pages/LoginPage.jsx'
import RegisterPage from './pages/RegisterPage.jsx'
import QuizCreatePage from './pages/QuizCreatePage.jsx'
import { useAuth } from './context/AuthContext.jsx'

// placeholder till we develop layout
function HomePage() {
    const { user, logout } = useAuth();
    return (
        <div style={{ padding: '2rem' }}>
            <h1>Welcome, {user?.username}</h1>
            <button onClick={logout}>Log out</button>
        </div>
    );
}

function App() {
  return (
    <AuthProvider>
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />

                <Route element={<PrivateRoute />}>
                    <Route path="/home" element={<HomePage />} />
                </Route>

                <Route path="*" element={<Navigate to="/login" replace />} />
                <Route
                    path="/quiz-create"
                    element={
                        <PrivateRoute>
                            <QuizCreatePage />
                        </PrivateRoute>
                    }
                />
            </Routes>
        </BrowserRouter>
    </AuthProvider>
  )
}

export default App

import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext.jsx';

// Security & Layout Components
import PrivateRoute from './components/PrivateRoute.jsx';
import AdminRoute from './components/AdminRoute.jsx';
import Layout from './components/Layout.jsx';

// Pages
import LoginPage from './pages/LoginPage.jsx';
import RegisterPage from './pages/RegisterPage.jsx';
import HomePage from './pages/HomePage.jsx';
import AdminPage from './pages/AdminPage.jsx';
import ProfilePage from './pages/ProfilePage.jsx';
import QuizSummaryPage from './pages/QuizSummaryPage.jsx';
import QuizCreatePage from './pages/QuizCreatePage.jsx';
import FriendsPage from './pages/FriendsPage.jsx';

function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <Routes>
                    {/* Public Routes */}
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/register" element={<RegisterPage />} />

                    {/* Protected Routes wrapped in the Layout Navbar */}
                    <Route element={<PrivateRoute />}>
                        <Route element={<Layout />}>
                            {/* General User Routes */}
                            <Route path="/home" element={<HomePage />} />
                            <Route path="/profile/:username" element={<ProfilePage />} />
                            <Route path="/quizzes/:quizId/summary" element={<QuizSummaryPage />} />
                            <Route path="/quizzes/create" element={<QuizCreatePage />} />
                            <Route path="/friends" element={<FriendsPage />} />

                            {/* Admin Only Routes */}
                            <Route element={<AdminRoute />}>
                                <Route path="/admin" element={<AdminPage />} />
                            </Route>
                        </Route>
                    </Route>

                    {/* Fallback for unknown URLs */}
                    <Route path="*" element={<Navigate to="/login" replace />} />
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    );
}

export default App;
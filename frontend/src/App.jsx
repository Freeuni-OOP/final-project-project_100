import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext.jsx';

// Security & Layout Components
import PrivateRoute from './components/PrivateRoute.jsx';
import Layout from './components/Layout.jsx';

// Pages
import LoginPage from './pages/LoginPage.jsx';
import RegisterPage from './pages/RegisterPage.jsx';
import HomePage from './pages/HomePage.jsx';
import ProfilePage from './pages/ProfilePage.jsx';
import QuizSummaryPage from './pages/QuizSummaryPage.jsx';
import QuizCreatePage from './pages/QuizCreatePage.jsx';
import FriendsPage from './pages/FriendsPage.jsx'; // <-- NEW IMPORT

function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <Routes>
                    {/* Public Routes */}
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/register" element={<RegisterPage />} />

                    {/* Protected Routes with Layout */}
                    <Route element={<PrivateRoute />}>
                        <Route element={<Layout />}>
                            <Route path="/home" element={<HomePage />} />
                            <Route path="/profile/:username" element={<ProfilePage />} />
                            <Route path="/quizzes/:quizId/summary" element={<QuizSummaryPage />} />
                            <Route path="/quiz-create" element={<QuizCreatePage />} />
                            <Route path="/friends" element={<FriendsPage />} /> {/* <-- NEW ROUTE */}
                        </Route>
                    </Route>

                    {/* Fallback */}
                    <Route path="*" element={<Navigate to="/login" replace />} />
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    );
}

export default App;
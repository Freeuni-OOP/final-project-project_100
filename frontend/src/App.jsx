import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext.jsx'
import PrivateRoute from './components/PrivateRoute.jsx'
import AdminRoute from './components/AdminRoute.jsx'
import LoginPage from './pages/LoginPage.jsx'
import AdminPage from './pages/AdminPage.jsx'
import RegisterPage from './pages/RegisterPage.jsx'
import HomePage from './pages/HomePage.jsx'
import Layout from './components/Layout.jsx'
import QuizCreatePage from "./pages/QuizCreatePage.jsx";
import QuizTakePage from './pages/QuizTakePage.jsx'
import QuizResultPage from './pages/QuizResultPage.jsx'


// --- Your New Social & Summary Pages ---
import ProfilePage from "./pages/ProfilePage.jsx";
import FriendsPage from "./pages/FriendsPage.jsx";
import QuizSummaryPage from "./pages/QuizSummaryPage.jsx";

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
                            <Route path="/quizzes/create" element={<QuizCreatePage />} />

                            <Route path="/profile/:username" element={<ProfilePage />} />
                            <Route path="/friends" element={<FriendsPage />} />
                            <Route path="/quizzes/:quizId/summary" element={<QuizSummaryPage />} />

                            <Route element={<AdminRoute />}>
                                <Route path="/admin" element={<AdminPage />} />
                            </Route>
                        </Route>

                        <Route path="/quizzes/:quizId" element={<QuizTakePage />} />
                        <Route path="/quizzes/:quizId/results" element={<QuizResultPage />} />
                    </Route>

                    <Route path="*" element={<Navigate to="/login" replace />} />
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    );
}

export default App;
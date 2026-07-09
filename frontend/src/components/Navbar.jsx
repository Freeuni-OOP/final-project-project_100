import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import styles from '../styles/navbar.module.css';

const NAV_LINKS = [
    { to: '/home',    label: 'Home' },
    { to: '/quizzes', label: 'Quizzes' },
];

export default function Navbar() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();

    function handleLogout() {
        logout();
        navigate('/login');
    }

    return (
        <nav className={styles.nav}>
            <div className={styles.inner}>

                <Link to="/home" className={styles.logo}>
                    QuizSite
                </Link>

                <div className={styles.links}>
                    {/* 1. Standard Links */}
                    {NAV_LINKS.map(({ to, label }) => (
                        <Link
                            key={to}
                            to={to}
                            className={`${styles.link} ${location.pathname === to ? styles.linkActive : ''}`}
                        >
                            {label}
                        </Link>
                    ))}

                    {/* 2. Your Dynamic Profile Link */}
                    {user?.username && (
                        <Link
                            to={`/profile/${user.username}`}
                            className={`${styles.link} ${location.pathname.startsWith('/profile') ? styles.linkActive : ''}`}
                        >
                            My Profile
                        </Link>
                    )}

                    {/* 3. Temporary Quiz Summary Link for testing */}
                    <Link
                        to="/quizzes/1/summary"
                        className={`${styles.link} ${location.pathname.includes('/summary') ? styles.linkActive : ''}`}
                    >
                        Quiz 1 Summary
                    </Link>

                    {/* Admin link has been removed from here to match your App.jsx */}
                </div>

                <div className={styles.right}>
                    <span className={styles.username}>{user?.username}</span>
                    <button className={styles.logoutBtn} onClick={handleLogout}>
                        Log out
                    </button>
                </div>

            </div>
        </nav>
    );
}
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import styles from '../styles/navbar.module.css';

// 1. Added the Friends link right into your array
const NAV_LINKS = [
    { to: '/home',    label: 'Home' },
    { to: '/quizzes', label: 'Quizzes' },
    { to: '/friends', label: 'Friends' },
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
                    {/* Standard Links */}
                    {NAV_LINKS.map(({ to, label }) => (
                        <Link
                            key={to}
                            to={to}
                            className={`${styles.link} ${location.pathname === to ? styles.linkActive : ''}`}
                        >
                            {label}
                        </Link>
                    ))}

                    {/* Your Dynamic Profile Link */}
                    {user?.username && (
                        <Link
                            to={`/profile/${user.username}`}
                            className={`${styles.link} ${location.pathname.startsWith('/profile') ? styles.linkActive : ''}`}
                        >
                            My Profile
                        </Link>
                    )}

                    {/* Temporary Quiz Summary Link for testing */}
                    <Link
                        to="/quizzes/1/summary"
                        className={`${styles.link} ${location.pathname.includes('/summary') ? styles.linkActive : ''}`}
                    >
                        Quiz 1 Summary
                    </Link>
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
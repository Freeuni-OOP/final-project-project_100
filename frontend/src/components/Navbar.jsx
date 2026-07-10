import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import styles from '../styles/navbar.module.css';

export default function Navbar() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();

    function handleLogout() {
        logout();
        navigate('/login');
    }

    // Moved inside the component so it can use the dynamic user data
    const NAV_LINKS = [
        { to: '/home',    label: 'Home' },
        { to: '/quizzes', label: 'Quizzes' },
        { to: '/friends', label: 'Friends' },
        { to: `/profile/${user?.username}`, label: 'My Profile' },
    ];

    return (
        <nav className={styles.nav}>
            <div className={styles.inner}>

                <Link to="/home" className={styles.logo}>
                    QuizSite
                </Link>

                <div className={styles.links}>
                    {NAV_LINKS.map(({ to, label }) => (
                        <Link
                            key={to}
                            to={to}
                            className={`${styles.link} ${location.pathname === to ? styles.linkActive : ''}`}
                        >
                            {label}
                        </Link>
                    ))}

                    {user?.isAdmin && (
                        <Link
                            to="/admin"
                            className={`${styles.link} ${location.pathname === '/admin' ? styles.linkActive : ''}`}
                        >
                            Admin
                        </Link>
                    )}

                    {user?.username && (
                        <Link
                            to={`/profile/${user.username}`}
                            className={`${styles.link} ${location.pathname.startsWith('/profile') ? styles.linkActive : ''}`}
                        >
                            My Profile
                        </Link>
                    )}
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
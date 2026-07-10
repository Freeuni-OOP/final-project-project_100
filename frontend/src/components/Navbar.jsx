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
        { to: '/home', label: 'Home' },
        { to: '/quizzes', label: 'Quizzes' },
        { to: '/friends', label: 'Friends' }, // So you don't have to type the URL manually!
        { to: `/profile/${user?.username}`, label: 'My Profile' },
        { to: '/quizzes/1/summary', label: 'Quiz 1 Summary' } // Hardcoded as it was in your version
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
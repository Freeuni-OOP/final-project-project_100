import { Outlet } from 'react-router-dom';
import Navbar from './Navbar.jsx';
import styles from '../styles/layout.module.css';

export default function Layout() {
    return (
        <div className={styles.root}>
            <Navbar />
            <main className={styles.main}>
                <Outlet />
            </main>
        </div>
    );
}
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios.js';
import { useAuth } from '../context/AuthContext.jsx';
import styles from '../styles/homepage.module.css';

export default function HomePage() {
    const { user } = useAuth();
    const [announcements, setAnnouncements] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function fetchData() {
            try {
                const announcementsRes = await api.get('/announcements');
                setAnnouncements(announcementsRes.data);
            } catch (err) {
                console.error('Failed to load homepage data', err);
            } finally {
                setLoading(false);
            }
        }
        fetchData();
    }, []);

    if (loading) return <div className={styles.loading}>Loading...</div>;

    return (
        <div className={styles.page}>

            {announcements.length > 0 && (
                <section className={styles.section}>
                    <h2 className={styles.sectionTitle}>Announcements</h2>
                    <div className={styles.announcementList}>
                        {announcements.map(a => (
                            <div key={a.id} className={styles.announcement}>
                                <div className={styles.announcementTitle}>{a.title}</div>
                                <div className={styles.announcementMeta}>
                                    Posted by {a.createdBy} · {new Date(a.createdAt).toLocaleDateString()}
                                </div>
                                <div className={styles.announcementContent}>{a.content}</div>
                            </div>
                        ))}
                    </div>
                </section>
            )}

            <section className={styles.section}>
                <div className={styles.sectionHeader}>
                    <h2 className={styles.sectionTitle}>Welcome back, {user?.username}</h2>
                    <Link to="/quizzes/create" className={styles.createBtn}>
                        + Create Quiz
                    </Link>
                </div>
                <p className={styles.placeholder}>
                    Popular quizzes and your recent activity will appear here once
                    the quiz engine is implemented.
                </p>
            </section>

        </div>
    );
}
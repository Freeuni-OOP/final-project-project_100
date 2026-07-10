import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios.js';
import { useAuth } from '../context/AuthContext.jsx';
import styles from '../styles/homepage.module.css';

export default function HomePage() {
    const { user } = useAuth();
    const [announcements, setAnnouncements] = useState([]);
    const [recentQuizzes, setRecentQuizzes] = useState([])
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function fetchData() {
            try {
                const [announcementsRes, quizzesRes] = await Promise.all([
                    api.get('/announcements'),
                    api.get('/quizzes/recent')
                ])
                setAnnouncements(announcementsRes.data)
                setRecentQuizzes(quizzesRes.data)
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
                    <h2 className={styles.sectionTitle}>Recent Quizzes</h2>
                    <Link to="/quizzes/create" className={styles.createBtn}>
                        + Create Quiz
                    </Link>
                </div>

                {recentQuizzes.length === 0
                    ? <p className={styles.placeholder}>No quizzes yet — be the first to create one!</p>
                    : <div className={styles.quizGrid}>
                        {recentQuizzes.map(q => (
                            <Link
                                key={q.id}
                                to={`/quizzes/${q.id}`}
                                className={styles.quizCard}
                            >
                                <div className={styles.quizCardTitle}>{q.title}</div>
                                <div className={styles.quizCardMeta}>
                                    {q.questionCount} questions · by {q.createdBy}
                                </div>
                                {q.description && (
                                    <div className={styles.quizCardDesc}>{q.description}</div>
                                )}
                                <div className={styles.quizCardDate}>
                                    {new Date(q.createdAt).toLocaleDateString()}
                                </div>
                            </Link>
                        ))}
                    </div>
                }
            </section>
        </div>
    );
}
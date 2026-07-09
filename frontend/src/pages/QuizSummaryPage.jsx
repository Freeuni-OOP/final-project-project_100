import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { getQuizSummary } from "../api/quizSummaryService.js";
import { useAuth } from '../context/AuthContext.jsx';
import AttemptTable from '../components/AttemptTable.jsx';
import styles from '../styles/quizSummary.module.css';

export default function QuizSummaryPage() {
    const { user } = useAuth();
    const { quizId } = useParams();

    const [summary, setSummary] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchSummary = async () => {
            setLoading(true);
            try {
                const data = await getQuizSummary(quizId);
                setSummary(data);
                setError(null);
            } catch (err) {
                console.error("Summary fetch error:", err);
                setError("Unable to load the quiz summary at this time.");
            } finally {
                setLoading(false);
            }
        };

        if (quizId) {
            fetchSummary();
        }
    }, [quizId]);

    if (loading) return <div className={styles.loading}>Loading Quiz Data...</div>;
    if (error) return <div className={styles.error}>{error}</div>;
    if (!summary) return null;

    return (
        <div className={styles.container}>
            <Link to="/home" className={styles.backLink}>← Back to Home</Link>

            <div className={styles.headerCard}>
                <h1 className={styles.title}>Quiz #{quizId} Summary</h1>
                <p className={styles.subtitle}>Review top performers and your past attempts.</p>
            </div>

            <div className={styles.grid}>
                {/* Highest Performers (All Time) */}
                <div className={styles.card}>
                    <h3>All-Time Top Performers</h3>
                    <AttemptTable attempts={summary.topAllTime} currentUsername={user?.username} mode="ranked" />
                </div>

                {/* Top Performers (Last Day) */}
                <div className={styles.card}>
                    <h3>Top Performers (Last 24h)</h3>
                    <AttemptTable attempts={summary.topLastDay} currentUsername={user?.username} mode="ranked" />
                </div>

                {/* Recent Test Takers */}
                <div className={styles.card}>
                    <h3>Recent Test Takers</h3>
                    <AttemptTable attempts={summary.recentTakers} currentUsername={user?.username} mode="recent" />
                </div>

                {/* User's Past Performance */}
                <div className={styles.card}>
                    <h3>Your Past Performance</h3>
                    <AttemptTable attempts={summary.userHistory} currentUsername={user?.username} mode="history" />
                </div>
            </div>
        </div>
    );
}
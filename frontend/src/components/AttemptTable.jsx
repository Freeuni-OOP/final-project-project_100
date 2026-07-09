import { Link } from 'react-router-dom';
import styles from '../styles/quizSummary.module.css';

export default function AttemptTable({ attempts, currentUsername, mode }) {
    if (!attempts || attempts.length === 0) {
        return <p className={styles.emptyText}>No data available yet.</p>;
    }

    const showRank = mode === 'ranked';
    const showDate = mode === 'recent' || mode === 'history';

    // Helper function to make time readable (e.g., 65s -> 1m 5s)
    const formatTime = (totalSeconds) => {
        const mins = Math.floor(totalSeconds / 60);
        const secs = totalSeconds % 60;
        return mins > 0 ? `${mins}m ${secs}s` : `${secs}s`;
    };

    return (
        <table className={styles.table}>
            <thead>
            <tr>
                {showRank && <th>Rank</th>}
                {showDate && <th>Date</th>}
                <th>User</th>
                <th>Score</th>
                <th>Time</th>
            </tr>
            </thead>
            <tbody>
            {attempts.map((attempt, index) => {
                // Safe fallback for nested DTO properties
                const targetUsername = attempt.username || attempt.user?.username || "Anonymous";
                const isCurrentUser = targetUsername === currentUsername;

                return (
                    <tr key={attempt.id || index} className={isCurrentUser ? styles.highlightRow : ''}>
                        {showRank && (
                            <td><span className={styles.rankBadge}>{index + 1}</span></td>
                        )}
                        {showDate && (
                            <td>{new Date(attempt.takenAt).toLocaleDateString()}</td>
                        )}
                        <td>
                            <Link to={`/profile/${targetUsername}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                                {targetUsername} {isCurrentUser && "(You)"}
                            </Link>
                        </td>
                        <td style={{ fontWeight: '600' }}>{attempt.score} pts</td>
                        <td>{formatTime(attempt.timeTakenSec)}</td>
                    </tr>
                );
            })}
            </tbody>
        </table>
    );
}
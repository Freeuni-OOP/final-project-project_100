import styles from '../styles/profile.module.css';

export default function QuizHistoryList({ attempts }) {
    // Failsafe in case they haven't taken a quiz yet
    if (!attempts || attempts.length === 0) {
        return (
            <div className={styles.card}>
                <h3 className={styles.title}>Recent Activity</h3>
                <p className={styles.text}>No quizzes taken yet.</p>
            </div>
        );
    }

    return (
        <div className={styles.card}>
            <h3 className={styles.title}>Recent Activity</h3>
            <table className={styles.table}>
                <thead>
                <tr>
                    <th>Date</th>
                    <th>Quiz ID</th>
                    <th>Score</th>
                    <th>Time Taken</th>
                </tr>
                </thead>
                <tbody>
                {attempts.map((attempt) => (
                    <tr key={attempt.id}>
                        <td>{new Date(attempt.takenAt).toLocaleDateString()}</td>
                        <td>#{attempt.quizId}</td>
                        <td>{attempt.score} pts</td>
                        <td>{attempt.timeTakenSec}s</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
import styles from '../styles/profile.module.css';

export default function AchievementsBoard({ achievements }) {
    // Failsafe in case the user has no achievements yet
    if (!achievements || achievements.length === 0) {
        return (
            <div className={styles.card}>
                <h3 className={styles.title}>Achievements</h3>
                <p className={styles.text}>No achievements yet.</p>
            </div>
        );
    }

    return (
        <div className={styles.card}>
            <h3 className={styles.title}>Achievements</h3>
            <div className={styles.badgeContainer}>
                {achievements.map((achievement, index) => (
                    <span key={index} className={styles.badge}>
                        {achievement.replace(/_/g, ' ')}
                    </span>
                ))}
            </div>
        </div>
    );
}
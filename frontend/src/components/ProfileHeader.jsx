import styles from '../styles/profile.module.css';

export default function ProfileHeader({ profile }) {
    const joinDate = new Date(profile.joinedAt).toLocaleDateString();

    return (
        <div className={styles.card}>
            <div className={styles.header}>
                <div>
                    <h2 className={styles.title}>{profile.username}</h2>
                    <p className={styles.text}>{profile.email}</p>
                    <p className={styles.text}>Joined: {joinDate}</p>
                </div>
                {/* Conditionally render badges based on the backend data */}
                {profile.relation === 'self' && (
                    <span className={styles.badge}>This is you</span>
                )}
                {profile.isAdmin && (
                    <span className={styles.badge} style={{ backgroundColor: '#fee2e2', color: '#991b1b' }}>Admin</span>
                )}
            </div>
        </div>
    );
}
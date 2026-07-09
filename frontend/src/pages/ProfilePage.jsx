import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { getProfile } from '../api/profileService.js';
import ProfileHeader from '../components/ProfileHeader.jsx';
import AchievementsBoard from '../components/AchievementsBoard.jsx';
import QuizHistoryList from '../components/QuizHistoryList.jsx';
import styles from '../styles/profile.module.css';

export default function ProfilePage() {
    const { username } = useParams();

    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchProfile = async () => {
            setLoading(true);
            setError(null);

            try {
                const data = await getProfile(username);
                setProfile(data);
            } catch (err) {
                if (err.response && err.response.status === 404) {
                    setError("User not found.");
                } else {
                    setError("Unable to connect to the server. Please try again.");
                }
            } finally {
                setLoading(false);
            }
        };

        if (username) {
            fetchProfile();
        }
    }, [username]); // Re-run if the URL changes

    if (loading) return <div className={styles.loading}>Loading profile...</div>;

    if (error) return (
        <div className={styles.container}>
            <Link to="/home" className={styles.backLink}>← Back to Home</Link>
            <div className={styles.error}>{error}</div>
        </div>
    );

    if (!profile) return null;

    return (
        <div className={styles.container}>
            <Link to="/home" className={styles.backLink}>← Back to Home</Link>

            <ProfileHeader profile={profile} />
            <AchievementsBoard achievements={profile.achievements} />
            <QuizHistoryList attempts={profile.recentAttempts} />
        </div>
    );
}
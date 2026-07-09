import { useEffect, useState, useCallback } from 'react';
import { useParams, Link } from 'react-router-dom';
import { getProfile } from '../api/profileService.js';
import ProfileHeader from '../components/ProfileHeader.jsx';
import AchievementsBoard from '../components/AchievementsBoard.jsx';
import QuizHistoryList from '../components/QuizHistoryList.jsx';
import FriendActionButton from '../components/FriendActionButton.jsx';
import styles from '../styles/profile.module.css';

export default function ProfilePage() {
    const { username } = useParams();

    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Wrapped in useCallback so we can safely pass it to the action button
    const fetchProfile = useCallback(async () => {
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
    }, [username]);

    useEffect(() => {
        if (username) {
            fetchProfile();
        }
    }, [fetchProfile]); // Re-run if the URL changes

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

            {/* Dynamic Friendship and Management Buttons */}
            <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: '1rem', gap: '10px' }}>

                {/* 1. Manage Friends shortcut for your own profile */}
                {profile.relation === 'SELF' && (
                    <Link
                        to="/friends"
                        style={{
                            textDecoration: 'none',
                            padding: '0.5rem 1rem',
                            backgroundColor: '#e0e0e0',
                            color: '#333',
                            borderRadius: '4px',
                            fontWeight: '600',
                            display: 'inline-block'
                        }}
                    >
                        Manage Friends
                    </Link>
                )}

                {/* 2. The standard Action Button (hides itself internally if relation is SELF) */}
                <FriendActionButton
                    targetUserId={profile.userId}
                    targetUsername={profile.username}
                    currentRelation={profile.relation}
                    onActionComplete={fetchProfile}
                />
            </div>

            <ProfileHeader profile={profile} />
            <AchievementsBoard achievements={profile.achievements} />
            <QuizHistoryList attempts={profile.recentAttempts} />
        </div>
    );
}
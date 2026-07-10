import { useState } from 'react';
import { friendshipService } from '../api/friendshipService';
import styles from '../styles/friends.module.css';

const FriendActionButton = ({ targetUserId, targetUsername, currentRelation, onActionComplete }) => {
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    // If looking at your own profile, render nothing
    if (currentRelation === 'SELF') return null;

    // Centralized handler for all API calls to manage loading and error states safely
    const handleAction = async (actionFn) => {
        setIsLoading(true);
        setError(null);
        try {
            await actionFn();
            if (onActionComplete) onActionComplete(); // Instantly refreshes the profile UI
        } catch (err) {
            setError(err.message || 'An error occurred. Please try again.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className={styles.actionContainer}>
            {error && <span className={styles.errorText}>{error}</span>}

            {/* STATE 1: No Relationship */}
            {currentRelation === 'NONE' && (
                <button
                    onClick={() => handleAction(() => friendshipService.sendRequest(targetUsername))}
                    disabled={isLoading}
                    className={styles.primaryBtn}
                >
                    Add Friend
                </button>
            )}

            {/* STATE 2: Already Friends */}
            {currentRelation === 'FRIEND' && (
                <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: '8px' }}>
                    <span style={{ fontSize: '0.9rem', color: '#28a745', fontWeight: '600' }}>
                        ✓ You and {targetUsername} are friends
                    </span>
                    <button
                        onClick={() => handleAction(() => friendshipService.removeFriend(targetUserId))}
                        disabled={isLoading}
                        className={styles.dangerBtn}
                    >
                        Unfriend
                    </button>
                </div>
            )}

            {/* STATE 3: You Sent Them a Request */}
            {currentRelation === 'PENDING_OUTGOING' && (
                <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: '8px' }}>
                    <span style={{ fontSize: '0.9rem', color: '#555', fontWeight: '500', fontStyle: 'italic' }}>
                        Friend request sent...
                    </span>
                    <button
                        onClick={() => handleAction(() => friendshipService.removeFriend(targetUserId))}
                        disabled={isLoading}
                        className={styles.secondaryBtn}
                    >
                        Cancel Request
                    </button>
                </div>
            )}

            {/* STATE 4: They Sent You a Request */}
            {currentRelation === 'PENDING_INCOMING' && (
                <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: '8px' }}>
                    <span style={{ fontSize: '0.9rem', color: '#333', fontWeight: '600' }}>
                        {targetUsername} sent you a friend request!
                    </span>
                    <div className={styles.buttonGroup}>
                        <button
                            onClick={() => handleAction(() => friendshipService.acceptRequest(targetUserId))}
                            disabled={isLoading}
                            className={styles.primaryBtn}
                        >
                            Accept
                        </button>
                        <button
                            onClick={() => handleAction(() => friendshipService.rejectRequest(targetUserId))}
                            disabled={isLoading}
                            className={styles.dangerBtn}
                        >
                            Reject
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default FriendActionButton;
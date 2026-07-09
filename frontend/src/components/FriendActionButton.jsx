import { useState } from 'react';
import { friendshipService } from '../api/friendshipService';
import styles from '../styles/friends.module.css';

const FriendActionButton = ({ targetUserId, targetUsername, currentRelation, onActionComplete }) => {
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    // If looking at your own profile, render nothing
    if (currentRelation === 'SELF') return null;

    const handleAction = async (actionFn) => {
        setIsLoading(true);
        setError(null);
        try {
            await actionFn();
            if (onActionComplete) onActionComplete(); // Triggers a profile re-fetch
        } catch (err) {
            setError(err.message);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className={styles.actionContainer}>
            {error && <span className={styles.errorText}>{error}</span>}

            {currentRelation === 'NONE' && (
                <button
                    onClick={() => handleAction(() => friendshipService.sendRequest(targetUsername))}
                    disabled={isLoading}
                    className={styles.primaryBtn}
                >
                    Add Friend
                </button>
            )}

            {currentRelation === 'FRIEND' && (
                <button
                    onClick={() => handleAction(() => friendshipService.removeFriend(targetUserId))}
                    disabled={isLoading}
                    className={styles.dangerBtn}
                >
                    Unfriend
                </button>
            )}

            {currentRelation === 'PENDING_OUTGOING' && (
                <button
                    onClick={() => handleAction(() => friendshipService.removeFriend(targetUserId))}
                    disabled={isLoading}
                    className={styles.secondaryBtn}
                >
                    Cancel Request
                </button>
            )}

            {currentRelation === 'PENDING_INCOMING' && (
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
            )}
        </div>
    );
};

export default FriendActionButton;
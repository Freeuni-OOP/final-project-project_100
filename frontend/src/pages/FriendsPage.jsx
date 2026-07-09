import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { friendshipService } from '../api/friendshipService';
import styles from '../styles/friends.module.css';

const FriendsPage = () => {
    const [friends, setFriends] = useState([]);
    const [requests, setRequests] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    // NEW: Search state and navigation hook
    const [searchQuery, setSearchQuery] = useState('');
    const navigate = useNavigate();

    const loadData = async () => {
        setIsLoading(true);
        try {
            const [friendsData, requestsData] = await Promise.all([
                friendshipService.getAcceptedFriends(),
                friendshipService.getPendingRequests()
            ]);
            setFriends(friendsData);
            setRequests(requestsData);
        } catch (err) {
            setError('Failed to load friendship data.');
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        loadData();
    }, []);

    const handleAccept = async (userId) => {
        await friendshipService.acceptRequest(userId);
        loadData();
    };

    const handleReject = async (userId) => {
        await friendshipService.rejectRequest(userId);
        loadData();
    };

    // NEW: Safely route to the typed username's profile
    const handleSearch = (e) => {
        e.preventDefault();
        if (searchQuery.trim()) {
            navigate(`/profile/${searchQuery.trim()}`);
        }
    };

    if (isLoading) return <div className={styles.loading}>Loading friends...</div>;
    if (error) return <div className={styles.errorText}>{error}</div>;

    return (
        <div className={styles.pageContainer}>

            {/* NEW: Find a Friend Section */}
            <div className={styles.section}>
                <h2>Find a Friend</h2>
                <form onSubmit={handleSearch} style={{ display: 'flex', gap: '10px' }}>
                    <input
                        type="text"
                        placeholder="Enter username to search..."
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                        style={{
                            padding: '0.5rem',
                            borderRadius: '4px',
                            border: '1px solid #ccc',
                            flex: 1,
                            fontSize: '1rem'
                        }}
                    />
                    <button type="submit" className={styles.primaryBtn}>Search</button>
                </form>
            </div>

            {/* Friend Requests Section */}
            <div className={styles.section}>
                <h2>Friend Requests ({requests.length})</h2>
                {requests.length === 0 ? (
                    <p className={styles.emptyText}>No pending requests.</p>
                ) : (
                    <ul className={styles.userList}>
                        {requests.map(req => (
                            <li key={req.userId} className={styles.userCard}>
                                <Link to={`/profile/${req.username}`} className={styles.usernameLink}>
                                    {req.username}
                                </Link>
                                <div className={styles.buttonGroup}>
                                    <button onClick={() => handleAccept(req.userId)} className={styles.primaryBtn}>Accept</button>
                                    <button onClick={() => handleReject(req.userId)} className={styles.dangerBtn}>Reject</button>
                                </div>
                            </li>
                        ))}
                    </ul>
                )}
            </div>

            {/* My Friends Section */}
            <div className={styles.section}>
                <h2>My Friends ({friends.length})</h2>
                {friends.length === 0 ? (
                    <p className={styles.emptyText}>You haven't added any friends yet.</p>
                ) : (
                    <ul className={styles.userList}>
                        {friends.map(friend => (
                            <li key={friend.userId} className={styles.userCard}>
                                <Link to={`/profile/${friend.username}`} className={styles.usernameLink}>
                                    {friend.username}
                                </Link>
                            </li>
                        ))}
                    </ul>
                )}
            </div>

        </div>
    );
};

export default FriendsPage;
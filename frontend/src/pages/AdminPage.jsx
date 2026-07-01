import { useState, useEffect, useCallback } from 'react';
import api from '../api/axios.js';
import styles from '../styles/admin.module.css';

const TABS = ['Announcements', 'Users', 'Quizzes', 'Stats'];

export default function AdminPage() {
  const [activeTab, setActiveTab] = useState('Announcements');

  return (
    <div className={styles.page}>
      <h1 className={styles.title}>Admin Panel</h1>

      <div className={styles.tabs}>
        {TABS.map(tab => (
          <button
            key={tab}
            className={`${styles.tab} ${activeTab === tab ? styles.tabActive : ''}`}
            onClick={() => setActiveTab(tab)}
          >
            {tab}
          </button>
        ))}
      </div>

      <div className={styles.content}>
        {activeTab === 'Announcements' && <AnnouncementsTab />}
        {activeTab === 'Users'         && <UsersTab />}
        {activeTab === 'Quizzes'       && <QuizzesTab />}
        {activeTab === 'Stats'         && <StatsTab />}
      </div>
    </div>
  )
}

function AnnouncementsTab() {
  const [announcements, setAnnouncements] = useState([]);
  const [form, setForm] = useState({ title: '', content: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const fetchAnnouncements = useCallback(async () => {
    const res = await api.get('/admin/announcements');
    setAnnouncements(res.data);
  }, []);

  useEffect(() => { fetchAnnouncements() }, [fetchAnnouncements]);

  async function handleCreate(e) {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      await api.post('/admin/announcements', form);
      setForm({ title: '', content: '' });
      fetchAnnouncements();
    } catch (err) {
      setError(err.response?.data?.error || 'Failed to create announcement');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className={styles.tabContent}>
      <h2 className={styles.sectionTitle}>Create Announcement</h2>
      {error && <div className={styles.error}>{error}</div>}
      <form onSubmit={handleCreate} className={styles.form}>
        <input
          className={styles.input}
          placeholder="Title"
          value={form.title}
          onChange={e => setForm(p => ({ ...p, title: e.target.value }))}
          required
        />
        <textarea
          className={styles.textarea}
          placeholder="Content"
          value={form.content}
          onChange={e => setForm(p => ({ ...p, content: e.target.value }))}
          rows={4}
          required
        />
        <button className={styles.button} type="submit" disabled={loading}>
          {loading ? 'Posting...' : 'Post Announcement'}
        </button>
      </form>

      <h2 className={styles.sectionTitle} style={{ marginTop: '2rem' }}>All Announcements</h2>
      {announcements.length === 0
        ? <p className={styles.empty}>No announcements yet.</p>
        : announcements.map(a => (
            <div key={a.id} className={styles.listItem}>
              <div className={styles.listItemTitle}>{a.title}</div>
              <div className={styles.listItemMeta}>by {a.createdBy} · {new Date(a.createdAt).toLocaleDateString()}</div>
              <div className={styles.listItemDesc}>{a.content}</div>
            </div>
          ))
      }
    </div>
  )
}

function UsersTab() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchUsers = useCallback(async () => {
    const res = await api.get('/admin/users')
    setUsers(res.data)
    setLoading(false)
  }, [])

  useEffect(() => { fetchUsers() }, [fetchUsers])

  async function handleDelete(id, username) {
    if (!window.confirm(`Delete user "${username}"? This cannot be undone.`)) return
    try {
      await api.delete(`/admin/users/${id}`)
      setUsers(prev => prev.filter(u => u.id !== id))
    } catch (err) {
      alert(err.response?.data?.error || 'Failed to delete user')
    }
  }

  async function handlePromote(id, username) {
    if (!window.confirm(`Promote "${username}" to admin?`)) return
    try {
      await api.put(`/admin/users/${id}/promote`)
      setUsers(prev => prev.map(u => u.id === id ? { ...u, isAdmin: true } : u))
    } catch (err) {
      alert(err.response?.data?.error || 'Failed to promote user')
    }
  }

  if (loading) return <p className={styles.empty}>Loading users...</p>

  return (
    <div className={styles.tabContent}>
      <h2 className={styles.sectionTitle}>All Users ({users.length})</h2>
      {users.map(u => (
        <div key={u.id} className={styles.listItem}>
          <div className={styles.listItemTitle}>
            {u.username}
            {u.isAdmin && <span className={styles.adminBadge}>Admin</span>}
          </div>
          <div className={styles.listItemMeta}>{u.email} · joined {new Date(u.createdAt).toLocaleDateString()}</div>
          <div className={styles.actions}>
            {!u.isAdmin && (
              <>
                <button
                  className={styles.buttonSmall}
                  onClick={() => handlePromote(u.id, u.username)}
                >
                  Promote to Admin
                </button>
                <button
                  className={`${styles.buttonSmall} ${styles.buttonDanger}`}
                  onClick={() => handleDelete(u.id, u.username)}
                >
                  Delete
                </button>
              </>
            )}
          </div>
        </div>
      ))}
    </div>
  )
}

function QuizzesTab() {
  const [quizzes, setQuizzes] = useState([])
  const [loading, setLoading] = useState(true)

  const fetchQuizzes = useCallback(async () => {
    const res = await api.get('/quizzes')
    setQuizzes(res.data)
    setLoading(false)
  }, [])

  useEffect(() => { fetchQuizzes() }, [fetchQuizzes])

  async function handleDelete(id, title) {
    if (!window.confirm(`Delete quiz "${title}"? This cannot be undone.`)) return
    try {
      await api.delete(`/admin/quiz/${id}`)
      setQuizzes(prev => prev.filter(q => q.id !== id))
    } catch (err) {
      alert(err.response?.data?.error || 'Failed to delete quiz')
    }
  }

  async function handleClearHistory(id, title) {
    if (!window.confirm(`Clear all attempt history for "${title}"?`)) return
    try {
      await api.delete(`/admin/quiz/${id}/history`)
      alert('History cleared')
    } catch (err) {
      alert(err.response?.data?.error || 'Failed to clear history')
    }
  }

  if (loading) return <p className={styles.empty}>Loading quizzes...</p>

  return (
    <div className={styles.tabContent}>
      <h2 className={styles.sectionTitle}>All Quizzes ({quizzes.length})</h2>
      {quizzes.map(q => (
        <div key={q.id} className={styles.listItem}>
          <div className={styles.listItemTitle}>{q.title}</div>
          <div className={styles.listItemMeta}>by {q.createdBy}</div>
          <div className={styles.actions}>
            <button
              className={styles.buttonSmall}
              onClick={() => handleClearHistory(q.id, q.title)}
            >
              Clear History
            </button>
            <button
              className={`${styles.buttonSmall} ${styles.buttonDanger}`}
              onClick={() => handleDelete(q.id, q.title)}
            >
              Delete
            </button>
          </div>
        </div>
      ))}
    </div>
  )
}

function StatsTab() {
  const [stats, setStats] = useState(null)

  useEffect(() => {
    api.get('/admin/stats').then(res => setStats(res.data))
  }, [])

  if (!stats) return <p className={styles.empty}>Loading stats...</p>

  return (
    <div className={styles.tabContent}>
      <h2 className={styles.sectionTitle}>Site Statistics</h2>
      <div className={styles.statsGrid}>
        <StatCard label="Total Users"    value={stats.totalUsers} />
        <StatCard label="Total Quizzes"  value={stats.totalQuizzes} />
        <StatCard label="Total Attempts" value={stats.totalAttempts} />
        <StatCard label="New Users Today" value={stats.newUsersToday} />
      </div>
    </div>
  )
}

function StatCard({ label, value }) {
  return (
    <div className={styles.statCard}>
      <div className={styles.statValue}>{value}</div>
      <div className={styles.statLabel}>{label}</div>
    </div>
  )
}

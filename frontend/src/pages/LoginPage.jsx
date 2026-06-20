import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../api/axios.js';
import { useAuth } from '../context/AuthContext.jsx';

import styles from '../styles/auth.module.css';

export default function LoginPage() {
    const { login } = useAuth();
    const navigate = useNavigate();

    const [form, setForm] = useState({ username: "", password: "" });
    const [errors, setErrors] = useState({});
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target
        setForm(prev => ({ ...prev, [name]: value }))
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setErrors({});

        try {
            const resp = await api.post("/auth/login", form);
            const { userId, username, token } = resp.data;
            login(token, { id: userId, username });
            navigate("/home");
        } catch (err) {
            const data = err.response?.data;
            setErrors({ general: data?.error || "Login failed, please try again" });
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className={styles.page}>
            <div className={styles.card}>
                <h2 className={styles.title}>Sign in</h2>
                {errors.general && <div className={styles.error}>{errors.general}</div>}

                <form onSubmit={handleSubmit} className={styles.form}>
                    <label className={styles.label}>
                        Username
                        <input
                            className={styles.input}
                            name="username"
                            value={form.username}
                            onChange={handleChange}
                            autoComplete="off"
                            required
                        />
                    </label>

                    <label className={styles.label}>
                        Password
                        <input
                            className={styles.input}
                            name="password"
                            type="password"
                            value={form.password}
                            onChange={handleChange}
                            required
                        />
                    </label>

                    <button className={styles.button} type="submit" disabled={loading}>
                        {loading ? 'Signing in...' : 'Sign in'}
                    </button>
                </form>

                <p className={styles.footer}>
                    Don't have an account?{' '}
                    <Link to="/register" className={styles.link}>Register</Link>
                </p>
            </div>
        </div>
    );
}

import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import api from '../api/axios.js';

import styles from '../styles/auth.module.css';

export default function RegsterPage() {
    const { login } = useAuth();
    const navigate = useNavigate();

    const [form, setForm] = useState({ username: "", email: "", password: "" });
    const [errors, setErrors] = useState({});
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target
        setForm(prev => ({ ...prev, [name]: value }))
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setErrors({});

        try {
            const resp = await api.post("/auth/register", form);
            const { userId, username, token } = resp.data;
            login(token, { id: userId, username });
            navigate("/home");
        } catch (err) {
            const data = err.response?.data;
            if (data?.fields) {
                setErrors(data.fields);
            } else {
                setErrors({ general: data?.error || "Registration failed, please try again" });
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={styles.page}>
            <div className={styles.card}>
                <h2 className={styles.title}>Create account</h2>
                {errors.general && <div className={styles.error}>{errors.general}</div>}

                <form onSubmit={handleSubmit} className={styles.form}>
                    <label className={styles.label}>
                        Username
                        <input
                            className={`${styles.input} ${errors.username ? styles.inputError : ""}`}
                            name="username"
                            value={form.username}
                            onChange={handleChange}
                            required
                        />
                        {errors.username && <span className={styles.fieldError}>{errors.username}</span>}
                    </label>

                    <label className={styles.label}>
                        Email
                        <input
                            className={`${styles.input} ${errors.email ? styles.inputError : ""}`}
                            name="email"
                            type="email"
                            value={form.email}
                            onChange={handleChange}
                            required
                        />
                        {errors.email && <span className={styles.fieldError}>{errors.email}</span>}
                    </label>

                    <label className={styles.label}>
                        Password
                        <input
                            className={`${styles.input} ${errors.password ? styles.inputError : ""}`}
                            name="password"
                            type="password"
                            value={form.password}
                            onChange={handleChange}
                            required
                        />
                        {errors.password && <span className={styles.fieldError}>{errors.password}</span>}
                    </label>

                    <button className={styles.button} type="submit" disabled={loading}>
                        {loading ? 'Creating account...' : 'Create account'}
                    </button>
                </form>

                <p className={styles.footer}>
                    Already have an account?{' '}
                    <Link to="/login" className={styles.link}>Sign in</Link>
                </p>
            </div>
        </div>
    );
}

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
        setForm({ ...form, [e.target.name]: e.target.value });
        setErrors({ ...errors, [e.target.name]: "" });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setErrors("");

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
        <div style={styles.page}>
            <div style={styles.card}>
                <h1 style={styles.title}>Create account</h1>
                {errors.general && <div style={styles.error}>{errors.general}</div>}

                <form onSubmit={handleSubmit} style={styles.form}>
                    <label style={styles.label}>
                        Username
                        <input
                            style={{...styles.input, ...(errors.username ? styles.inputError : {})}}
                            name="username"
                            value={form.username}
                            onChange={handleChange}
                            autoComplete="username"
                            required
                        />
                        {errors.username && <span style={styles.fieldError}>{errors.username}</span>}
                    </label>

                    <label style={styles.label}>
                        Email
                        <input
                            style={{...styles.input, ...(errors.email ? styles.inputError : {})}}
                            name="email"
                            type="email"
                            value={form.email}
                            onChange={handleChange}
                            autoComplete="email"
                            required
                        />
                        {errors.email && <span style={styles.fieldError}>{errors.email}</span>}
                    </label>

                    <label style={styles.label}>
                        Password
                        <input
                            style={{...styles.input, ...(errors.password ? styles.inputError : {})}}
                            name="password"
                            type="password"
                            value={form.password}
                            onChange={handleChange}
                            autoComplete="new-password"
                            required
                        />
                        {errors.password && <span style={styles.fieldError}>{errors.password}</span>}
                    </label>

                    <button style={styles.button} type="submit" disabled={loading}>
                        {loading ? 'Creating account...' : 'Create account'}
                    </button>
                </form>

                <p style={styles.footer}>
                    Already have an account?{' '}
                    <Link to="/login" style={styles.link}>Sign in</Link>
                </p>
            </div>
        </div>
    );
}

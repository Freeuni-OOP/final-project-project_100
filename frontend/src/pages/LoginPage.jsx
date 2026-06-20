import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../api/axios.js';

export default function LoginPage() {
    const { login } = useAuth();
    const navigate = useNavigate();

    const [form, setForm] = useState({ username: "", password: "" });
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setForm({ username: e.target.name, password: e.target.password });
        setError("");
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError("");

        try {
            const resp = await api.post("/auth/login", form);
            const { userId, username, token } = resp.data;
            login(token, { id: userId, username });
            navigate("/home");
        } catch (err) {
            setError(err.response?.data?.error || "Login failed, please try again");
        } finally {
            setLoading(false);
        }
    }

    return (
        <div style={styles.page}>
            <div style={styles.card}>
                <h2 style={styles.title}>Sign in</h2>
                {error && <div style={styles.error}>{error}</div>}

                <form onSubmit={handleSubmit} style={styles.form}>
                    <label style={styles.label}>
                        Username
                        <input
                            style={styles.input}
                            name="username"
                            value={form.username}
                            onChange={handleChange}
                            autoComplete="username"
                            required
                        />
                    </label>

                    <label style={styles.label}>
                        Password
                        <input
                            style={styles.input}
                            name="password"
                            type="password"
                            value={form.password}
                            onChange={handleChange}
                            autoComplete="current-password"
                            required
                        />
                    </label>

                    <button style={styles.button} type="submit" disabled={loading}>
                        {loading ? 'Signing in...' : 'Sign in'}
                    </button>
                </form>

                <p style={styles.footer}>
                    Don't have an account?{' '}
                    <Link to="/register" style={styles.link}>Register</Link>
                </p>
            </div>
        </div>
    );
}

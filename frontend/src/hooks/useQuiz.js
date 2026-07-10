import { useState, useRef, useEffect, useCallback } from 'react';
import api from '../api/axios.js';

export default function useQuiz(quizId, isPractice = false) {
    const [quiz, setQuiz] = useState(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState('')
    const [currentIdx, setCurrentIdx] = useState(0)
    const [answers, setAnswers] = useState({})
    const [feedback, setFeedback] = useState(null)
    const [result, setResult] = useState(null)
    const [submitting, setSubmitting] = useState(false)

    const startTime = useRef(null);

    useEffect(() => {
        const fetchQuiz = async () => {
            try {
                const res = await api.get(`/quizzes/${quizId}/start`, {
                    params: { practice: isPractice }
                });
                console.log('Quiz response:', res.data)  // ← add this

                setQuiz(res.data);
                startTime.current = Date.now();
            } catch (err) {
                setError(err.response?.data?.error || "Failed to load the quiz");
            } finally {
                setLoading(false);
            }
        };
        fetchQuiz();
    }, [quizId, isPractice]);

    const setAnswer = (questionId, resp) => {
        setAnswers(prev => ({ ...prev, [questionId]: resp }));
    }

    const currQuestion = quiz?.questions?.[currentIdx];
    const isLastQuestion = quiz && currentIdx === quiz.questions.length - 1;

    const checkCurrAnswer = async () => {
        if (!quiz.immediateFeedback || !currQuestion) return;
        setSubmitting(true);

        try {
            const res = await api.post(`/attemps/${quizId}/check-answer`, {
                questionId: currQuestion.id,
                response: answers[currQuestion.id]
            });
            setFeedback(res.data);
        } finally {
            setSubmitting(false);
        }
    };

    const goNext = () => {
        setFeedBack(null);
        setCurrentIdx(i => Math.min(i+1, quiz.questions.length - 1));
    }

    const goBack = () => {
        setFeedBack(null);
        setCurrentIdx(i => Math.max(i-1, 0));
    }

    const submitQuiz = useCallback(async () => {
        setSubmitting(true);
        setError("");
        const timeTakenSec = Math.round((Date.now() - startTime.current) / 1000);

        const payload = {
            answers: Object.entries(answers).map(([questionId, response]) => ({
                questionId: Number(questionId),
                response
            })),
            isPractice,
            timeTakenSec
        };

        try {
            const res = await api.post(`/attempts/${quizId}/submit`, payload);
            setResult(res.data);
        } catch (err) {
            setError(err.response?.data?.error || "Failed to submit the quiz");
        } finally {
            setSubmitting(false);
        }
    }, [quizId, answers, isPractice]);

    return {
        quiz, loading, error, currQuestion, currentIdx,
        isLastQuestion, answers, setAnswer, feedback,
        checkCurrAnswer, goNext, goBack,
        result, submitQuiz, submitting
    };
}

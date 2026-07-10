import { useParams, useNavigate, useSearchParams } from 'react-router-dom';
import { useEffect } from 'react';
import useQuiz from '../hooks/useQuiz.js';
import QuestionRenderer from '../components/QuestionRenderer.jsx';
import styles from '../styles/quizTakePage.module.css';

export default function QuizTakePage() {
    const { quizId } = useParams()
    const [searchParams] = useSearchParams()
    const isPractice = searchParams.get('practice') === 'true'
    const navigate = useNavigate()

    const {
        quiz, loading, error, currentQuestion, currentIndex,
        isLastQuestion, answers, setAnswer, feedback,
        checkCurrentAnswer, goNext, goBack,
        result, submitQuiz, submitting
    } = useQuiz(quizId, isPractice);


    useEffect(() => {
        if (result) {
            navigate(`/quizzes/${quizId}/results`, { state: { result, isPractice } });
        }
    }, [result, navigate, quizId, isPractice]);

    if (loading) return <div className={styles.center}>Loading quiz...</div>;
    if (error) return <div className={styles.centerError}>{error}</div>;
    if (!quiz) return null;

    if (!quiz.singlePage && !currentQuestion) return null

    if (quiz.singlePage) {
        return (
            <div className={styles.page}>
                <h1 className={styles.title}>{quiz.title}</h1>
                {isPractice && <div className={styles.practiceBadge}>Practice mode</div>}

                <div className={styles.questionList}>
                    {quiz.questions.map((q, i) => (
                        <div key={q.id} className={styles.questionBlock}>
                            <div className={styles.questionPrompt}>
                                {i + 1}. {q.prompt}
                            </div>
                            <QuestionRenderer
                                question={q}
                                value={answers[q.id]}
                                onChange={val => setAnswer(q.id, val)}
                                disabled={submitting}
                            />
                        </div>
                    ))}
                </div>

                <button
                    className={styles.submitBtn}
                    onClick={submitQuiz}
                    disabled={submitting || Object.keys(answers).length === 0}
                >
                    {submitting ? 'Submitting...' : 'Submit Quiz'}
                </button>
            </div>
        )
    }

    return (
        <div className={styles.page}>
            <div className={styles.progressBar}>
                <div
                    className={styles.progressFill}
                    style={{ width: `${((currentIndex + 1) / quiz.questions.length) * 100}%` }}
                />
            </div>
            <div className={styles.progressLabel}>
                Question {currentIndex + 1} of {quiz.questions.length}
                {isPractice && <span className={styles.practiceBadge}>Practice mode</span>}
            </div>

            <div className={styles.questionBlock}>
                <div className={styles.questionPrompt}>{currentQuestion.prompt}</div>
                <QuestionRenderer
                    question={currentQuestion}
                    value={answers[currentQuestion.id]}
                    onChange={val => setAnswer(currentQuestion.id, val)}
                    disabled={submitting || feedback !== null}
                />
            </div>

            {feedback && (
                <div className={`${styles.feedback} ${feedback.correct ? styles.feedbackCorrect : styles.feedbackWrong}`}>
                    {feedback.correct
                        ? 'Correct!'
                        : `Incorrect. Correct answer: ${feedback.correctAnswer}`}
                </div>
            )}

            <div className={styles.navRow}>
                <button
                    className={styles.navBtn}
                    onClick={goBack}
                    disabled={currentIndex === 0 || submitting}
                >
                    Back
                </button>

                {quiz.immediateFeedback && !feedback && (
                    <button
                        className={styles.checkBtn}
                        onClick={checkCurrentAnswer}
                        disabled={!answers[currentQuestion.id] || submitting}
                    >
                        Check Answer
                    </button>
                )}

                {isLastQuestion ? (
                    <button
                        className={styles.submitBtn}
                        onClick={submitQuiz}
                        disabled={submitting}
                    >
                        {submitting ? 'Submitting...' : 'Finish Quiz'}
                    </button>
                ) : (
                        <button
                            className={styles.navBtn}
                            onClick={goNext}
                            disabled={
                                submitting ||
                                    (quiz.immediateFeedback && !feedback) ||
                                    !answers[currentQuestion.id]
                            }
                        >
                            Next
                        </button>
                    )}
            </div>
        </div>
    );
}

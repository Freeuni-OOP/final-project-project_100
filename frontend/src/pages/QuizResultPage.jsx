import { useLocation, useParams, useNavigate, Link } from 'react-router-dom'
import styles from '../styles/quizResultPage.module.css'

export default function QuizResultPage() {
  const location = useLocation()
  const { quizId } = useParams()
  const navigate = useNavigate()
  const result = location.state?.result
  const isPractice = location.state?.isPractice

  if (!result) {
    navigate(`/quizzes/${quizId}`)
    return null
  }

  const percentage = Math.round((result.score / result.totalQuestions) * 100)

  return (
    <div className={styles.page}>
      {isPractice && (
        <div className={styles.practiceNotice}>
          Practice mode — this attempt was not saved to your history
        </div>
      )}

      <div className={styles.scoreCard}>
        <div className={styles.scoreValue}>{result.score} / {result.totalQuestions}</div>
        <div className={styles.scorePercent}>{percentage}%</div>
        <div className={styles.scoreTime}>
          Completed in {formatTime(result.timeTakenSec)}
        </div>
      </div>

      <div className={styles.breakdown}>
        <h2 className={styles.breakdownTitle}>Answer Breakdown</h2>
        {result.results.map((r, i) => (
          <div
            key={r.questionId}
            className={`${styles.resultRow} ${r.correct ? styles.resultCorrect : styles.resultWrong}`}
          >
            <div className={styles.resultIcon}>{r.correct ? '✓' : '✗'}</div>
            <div className={styles.resultDetails}>
              <div className={styles.resultAnswer}>Your answer: {r.userAnswer || '(blank)'}</div>
              {!r.correct && (
                <div className={styles.resultCorrectAnswer}>
                  Correct answer: {r.correctAnswer}
                </div>
              )}
            </div>
          </div>
        ))}
      </div>

      <div className={styles.actions}>
        <Link to={`/quizzes/${quizId}`} className={styles.actionBtn}>
          Back to Quiz
        </Link>
        <Link to="/home" className={styles.actionBtnSecondary}>
          Home
        </Link>
      </div>
    </div>
  )
}

function formatTime(seconds) {
  const m = Math.floor(seconds / 60)
  const s = seconds % 60
  return m > 0 ? `${m}m ${s}s` : `${s}s`
}

import styles from '../styles/questionRenderer.module.css';

export default function QuestionRenderer({ question, value, onChange, disabled }) {
  switch (question.qType) {
    case 'standard':
    case 'fill-in-the-blank':
      return (
        <input
          className={styles.textInput}
          type="text"
          value={value || ''}
          onChange={e => onChange(e.target.value)}
          disabled={disabled}
          placeholder="Type your answer..."
          autoComplete="off"
        />
      );

    case 'multiple-choice':
      return (
        <div className={styles.options}>
          {question.answers.map(opt => (
            opt.answerText?.trim() && (
                <label key={opt.id} className={styles.option}>
                    <input
                        type="radio"
                        name={`question-${question.id}`}
                        value={opt.answerText}
                        checked={value === opt.answerText}
                        onChange={() => onChange(opt.answerText)}
                        disabled={disabled}
                    />
                    <span>{opt.answerText}</span>
                </label>
            )
          ))}
        </div>
      );

    case 'picture-response':
      return (
        <div className={styles.pictureQuestion}>
          <img
            className={styles.questionImage}
            src={question.imageUrl}
            alt="Quiz question"
          />
          <input
            className={styles.textInput}
            type="text"
            value={value || ''}
            onChange={e => onChange(e.target.value)}
            disabled={disabled}
            placeholder="Type your answer..."
            autoComplete="off"
          />
        </div>
      );

    default:
      return <p className={styles.error}>Unsupported question type: {question.qType}</p>;
  }
}

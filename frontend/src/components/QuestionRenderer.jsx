import styles from '../styles/questionRenderer.module.css';

export default function QuestionRenderer({ question, value, onChange, disabled }) {
  switch (question.qType) {
    case 'STANDARD':
    case 'FILL_IN_THE_BLANK':
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

    case 'MULTIPLE_CHOICE':
      return (
        <div className={styles.options}>
          {question.options.map(opt => (
            <label key={opt.id} className={styles.option}>
              <input
                type="radio"
                name={`question-${question.id}`}
                value={opt.id}
                checked={value === opt.id}
                onChange={() => onChange(opt.id)}
                disabled={disabled}
              />
              <span>{opt.text}</span>
            </label>
          ))}
        </div>
      );

    case 'PICTURE_RESPONSE':
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
      return <p className={styles.error}>Unsupported question type</p>;
  }
}

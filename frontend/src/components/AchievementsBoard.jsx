import styles from '../styles/achievements.module.css';

// Import the images dynamically from your folder paths
import amateurAuthorImg from '../assets/badges/amateur_author.png';
import prolificAuthorImg from '../assets/badges/prolific_author.png';
import prodigiousAuthorImg from '../assets/badges/prodigious_author.png';
import quizMachineImg from '../assets/badges/quiz_machine.png';
import greatestImg from '../assets/badges/greatest.png';
import practicePerfectImg from '../assets/badges/practice_perfect.png';

const ACHIEVEMENT_DATA = {
    'amateur_author': { title: 'Amateur Author', desc: 'Created your first quiz.', img: amateurAuthorImg },
    'prolific_author': { title: 'Prolific Author', desc: 'Created multiple quizzes.', img: prolificAuthorImg },
    'prodigious_author': { title: 'Prodigious Author', desc: 'Created a massive library of quizzes.', img: prodigiousAuthorImg },
    'quiz_machine': { title: 'Quiz Machine', desc: 'Completed a large number of quizzes.', img: quizMachineImg },
    'greatest': { title: 'The Greatest', desc: 'Achieved a perfect score.', img: greatestImg },
    'practice_perfect': { title: 'Practice Perfect', desc: 'Retook a quiz to improve a score.', img: practicePerfectImg }
};

const ALL_ACHIEVEMENTS = Object.keys(ACHIEVEMENT_DATA);

export default function AchievementsBoard({ achievements = [] }) {
    // Standardizes incoming array items to simple lowercase strings
    const earnedTypes = achievements.map(ach => {
        const typeString = typeof ach === 'string' ? ach : (ach.achievementType || ach.type || ach.achievement_type || '');
        return typeString.toLowerCase();
    }).filter(Boolean);

    return (
        <div className={styles.boardContainer}>
            <h2 className={styles.header}>Achievements</h2>

            <div className={styles.grid}>
                {ALL_ACHIEVEMENTS.map(key => {
                    const isEarned = earnedTypes.includes(key);
                    const data = ACHIEVEMENT_DATA[key];

                    return (
                        <div key={key} className={`${styles.card} ${isEarned ? styles.earned : styles.locked}`}>
                            <img
                                src={data.img}
                                alt={data.title}
                                className={styles.badgeImage}
                            />

                            <div className={styles.textDetails}>
                                <h4 className={styles.title}>{data.title}</h4>
                                <p className={styles.desc}>{data.desc}</p>
                            </div>

                            {!isEarned && <div className={styles.lockOverlay}>🔒</div>}
                        </div>
                    );
                })}
            </div>
        </div>
    );
}
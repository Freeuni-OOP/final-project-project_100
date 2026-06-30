import React, { useState } from 'react';
import styles from '../styles/quizCreate.module.css';

const STEPS = {
    0: 'Basic Info & Options',
    1: 'Add Questions',
    2: 'Review & Publish'
};

export default function QuizCreatePage() {
    const [currentStep, setCurrentStep] = useState(0);

    const [quizData, setQuizData] = useState({
        title: '',
        description: '',
        randomizeQuestions: false,
        singlePageLayout: true,
        immediateFeedback: false,
        questions: []
    });

    // Dual-purpose handler for text inputs and checkboxes
    const handleInputChange = (e) => {
        const { name, value, type, checked } = e.target;
        setQuizData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleNext = () => {
        if (currentStep === 0 && !quizData.title.trim()) {
            alert("Please enter a quiz title.");
            return;
        }
        setCurrentStep(prev => prev + 1);
    };

    const handleBack = () => {
        setCurrentStep(prev => prev - 1);
    };

    return (
        <div className={styles.container}>
            {/* Progress tracking header */}
            <div className={styles.progressContainer}>
                {Object.keys(STEPS).map((idx) => (
                    <div
                        key={idx}
                        className={`${styles.stepIndicator} ${currentStep >= parseInt(idx) ? styles.activeStep : ''}`}
                    >
                        <span>{parseInt(idx) + 1}</span> {STEPS[idx]}
                    </div>
                ))}
            </div>

            <div className={styles.formWindow}>
                {/* Step 1: Base Configurations */}
                {currentStep === 0 && (
                    <div className={styles.stepForm}>
                        <div className={styles.fieldGroup}>
                            <label htmlFor="title">Quiz Title</label>
                            <input
                                id="title"
                                name="title"
                                type="text"
                                value={quizData.title}
                                onChange={handleInputChange}
                                placeholder="e.g., CS108 Midterm Review"
                            />
                        </div>

                        <div className={styles.fieldGroup}>
                            <label htmlFor="description">Description</label>
                            <textarea
                                id="description"
                                name="description"
                                value={quizData.description}
                                onChange={handleInputChange}
                                placeholder="Provide details about the quiz scope..."
                            />
                        </div>

                        <div className={styles.optionsGroup}>
                            <h3>Quiz Behaviors</h3>

                            <label className={styles.checkboxLabel}>
                                <input
                                    type="checkbox"
                                    name="randomizeQuestions"
                                    checked={quizData.randomizeQuestions}
                                    onChange={handleInputChange}
                                />
                                Randomize question presentation order
                            </label>

                            <label className={styles.checkboxLabel}>
                                <input
                                    type="checkbox"
                                    name="singlePageLayout"
                                    checked={quizData.singlePageLayout}
                                    onChange={handleInputChange}
                                />
                                Display all questions on a single page layout
                            </label>

                            <label className={styles.checkboxLabel}>
                                <input
                                    type="checkbox"
                                    name="immediateFeedback"
                                    checked={quizData.immediateFeedback}
                                    onChange={handleInputChange}
                                />
                                Provide immediate correction feedback to students
                            </label>
                        </div>
                    </div>
                )}

                {/* Question Slot  */}
                {currentStep === 1 && (
                    <div className={styles.questionPlaceholder}>
                        <h3>Questions Configuration Panel</h3>
                        <p>Question creation forms managed via secondary module branch.</p>
                    </div>
                )}

                {/* Global Review Summary */}
                {currentStep === 2 && (
                    <div className={styles.reviewForm}>
                        <h3>Confirm Settings</h3>
                        <p><strong>Title:</strong> {quizData.title || 'Untitled Quiz'}</p>
                        <p><strong>Total Questions:</strong> {quizData.questions.length}</p>
                        <p><strong>Randomization:</strong> {quizData.randomizeQuestions ? 'Enabled' : 'Disabled'}</p>
                        <p><strong>Layout:</strong> {quizData.singlePageLayout ? 'Single Page' : 'Multi-Page Wizard'}</p>
                        <p><strong>Feedback Mode:</strong> {quizData.immediateFeedback ? 'Immediate Correct' : 'On Submit'}</p>
                    </div>
                )}
            </div>

            {/* Control Layer */}
            <div className={styles.navRow}>
                <button
                    disabled={currentStep === 0}
                    onClick={handleBack}
                    className={styles.backBtn}
                >
                    Back
                </button>

                {currentStep < Object.keys(STEPS).length - 1 ? (
                    <button onClick={handleNext} className={styles.nextBtn}>
                        Next
                    </button>
                ) : (
                    <button className={styles.submitBtn}>
                        Publish Quiz
                    </button>
                )}
            </div>
        </div>
    );
}
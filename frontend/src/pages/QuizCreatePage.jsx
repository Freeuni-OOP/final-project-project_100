import { useState } from 'react';
import styles from '../styles/quizCreate.module.css';
import axiosInstance from '../api/axios';
import { useNavigate } from 'react-router-dom';


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
        allowPractice: false,
        questions: []
    });

    const [editingId, setEditingId] = useState(null);

    const [currentQuestion, setCurrentQuestion] = useState({
        type: 'multiple-choice',
        questionText: '',
        options: ['', '', '', ''],
        correctAnswer: '',
        imageUrl: ''
    });

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
        if (currentStep === 1 && quizData.questions.length === 0) {
            alert("Please add at least one question before proceeding.");
            return;
        }
        setCurrentStep(prev => prev + 1);
    };

    const handleBack = () => {
        setCurrentStep(prev => prev - 1);
    };

    const handleOptionChange = (index, value) => {
        const updatedOptions = [...currentQuestion.options];
        updatedOptions[index] = value;
        setCurrentQuestion(prev => ({ ...prev, options: updatedOptions }));
    };

    const startEditingQuestion = (question) => {
        setEditingId(question.id);
        setCurrentQuestion({
            type: question.type,
            questionText: question.questionText,
            options: question.options && question.options.length > 0 ? [...question.options] : ['', '', '', ''],
            correctAnswer: question.correctAnswer,
            imageUrl: question.imageUrl || ''
        });
    };

    const moveQuestionUp = (index) => {
        if(index === 0) return;
        setQuizData(prev => {
            const updated = [...prev.questions];
            [updated[index], updated[index - 1]] = [updated[index - 1], updated[index]];
            return {...prev, questions: updated}
        })
    };

    const moveQuestionDown = (index) => {
        setQuizData(prev => {
            if(index === prev.questions.length - 1) return prev;
            const updated = [...prev.questions];
            [updated[index], updated[index + 1]] = [updated[index + 1], updated[index]];
            return {...prev, questions: updated}
        })
    };

    const duplicateQuesiton = (question) => {
        setQuizData(prev => ({
            ...prev,
            questions: [...prev.questions, {
                ...question,
                id: Date.now(),
                options: question.options ? [...question.options] : []
            }]
        }));
    };

    const addQuestionToQuiz = () => {
        if (!currentQuestion.questionText.trim()) {
            alert("Question text or prompt cannot be empty.");
            return;
        }
        if (currentQuestion.type === 'picture-response' && !currentQuestion.imageUrl.trim()) {
            alert("Picture-Response questions require an absolute Image URL.");
            return;
        }
        if (!currentQuestion.correctAnswer.trim()) {
            alert("Please specify the correct answer target.");
            return;
        }

        setQuizData(prev => {
            let updatedQuestions;
            if(editingId !== null){
                //Map over existing questions to update the one matching editingId.
                updatedQuestions = prev.questions.map(q =>
                    q.id === editingId ? {...currentQuestion, id: editingId} : q
                );
            }
            else{
                //Add new question.
                updatedQuestions = [...prev.questions, {...currentQuestion, id: Date.now()}];
            }
            return {...prev, questions: updatedQuestions};
        });

        // Reset state back to defaults and clear editing lock.
        setEditingId(null);
        setCurrentQuestion({
            type: 'multiple-choice',
            questionText: '',
            options: ['', '', '', ''],
            correctAnswer: '',
            imageUrl: ''
        });
    };

    const removeQuestion = (id) => {
        if(editingId === id){
            setEditingId(null);
            setCurrentQuestion({
                type: 'multiple-choice',
                questionText: '',
                options: ['', '', '', ''],
                correctAnswer: '',
                imageUrl: ''
            });
        }
        setQuizData(prev => ({
            ...prev,
            questions: prev.questions.filter(q => q.id !== id)
        }));
    };

    const navigate = useNavigate();

    const handleSubmit = async () => {
        try {
            await axiosInstance.post('/quizzes/create', quizData);
            navigate('/home');
        } catch (error) {
            console.error('Error saving quiz:', error);
            alert('Failed to save quiz.');
        }
    };

    return (
        <div className={styles.container}>
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
                {/* Configurations */}
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
                                placeholder="e.g., Programming Abstractions Quiz"
                            />
                        </div>

                        <div className={styles.fieldGroup}>
                            <label htmlFor="description">Description</label>
                            <textarea
                                id="description"
                                name="description"
                                value={quizData.description}
                                onChange={handleInputChange}
                                placeholder="Provide an overall description of the purpose of the quiz..."
                            />
                        </div>

                        <div className={styles.optionsGroup}>
                            <h3>Quiz Properties & Options</h3>
                            <label className={styles.checkboxLabel}>
                                <input
                                    type="checkbox"
                                    name="randomizeQuestions"
                                    checked={quizData.randomizeQuestions}
                                    onChange={handleInputChange}
                                />
                                Randomize presentation order of questions
                            </label>

                            <label className={styles.checkboxLabel}>
                                <input
                                    type="checkbox"
                                    name="singlePageLayout"
                                    checked={quizData.singlePageLayout}
                                    onChange={handleInputChange}
                                />
                                Display all questions on a single webpage layout
                            </label>

                            <label className={styles.checkboxLabel}>
                                <input
                                    type="checkbox"
                                    name="immediateFeedback"
                                    checked={quizData.immediateFeedback}
                                    onChange={handleInputChange}
                                />
                                Immediate Correction (Provide feedback immediately item-by-item)
                            </label>

                            <label className={styles.checkboxLabel}>
                                <input
                                    type="checkbox"
                                    name="allowPractice"
                                    checked={quizData.allowPractice}
                                    onChange={handleInputChange}
                                />
                                Allow practice mode on quiz
                            </label>
                        </div>
                    </div>
                )}

                {/*Question builder that has all 4 required types */}
                {currentStep === 1 && (
                    <div className={styles.stepForm}>
                        <h3>Build Your Questions</h3>

                        {quizData.questions.length > 0 && (
                            <div style={{ marginBottom: '1.5rem', borderBottom: '1px dashed #ccc', paddingBottom: '1rem' }}>
                                <h4>Added Questions ({quizData.questions.length})</h4>
                                {quizData.questions.map((q, index) => (
                                    <div key={q.id} style={{ display: 'flex', justifyContent: 'space-between', padding: '0.5rem 0', fontSize: '0.9rem', alignItems: 'center', borderBottom: '1px solid #f0f0f0' }}>
                                        <span style={{maxWidth: '60%'}}>{index + 1}. <strong>[{q.type.toUpperCase()}]</strong> {q.questionText}</span>
                                        <div style={{display: 'flex', gap: '8px', alignItems: 'center' }}>
                                            <button onClick={() => startEditingQuestion(q)}
                                                    style={{ color: 'blue', background: 'none', border: 'none', cursor: 'pointer'}}>Edit</button>
                                            <button onClick={() => duplicateQuesiton(q)}
                                                    style={{ color: 'pink', background: 'none', border: 'none', cursor: 'pointer'}}>Duplicate</button>
                                            <button onClick={() => removeQuestion(q.id)}
                                                    style={{ color: 'red', background: 'none', border: 'none', cursor: 'pointer'}}>Delete</button>
                                            <button disabled={index === 0} onClick={() => moveQuestionUp(index)}
                                                    style={{ background: 'none', border: 'none', cursor: index === 0 ? 'not-allowed' : 'pointer', opacity: index === 0 ? 0.3 : 1}}
                                                    title="Move Up">↑</button>
                                            <button disabled={index === quizData.questions.length - 1} onClick={() => moveQuestionDown(index)}
                                                    style={{ background: 'none', border: 'none', cursor: index === quizData.questions.length - 1 ? 'not-allowed' : 'pointer', opacity: index === quizData.questions.length - 1 ? 0.3 : 1}}
                                                    title="Move down">↓</button>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        )}

                        <div className={styles.fieldGroup}>
                            <label>Required Question Type</label>
                            <select
                                value={currentQuestion.type}
                                onChange={(e) => setCurrentQuestion(prev => ({
                                    ...prev,
                                    type: e.target.value,
                                    options: e.target.value === 'multiple-choice' ? ['', '', '', ''] : [],
                                    correctAnswer: '',
                                    imageUrl: ''
                                }))}
                            >
                                <option value="multiple-choice">Multiple Choice (Radio Buttons)</option>
                                <option value="question-response">Question-Response (Standard Text)</option>
                                <option value="fill-in-the-blank">Fill in the Blank</option>
                                <option value="picture-response">Picture-Response Questions</option>
                            </select>
                        </div>

                        {/* Renders an image URL text input explicitly for Picture-Response types */}
                        {currentQuestion.type === 'picture-response' && (
                            <div className={styles.fieldGroup}>
                                <label>Absolute External Image URL</label>
                                <input
                                    type="text"
                                    value={currentQuestion.imageUrl}
                                    onChange={(e) => setCurrentQuestion(prev => ({ ...prev, imageUrl: e.target.value }))}
                                    placeholder="e.g., http://events.stanford.edu/.../Memchu_small.jpg"
                                />
                            </div>
                        )}

                        <div className={styles.fieldGroup}>
                            <label>
                                {currentQuestion.type === 'fill-in-the-blank'
                                    ? "Question Text (Use underscores for the blank, e.g., 'Lincoln gave the __________ Address')"
                                    : "Question Prompt / Text"}
                            </label>
                            <input
                                type="text"
                                value={currentQuestion.questionText}
                                onChange={(e) => setCurrentQuestion(prev => ({ ...prev, questionText: e.target.value }))}
                                placeholder="Enter the prompt content..."
                            />
                            <div style={{ textAlign: 'right', fontSize: '0.8rem', marginTop: '0.25rem',
                                color: currentQuestion.questionText.length > 150 ? 'red' : 'green'
                            }}>{currentQuestion.questionText.length} / 150 characters</div>
                        </div>

                        {currentQuestion.type === 'multiple-choice' && (
                            <div className={styles.fieldGroup}>
                                <label>Choices</label>
                                {currentQuestion.options.map((opt, i) => (
                                    <input
                                        key={i}
                                        type="text"
                                        value={opt}
                                        onChange={(e) => handleOptionChange(i, e.target.value)}
                                        placeholder={`Option ${i + 1}`}
                                        style={{ marginBottom: '0.5rem' }}
                                    />
                                ))}
                            </div>
                        )}

                        <div className={styles.fieldGroup}>
                            <label>Correct Answer Target</label>
                            <input
                                type="text"
                                value={currentQuestion.correctAnswer}
                                onChange={(e) => setCurrentQuestion(prev => ({ ...prev, correctAnswer: e.target.value }))}
                                placeholder="Expected text or exact multiple choice match option string"
                            />
                        </div>

                        <button
                            type="button"
                            onClick={addQuestionToQuiz}
                            disabled={currentQuestion.questionText.length > 150}
                            style={{ padding: '0.6rem 1rem',
                                background: currentQuestion.questionText.length > 150 ? 'gray' : editingId !== null ? '#1a73e8' : '#34a853',
                                color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer', fontWeight: 'bold' }}
                        >
                            {editingId !== null ? 'Update Question in List' : '+ Save Question to List'}
                        </button>
                    </div>
                )}

                {/* global review summary */}
                {currentStep === 2 && (
                    <div className={styles.reviewForm}>
                        <h3>Confirm Settings & Publish Quiz</h3>
                        <p><strong>Title:</strong> {quizData.title || 'Untitled Quiz'}</p>
                        <p><strong>Total Structured Questions:</strong> {quizData.questions.length}</p>
                        <p><strong>Randomization:</strong> {quizData.randomizeQuestions ? 'Enabled' : 'Disabled'}</p>
                        <p><strong>Layout Format:</strong> {quizData.singlePageLayout ? 'Single Page' : 'One Question Per Page (Flashcard style)'}</p>
                        <p><strong>Feedback Correction:</strong> {quizData.immediateFeedback ? 'Immediate Correction Enabled' : 'Graded on full submission'}</p>
                    </div>
                )}
            </div>

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
                    <button onClick={handleSubmit} className={styles.submitBtn}>
                        Publish Quiz
                    </button>
                )}
            </div>
        </div>
    );
}
# DailyRoundsQz
Features & Functionality
# 🎨 Modern Material Design
Built with Jetpack Compose and Material3 for a smooth, responsive UI.

Fully supports light and dark themes, with a persistent manual theme switch (sun/moon icon in switch thumb).

# 🚀 Quiz Flow
Splash Screen: Animated splash with Lottie and app branding.

Quiz Screen:

Displays one question at a time with multiple-choice options.

Options highlight green for correct and red for incorrect answers.

Animated streak indicator with fire icons and celebratory Lottie animation for consecutive correct answers.

Progress bar and question counter.

Swipe gestures (left/right) and navigation arrows for moving between questions.

Skip button to move past a question.

Global timer (15 minutes for the whole test) shown at the top right with a timer icon and colored background.

Finish Test button to end the quiz at any time.

On the last question, selecting an answer reveals feedback but does not auto-advance or end the quiz; user must press "Finish Test" to complete.

# 📊 Results & Feedback
Results Screen:

Displays total score, number of correct answers, and longest streak.

Shows detailed feedback for each question (user's answer, correct answer).

Streak Tracking:

Fire icons light up for each consecutive correct answer.

Special animation when a streak milestone is reached.

# 🌗 Theming
Manual Theme Toggle: Switch between light and dark mode at any time, with sun/moon icon in the switch.

Persistent Theme: User’s theme preference is saved using DataStore and restored on app restart.

# 🔗 Data & Architecture
Questions loaded from API (Retrofit).

MVVM architecture with state management in ViewModel.

Persistent theme state using Jetpack DataStore.

Composable navigation with NavHost.

# 🛡️ User Experience
Loading and error handling: Animated progress indicator during data fetch, retry option on failure.

Responsive design: Works on phones and tablets.

Accessible: High-contrast colors, large touch targets, and descriptive icons.

# Feature Table
Feature	Description
Splash Screen	Animated intro with branding
Quiz Navigation	Swipe gestures & arrow buttons
Option Highlight	Green/red feedback, rounded cards
Streak Indicator	Fire icons, Lottie animation for streaks
Timer	15-minute global timer, timer icon, colored background
Finish Test	Button to end quiz anytime, required after last question
Results	Score, streak, detailed answer review
Theme Switch	Sun/moon icon in switch, persistent with DataStore
Error Handling	Retry on network/API failure
MVVM + DataStore	Modern architecture, persistent user settings

# How to Use
Start the app: Splash screen with animation.

Take the quiz: Answer questions, use arrows or swipe to navigate, skip if needed.

Watch your streak: Consecutive correct answers light up fire icons and trigger animations.

Monitor time: Timer is always visible at the top right.

Finish the quiz: Press "Finish Test" when done (required after last question).

View results: See your score, streak, and detailed feedback.

Switch theme: Toggle light/dark mode anytime with the sun/moon switch.

This app is designed for a modern, enjoyable, and educational quiz experience with robust feedback and customization.

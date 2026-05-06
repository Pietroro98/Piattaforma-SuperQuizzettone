import { Question } from "./question.model";
import { User } from "./user.model";


export interface Quiz {
    id: number;
    description: string;
    name: string; //unique
    questions: Question[];
    quizTime: string;
    totalPoints: number;
    category: Category[];
    attempts: QuizPlayed[];
}

export interface QuizPlayed {
    id: number;
    user: User;
    quiz: Quiz;
    score: number;
    completionDate: string;
    timeSpent: string;
}

export interface Category {
    id: number;
    name: string; //unique
    description: string;
}
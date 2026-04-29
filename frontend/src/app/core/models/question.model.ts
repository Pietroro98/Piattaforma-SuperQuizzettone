import { Category } from "./quiz.model";

export interface Question {
    id: number;
    description: string;
    answers: Answer[];
    category: Category;
    tag: string;
    motivationRejection?: string;
}

export interface Answer {
    id: number;
    description: string;
    isCorrect: boolean;
    //idQuestion: number;
}

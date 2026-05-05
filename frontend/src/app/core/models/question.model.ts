import { Category } from "./quiz.model";

export interface Question {
    id: number;
    description: string;
    answers: Answer[];
    category: Category;
    tag: string;
    status?: string;
    type?: string;
    motivationRejection?: string;
}

export interface Answer {
    id: number;
    description: string;
    isCorrect: boolean;
    //idQuestion: number;
}

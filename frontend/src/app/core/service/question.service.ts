import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { StorageService } from "./storage.service";
import { Question } from "../models/question.model";
import { map, Observable } from "rxjs";
import { Category } from "../models/quiz.model";

@Injectable({ providedIn: 'root' })
export class QuestionService {

  private readonly http = inject(HttpClient);
  private readonly storage = inject(StorageService);
  baseUrl: string = 'http://localhost:8080/superQuizzettone/api';


  createQuestion(payload: Question): Observable<any> {
    return this.http.post<Question>(`${this.baseUrl}/writer/create-question`, payload);
  }

  saveDraft(payload: Question): Observable<any> {
    return this.http.post<Question>(`${this.baseUrl}/writer/save-draft`, payload);
  }

  getAllCategory(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/open-controller/categories-list`)
     .pipe(
          map((response) => response.data));
  }

getQuestionsInReview() : Observable<any> {
  return this.http.get(`${this.baseUrl}/open-controller/all-questions`);
}


}
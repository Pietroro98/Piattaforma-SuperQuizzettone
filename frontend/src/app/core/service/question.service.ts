import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { StorageService } from "./storage.service";
import { Question } from "../models/question.model";
import { Observable } from "rxjs";

@Injectable({ providedIn: 'root' })
export class QuestionService {

  private readonly http = inject(HttpClient);
  private readonly storage = inject(StorageService);
  baseUrl: string = 'http://192.168.5.73:8080/superQuizzettone/api'; 


  createQuestion(payload: Question): Observable<any> {
    return this.http.post<Question>(`${this.baseUrl}/writer/create-question`, payload);
  }

  getAllCategory(): Observable<any> {
    return this.http.get<Question>(`${this.baseUrl}/open-controller/categories-list`);
  }

}
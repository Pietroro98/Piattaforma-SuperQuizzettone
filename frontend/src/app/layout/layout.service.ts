import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LayoutService {

  private collapsedSubject = new BehaviorSubject<boolean>(true);

  collapsed$ = this.collapsedSubject.asObservable();

  toggle() {
    this.collapsedSubject.next(!this.collapsedSubject.value);
  }

  setCollapsed(value: boolean) {
    this.collapsedSubject.next(value);
  }
}

import {BehaviorSubject, Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {UserModel} from '../../models/user/user.model';

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  public currentUser: Observable<UserModel>;
  private currentUserSubject: BehaviorSubject<UserModel>;

  constructor() {
    this.currentUserSubject = new BehaviorSubject<UserModel>(JSON.parse(sessionStorage.getItem('user')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  get getUser(): UserModel {
    return this.currentUserSubject.getValue();
  }

  setUser(value: any): void {
    this.currentUserSubject.next(value);
  }
}

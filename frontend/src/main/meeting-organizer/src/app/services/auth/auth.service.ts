import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {UserModel} from '../../models/user/user.model';
// import {UserLoginResponseModel} from '../../models/user/user-login-response.model';
import {StorageService} from './storage.service';
import {UserLoginResponseModel} from "../../models/user/user-login-response.model";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient, private storageService: StorageService) {
  }

  register(user: UserModel): Observable<any> {
    return this.http.post('/api/v1/user', user, {responseType: 'text'});
  }

  login(user: UserModel): Observable<UserLoginResponseModel> {
    return this.http.post<UserLoginResponseModel>('/api/v1/user/login', user, httpOptions);
  }

  logout(): void {
    window.sessionStorage.removeItem('token');
    window.sessionStorage.removeItem('user');
    this.storageService.setUser(null);
  }

  forgotPassword(email: string): Observable<any> {
    return this.http.post('/api/v1/user/forgot-password', {email}, httpOptions);
  }

  confirmResetPassword(token: string): Observable<any> {
    return this.http.get(`/api/v1/user/forgot-password/confirm-reset?token=${token}`, {responseType: 'text'});
  }

  resetPassword(email: string, password: string): Observable<any> {
    return this.http.post(`/api/v1/user/forgot-password/reset-password`, {email, password}, httpOptions);
  }
}

import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class TokenVerifyService {

  constructor(private http: HttpClient) {
  }

  confirmRegistration(token: string): Observable<any> {
    return this.http.get(`/api/v1/user/verify/registration-confirm?token=${token}`, {responseType: 'text'});
  }

  resendRegistrationToken(token: string): Observable<any> {
    return this.http.get(`/api/v1/user/verify/resend-registration-token?token=${token}`, {responseType: 'text'});
  }

}

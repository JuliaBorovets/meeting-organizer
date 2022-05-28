import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserModel} from '../models/user/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getUserById(id: any): Observable<UserModel> {
    return this.http.get<UserModel>('/api/v1/user/' + id);
  }

  updateUser(user: any): Observable<UserModel> {
    return this.http.put<UserModel>(`/api/v1/user`, user);
  }

  uploadUserImage(userId: number, photo: any): Observable<any> {
    return Observable.create ((observer)  => {
      const xhr = new XMLHttpRequest();
      xhr.onreadystatechange = ()  => {
        if (xhr.readyState === 4) {
          if (xhr.status === 200) {
            observer.next(xhr);
          } else {
            observer.error(xhr);
          }
        }
      };
      xhr.open('PATCH', `/api/v1/user/image/${userId}`);
      xhr.send(photo);
    });
  }

}

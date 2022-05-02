import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ContactUsModel} from '../models/contact-us/contact-us.model';

@Injectable({
  providedIn: 'root'
})
export class ContactUsService {

  constructor(private http: HttpClient) {
  }

  create(contactUsModel: ContactUsModel): Observable<ContactUsModel> {
    return this.http.post<ContactUsModel>('/api/v1/contact-us', contactUsModel);
  }

}

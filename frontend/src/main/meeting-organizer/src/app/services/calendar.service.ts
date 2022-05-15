import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {EventInput} from '@fullcalendar/common';

@Injectable({
  providedIn: 'root'
})
export class CalendarService {

  constructor(private http: HttpClient) {
  }


  findAllByUser(userId: number): Observable<EventInput[]> {
    let params = new HttpParams();
    params = params.append('userId', String(userId));

    return this.http.get<EventInput[]>(`/api/v1/calendar`, {params});
  }

}

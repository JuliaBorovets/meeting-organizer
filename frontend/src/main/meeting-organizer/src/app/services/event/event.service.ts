import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {EventModel} from '../../models/event/event.model';
import {EventFilterModel} from '../../models/event/event-filter.model';
import {EventResponseModel} from '../../models/event/event-response.model';


@Injectable({
  providedIn: 'root'
})
export class EventService {

  constructor(private http: HttpClient) {
  }

  create(event: any): Observable<any> {
    return this.http.post<any>('/api/v1/event', event);
  }

  findAllByLibraryId(filter: EventFilterModel): Observable<EventResponseModel> {
    let params = new HttpParams();
    const libraryId = filter.libraryId;
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    if (filter.streamId) {
      params = params.append('streamId', String(filter.streamId));
    }
    return this.http.get<EventResponseModel>(`/api/v1/event/library/${libraryId}`, {params});
  }

  findAllByLibraryModelId(libraryId: number): Observable<EventResponseModel> {
    return this.http.get<EventResponseModel>(`/api/v1/event/library/${libraryId}`);
  }

  findAllByStreamNotContaining(libraryId: number, streamId: number, name: string): Observable<EventModel[]> {
    let params = new HttpParams();
    params = params.append('name', name);
    params = params.append('streamId', String(streamId));
    return this.http.get<EventModel[]>(`/api/v1/event/stream/not/${libraryId}`, {params});
  }

  deleteEvent(id: number): Observable<any> {
    return this.http.delete<string>(`/api/v1/event/${id}`);
  }

  updateEvent(event: EventModel): Observable<EventModel> {
    return this.http.put<EventModel>('/api/v1/event', event);
  }
}

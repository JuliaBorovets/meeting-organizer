import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {StreamModel} from '../../models/stream/stream.model';
import {StreamResponseModel} from '../../models/stream/stream-response.model';
import {StreamFilterModel} from '../../models/stream/stream-filter.model';

@Injectable({
  providedIn: 'root'
})
export class StreamService {

  constructor(private http: HttpClient) {
  }

  create(stream: StreamModel): Observable<StreamModel> {
    return this.http.post<StreamModel>('/api/v1/stream', stream);
  }

  findAllByLibraryId(filter: StreamFilterModel): Observable<StreamResponseModel> {
    let params = new HttpParams();
    const libraryId = filter.libraryId;
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    if (filter.streamName) {
      params = params.append('streamName', String(filter.streamName));
    }
    return this.http.get<StreamResponseModel>(`/api/v1/stream/list/${libraryId}`, {params});
  }

  deleteStream(id: number): Observable<any> {
    return this.http.delete<string>(`/api/v1/stream/${id}`);
  }

  updateStream(stream: StreamModel): Observable<StreamModel> {
    return this.http.put<StreamModel>('/api/v1/stream', stream);
  }

  addEventToStream(streamId: number, eventIdsList: string[]): Observable<StreamModel> {
    return this.http.patch<StreamModel>(`/api/v1/stream/add_event`, {eventIdsList, streamId});
  }

  deleteEventFromStream(eventId: number, streamId: number): Observable<StreamModel> {
    return this.http.patch<StreamModel>(`/api/v1/stream/remove_event?eventId=${eventId}&streamId=${streamId}`, {});
  }
}

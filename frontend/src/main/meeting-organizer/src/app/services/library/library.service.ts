import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {LibraryModel} from '../../models/library/library.model';
import {LibraryFilterModel} from '../../models/library/library-filter.model';
import {LibraryResponseModel} from '../../models/library/library-response.model';
import {LibraryCreateModel} from '../../models/library/library-create.model';
import {StreamModel} from "../../models/stream/stream.model";

@Injectable({
  providedIn: 'root'
})
export class LibraryService {

  constructor(private http: HttpClient) {
  }

  create(library: LibraryCreateModel): Observable<LibraryModel> {
    return this.http.post<LibraryModel>('/api/v1/library', library);
  }

  findAll(filter: LibraryFilterModel): Observable<LibraryResponseModel> {
    let params = new HttpParams();
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    params = params.append('userId', String(filter.userId));
    if (filter.libraryName) {
      params = params.append('libraryName', String(filter.libraryName));
    }

    return this.http.get<LibraryResponseModel>(`/api/v1/library`, {params});
  }

  findAllUser(filter: LibraryFilterModel): Observable<LibraryResponseModel> {
    let params = new HttpParams();
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    params = params.append('userId', String(filter.userId));
    if (filter.libraryName) {
      params = params.append('libraryName', String(filter.libraryName));
    }

    return this.http.get<LibraryResponseModel>(`/api/v1/library/list`, {params});
  }

  findUserFavorites(filter: LibraryFilterModel): Observable<LibraryResponseModel> {
    let params = new HttpParams();
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    params = params.append('userId', String(filter.userId));
    if (filter.libraryName) {
      params = params.append('libraryName', String(filter.libraryName));
    }

    return this.http.get<LibraryResponseModel>(`/api/v1/library/favorite`, {params});
  }

  deleteLibrary(id: number): Observable<any> {
    return this.http.delete<string>(`/api/v1/library/${id}`);
  }

  getLibraryById(libraryId: number): Observable<LibraryModel> {
    return this.http.get<LibraryModel>(`/api/v1/library/${libraryId}`);
  }

  updateLibrary(library: LibraryCreateModel): Observable<LibraryModel> {
    return this.http.put<LibraryModel>('/api/v1/library', library);
  }

  addLibraryToFavorites(libraryId: number, userId: number): Observable<LibraryModel> {
    let params = new HttpParams();
    params = params.append('libraryId', String(libraryId));
    params = params.append('userId', String(userId));
    return this.http.put<LibraryModel>('/api/v1/library/favorite', {}, {params});
  }

  deleteLibraryFromFavorites(libraryId: number, userId: number): Observable<LibraryModel> {
    let params = new HttpParams();
    params = params.append('libraryId', String(libraryId));
    params = params.append('userId', String(userId));
    return this.http.delete<LibraryModel>('/api/v1/library/favorite', {params});
  }

  findAllUserAccess(filter: LibraryFilterModel): Observable<LibraryResponseModel> {
    let params = new HttpParams();
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    params = params.append('userId', String(filter.userId));
    if (filter.libraryName) {
      params = params.append('libraryName', String(filter.libraryName));
    }

    return this.http.get<LibraryResponseModel>(`/api/v1/library/access`, {params});
  }

  addAccessToLibraryByToken(request: any): Observable<LibraryModel> {
    return this.http.put<LibraryModel>('/api/v1/library/access/token', request);
  }

  removeAccessToLibraryByUserEmail(emailList: string[], libraryId: number): Observable<LibraryModel> {
    let params = new HttpParams();
    params = params.append('emailList', String(emailList));
    params = params.append('libraryId', String(libraryId));
    return this.http.delete<LibraryModel>('/api/v1/library/access', {params});
  }

  addEventToLibrary(libraryId: number, eventIdsList: string[]): Observable<LibraryModel> {
    return this.http.patch<LibraryModel>(`/api/v1/library/add_event`, {eventIdsList, libraryId});
  }

  deleteEventFromLibrary(eventId: number, libraryId: number): Observable<StreamModel> {
    return this.http.patch<StreamModel>(`/api/v1/library/remove_event?eventId=${eventId}&libraryId=${libraryId}`, {});
  }
}

import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {LibraryModel} from '../../models/library/library.model';
import {LibraryFilterModel} from '../../models/library/library-filter.model';
import {LibraryResponseModel} from '../../models/library/library-response.model';

@Injectable({
  providedIn: 'root'
})
export class LibraryService {

  constructor(private http: HttpClient) {
  }

  create(library: LibraryModel): Observable<LibraryModel> {
    return this.http.post<LibraryModel>('/api/v1/library', library);
  }

  findAll(filter: LibraryFilterModel): Observable<LibraryResponseModel> {
    let params = new HttpParams();
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    params = params.append('userId', String(filter.userId));

    return this.http.get<LibraryResponseModel>(`/api/v1/library`, {params});
  }
}

import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CommentResponseModel} from '../../models/reaction/comment-respomse.model';
import {CommentFilterModel} from '../../models/reaction/comment-filter.model';
import {CommentModel} from '../../models/reaction/comment.model';

@Injectable({
  providedIn: 'root'
})
export class ReactionService {

  constructor(private http: HttpClient) {
  }


  findAllByEvent(filter: CommentFilterModel): Observable<CommentResponseModel> {
    let params = new HttpParams();
    params = params.append('pageSize', String(filter.pageSize));
    params = params.append('pageNumber', String(filter.pageNumber));
    params = params.append('eventId', String(filter.eventId));

    return this.http.get<CommentResponseModel>(`/api/v1/comment`, {params});
  }

  createComment(comment: any): Observable<CommentModel> {
    return this.http.post<CommentModel>('/api/v1/comment', comment);
  }

  deleteComment(id: number): Observable<any> {
    return this.http.delete<string>(`/api/v1/comment/${id}`);
  }

}

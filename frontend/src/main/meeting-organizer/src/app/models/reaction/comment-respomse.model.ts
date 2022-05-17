import {CommentModel} from './comment.model';

export class CommentResponseModel {
  list: CommentModel[];
  totalItems: number;
}

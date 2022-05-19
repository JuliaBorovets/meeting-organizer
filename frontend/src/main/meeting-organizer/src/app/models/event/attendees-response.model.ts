import {UserModel} from '../user/user.model';

export class AttendeesResponseModel {
  list: UserModel[];
  totalItems: number;
}

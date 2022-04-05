export class UserLoginResponseModel {
  id: number;
  username: string;
  email: string;
  token: string;
  roles: Array<string>;
}

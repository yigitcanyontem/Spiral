import { Users } from './users';

export interface FavGame {
  id: number;
  usersid: Users;
  gameid: string;
  gamename: string;
  gameimage: string;
}

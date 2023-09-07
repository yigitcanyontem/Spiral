import { EntertainmentType } from '../enums/entertainment-type';
import { Users } from './users';
import { Entertainment } from './entertainment';

export interface Review {
  id: number;
  usersid: Users;
  entertainmentType: EntertainmentType;
  entertainmentid: string;
  entertainmentTitle: string;
  description: string | null;
  title: string | null;
  rating: number;
  upvote: number;
  downvote: number;
  date: Date;
  entertainmentImage: string;
}

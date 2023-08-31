import {EntertainmentType} from "../enums/entertainment-type";
import {Users} from "./users";

export interface Review {
  id: number;
  usersid: Users;
  entertainmentType: EntertainmentType;
  entertainmentid: string;
  description: string | null;
  rating: number;
  upvote: number;
  downvote: number;
  date: Date;
}


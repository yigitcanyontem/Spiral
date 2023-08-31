import {EntertainmentType} from "../enums/entertainment-type";

export interface ReviewCreateDTO {
  usersid: number;
  entertainmentType: EntertainmentType;
  entertainmentid: string;
  description: string;
  rating: number;
}


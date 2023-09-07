import { EntertainmentType } from '../enums/entertainment-type';

export interface ReviewCreateDTO {
  usersid: number;
  entertainmentType: EntertainmentType;
  title: string;
  entertainmentid: string;
  description: string;
  rating: number;
  entertainmentTitle: string;
  entertainmentImage: string;
}

import { CrewMember } from "./crew-member";
import {Entertainment} from "./entertainment";

export interface Show extends Entertainment{
  id: string;
  adult: boolean;
  backdrop_path: string;
  original_title: string;
  overview: string;
  poster_path: string;
  first_air_date: string;
  last_air_date: string;
  original_language: string;
  status: string;
  vote_count: number;
  favorite_count: number;
  producers: CrewMember[];
  actors: CrewMember[];
}

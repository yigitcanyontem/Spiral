import { CrewMember } from "./crew-member";

export interface Show {
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

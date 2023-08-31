import {CrewMember} from "./crew-member";

export interface Movie {
  id: string;
  adult: boolean;
  backdrop_path: string;
  original_title: string;
  overview: string;
  poster_path: string;
  release_date: string;
  language: string[];
  imdb_url: string;
  vote_count: number;
  favorite_count: number;
  genres: string[];
  tagline: string;
  directors: CrewMember[];
  actors: CrewMember[];
  writers: CrewMember[];
}

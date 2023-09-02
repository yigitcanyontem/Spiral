import {Entertainment} from "./entertainment";
import {Character} from "./character";

export interface Game extends Entertainment{
  id: string;
  name: string;
  releaseDate: string;
  platforms: string[];
  franchises: string[];
  genres: string[];
  developers: string[];
  publishers: string[];
  similar_games: string[];
  screenshots: string[];
  original_url: string;
  icon_url: string;
  screen_large_url: string;
  deck: string;
  characters: Character[];
  favorite_count: number;
}


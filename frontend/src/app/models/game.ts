export interface Game {
  id: string;
  name: string;
  releaseDate: string;
  platforms: string[];
  franchises: string[];
  genres: string[];
  developers: string[];
  publishers: string[];
  similar_games: string[];
  original_url: string;
  icon_url: string;
  screen_large_url: string;
  deck: string;
  characters: string[];
  favorite_count: number;
}


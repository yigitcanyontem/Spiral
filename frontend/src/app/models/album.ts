import {Entertainment} from "./entertainment";

export interface Album extends Entertainment{
  mbid: string;
  name: string;
  artist: string;
  image: string;
  url: string;
  favorite_count: number;
}

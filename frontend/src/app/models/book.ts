import {Entertainment} from "./entertainment";

export interface Book extends Entertainment{
  id: string;
  title: string;
  authors: string[];
  categories: string[];
  description: string;
  pageCount: number;
  cover_url: string;
  language: string;
  publishedDate: string;
  webReaderLink: string;
  favorite_count: number;
}

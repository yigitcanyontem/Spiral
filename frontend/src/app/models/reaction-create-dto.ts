import {ReactionType} from "../enums/reaction-type";

export interface ReactionCreateDto {
  usersid:number,
  reviewid:number,
  reactionType:ReactionType
}

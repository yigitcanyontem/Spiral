import {Users} from "./users";
import {Review} from "./review";
import {ReactionType} from "../enums/reaction-type";

export interface ReviewReaction {
  id:number,
  usersid:Users,
  reviewid:Review,
  reactionType:ReactionType
}

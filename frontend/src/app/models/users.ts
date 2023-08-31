import {Status} from "../enums/status";
import {Role} from "../enums/role";
import {Country} from "./country";

export interface Users {
  id: number;
  firstName: string;
  lastName: string;
  date_of_birth: Date;
  country: Country;
  email: string;
  username: string;
  password: string;
  role: Role;
  status: Status;
}

import {Country} from "./country";

export interface UserDTO {
  id: number;
  firstName: string;
  lastName: string;
  date_of_birth: Date;
  country: Country;
  email: string;
  username: string;
}

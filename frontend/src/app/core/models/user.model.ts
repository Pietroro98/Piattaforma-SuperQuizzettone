export interface User {
  id: number;
  name: string;
  surname: string;
  username: string;
  password: string;
  creationDate: string;
  state: UserState;
  totalPoints: number;
  attempts: number;
  roles: Role[];
}

export enum UserState {
  ATTIVO = "ATTIVO",
  DISABILITATO = "DISABILITATO"
}

export enum Role {
  ADMIN = "ROLE_ADMINISTRATOR",
  ORGANIZER = "ROLE_WRITER",
  REVIEWER = "ROLE_REVIEWER",
  PLAYER = "ROLE_PLAYER"
}

export interface AuthResponse {
  token: string;
  user: Omit<User, 'password'>;
}

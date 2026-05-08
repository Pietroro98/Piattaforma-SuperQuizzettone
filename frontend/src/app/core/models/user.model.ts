export interface User {
  nome: string;
  cognome: string;
  username: string;
  password: string;
  dataRegistrazione: string;
  stato: string;
  totalPoint: number;
  attempts: any[];
  roles: string[];
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
  status: number;
  message: string;
  data: Omit<User, 'password'>;
}
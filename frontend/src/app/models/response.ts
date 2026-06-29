export interface AuthResponseLogin {
  success: boolean;
  token?: string;
  user?: {
    email: string;
    username: string;
    id: number;
  };
}

export interface AuthResponseRegister {
  success: boolean;
  id: number;
}

export interface AuthResponseToken {
  message: string;
}

export interface ResponseToken {
  token: string;
}

export interface AuthResponse {
  id: number;
  success: boolean;
  token: string;
}

export interface AuthResponsePassword {
  message: string;
  success: boolean;
}

export interface AuthResponseLoginFireBase {
  success: boolean;
  id: number;
  token: string;
  username: string;
}

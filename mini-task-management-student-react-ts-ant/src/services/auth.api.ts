import axios from "./axios.customize";

export interface LoginRequest {
    email: string;
    password: string;
}

export interface LoginResponse {
    statusCode: number;
    message: string;
    data: {
        accessToken: string;
        refreshToken: string;
        user: {
            id: number;
            email: string;
            name: string;
        };
    };
}

export const authApi = {
    login: async (email: string, password: string): Promise<LoginResponse> => {
        const res = await axios.post("http://localhost:8080/api/v1/auth/login", {
            email,
            password,
        });
        return res;
    },
};

export default authApi;

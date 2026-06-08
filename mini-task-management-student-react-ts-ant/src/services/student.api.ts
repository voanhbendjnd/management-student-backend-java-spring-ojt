import axios from "./axios.customize";

const base = "/api/v1/students";

export const studentApi = {
    getAll: async () => {
        const res = await axios.get(base);
        return (res as any)?.data ?? res;
    },
    getById: async (id: number) => {
        const res = await axios.get(`${base}/${id}`);
        return (res as any)?.data ?? res;
    },
    delete: async (id: number) => {
        const res = await axios.delete(`${base}/${id}`);
        return (res as any)?.data ?? res;
    },
    create: async (payload: any) => {
        const res = await axios.post(base, payload);
        return (res as any)?.data ?? res;
    },
    update: async (id: number, payload: any) => {
        const res = await axios.put(`${base}/${id}`, payload);
        return (res as any)?.data ?? res;
    },
};

export default studentApi;

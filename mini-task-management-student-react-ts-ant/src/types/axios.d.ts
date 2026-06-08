declare module 'axios' {
    export type AxiosResponse<T = never> = Promise<T>
}
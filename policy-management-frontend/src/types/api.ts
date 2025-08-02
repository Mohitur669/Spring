export interface ApiResponse<T> {
    success: boolean;
    message: string;
    data: T;
    error?: ErrorResponse | null;
    timestamp: string;
    requestId?: string;
}

export interface ErrorResponse {
    timestamp: string;
    status: number;
    error: string;
    message: string;
    path: string;
    errorCode?: string;
    fieldErrors?: Record<string, string[]>;
    validationErrors?: Record<string, string>;
    traceId?: string;
}

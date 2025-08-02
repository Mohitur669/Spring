// services/policyService.ts
import { api } from './api';
import type { Policy, CreatePolicyRequest } from '../types/policy';
import type { ApiResponse } from '../types/api';

export const policyService = {
    async createPolicy(req: CreatePolicyRequest): Promise<Policy> {
        const response = await api.post<ApiResponse<Policy>>('/policies', req);
        return response.data.data;
    },

    async getAllPolicies(): Promise<Policy[]> {
        const response = await api.get<ApiResponse<Policy[]>>('/policies');
        return response.data.data;
    },

    async getPolicyByNumber(num: string): Promise<Policy> {
        const response = await api.get<ApiResponse<Policy>>(`/policies/${num}`);
        return response.data.data;
    },
};

import { api } from './api';
import type { Member, CreateMemberRequest } from '../types/member';

export const memberService = {
    async addMember(policyId: number, req: CreateMemberRequest): Promise<Member> {
        return (await api.post(`/members/policy/${policyId}`, req)).data.data;
    },
    async getMembers(policyId: number): Promise<Member[]> {
        return (await api.get(`/members/policy/${policyId}`)).data.data;
    },
};

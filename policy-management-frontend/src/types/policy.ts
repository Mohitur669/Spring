import type { Member } from './member';

export interface Policy {
    id: number;
    policyNumber: string;
    policyType: string;
    premiumAmount: number;
    startDate: string;
    endDate: string;
    status: PolicyStatus;
    members: Member[];
    createdAt: string;
    updatedAt: string;
}

export interface CreatePolicyRequest {
    policyType: string;
    premiumAmount: number;
    startDate: string;
    endDate: string;
}

export const PolicyStatus = {
    ACTIVE: 'ACTIVE',
    INACTIVE: 'INACTIVE',
    EXPIRED: 'EXPIRED',
    CANCELLED: 'CANCELLED',
} as const;

export type PolicyStatus = typeof PolicyStatus[keyof typeof PolicyStatus];

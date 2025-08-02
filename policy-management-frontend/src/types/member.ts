/*  types/member.ts  */

/** Runtime object */
export const MEMBER_TYPE = {
    PRIMARY: 'PRIMARY',
    SPOUSE: 'SPOUSE',
    DEPENDENT: 'DEPENDENT',
    BENEFICIARY: 'BENEFICIARY',
    CHILD: 'CHILD',
} as const;

/** String-literal union type, completely erased at compile time */
export type MemberType = typeof MEMBER_TYPE[keyof typeof MEMBER_TYPE];

/* ---------- domain models ---------- */

export interface Member {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: string;
    dateOfBirth: string;
    memberType: MemberType;
    address?: string;
    occupation?: string;
    emergencyContact?: string;
}

export interface CreateMemberRequest {
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: string;
    dateOfBirth: string;
    memberType: MemberType;
    address?: string;
    occupation?: string;
    emergencyContact?: string;
}

export interface UpdateMemberRequest {
    firstName: string;
    lastName: string;
    phoneNumber: string;
    address?: string;
    occupation?: string;
    emergencyContact?: string;
}

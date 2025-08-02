import { z } from 'zod';
import { MEMBER_TYPE } from '../types/member';  // ← Changed from MemberType

export const policySchema = z.object({
    policyType: z.string().nonempty(),
    premiumAmount: z.number().positive(),
    startDate: z.string().nonempty(),
    endDate: z.string().nonempty(),
}).refine(d => new Date(d.endDate) > new Date(d.startDate), {
    message: 'End date must be after start date',
    path: ['endDate'],
});

export const memberSchema = z.object({
    firstName: z.string().min(1),
    lastName: z.string().min(1),
    email: z.string().email(),
    phoneNumber: z.string().regex(/^[+]?[0-9]{10,15}$/),
    dateOfBirth: z.string(),
    memberType: z.enum([
        MEMBER_TYPE.PRIMARY,
        MEMBER_TYPE.SPOUSE,
        MEMBER_TYPE.DEPENDENT,
        MEMBER_TYPE.BENEFICIARY,
        MEMBER_TYPE.CHILD,
    ]),
    address: z.string().optional(),
    occupation: z.string().optional(),
    emergencyContact: z.string().optional(),
});

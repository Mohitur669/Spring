import React from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { MEMBER_TYPE } from '../../types/member';  // ← Changed from MemberType
import type { CreateMemberRequest } from '../../types/member';
import { memberSchema } from '../../utils/validation';
import { FormField } from './FormField';

interface Props {
    onSubmit: (data: CreateMemberRequest) => void;
    submitting: boolean;
}

export const MemberForm: React.FC<Props> = ({ onSubmit, submitting }) => {
    const { register, handleSubmit, formState: { errors } } =
        useForm<CreateMemberRequest>({ resolver: zodResolver(memberSchema) });

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <FormField label="First Name *" register={register('firstName')}
                error={errors.firstName} />
            <FormField label="Last Name *" register={register('lastName')}
                error={errors.lastName} />
            <FormField label="Email *" type="email"
                register={register('email')}
                error={errors.email} />
            <FormField label="Phone *"
                register={register('phoneNumber')}
                error={errors.phoneNumber} />
            <FormField label="Date of Birth *" type="date"
                register={register('dateOfBirth')}
                error={errors.dateOfBirth} />
            <FormField label="Member Type *"
                register={register('memberType')}
                error={errors.memberType}
                options={Object.values(MEMBER_TYPE).map((value) => ({
                    value: value,
                    label: value,
                }))} />
            <FormField label="Address" register={register('address')}
                error={errors.address} />
            <FormField label="Occupation" register={register('occupation')}
                error={errors.occupation} />
            <FormField label="Emergency Contact" register={register('emergencyContact')}
                error={errors.emergencyContact} />
            <button disabled={submitting}
                className="w-full bg-blue-600 text-white py-2 rounded">
                {submitting ? 'Adding…' : 'Add Member'}
            </button>
        </form>
    );
};

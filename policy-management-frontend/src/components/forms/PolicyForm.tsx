import React from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import type { CreatePolicyRequest } from '../../types/policy';
import { policySchema } from '../../utils/validation';
import { FormField } from './FormField';

interface Props {
    defaultValues?: Partial<CreatePolicyRequest>;
    onSubmit: (data: CreatePolicyRequest) => void;
    submitting: boolean;
}

export const PolicyForm: React.FC<Props> = ({ defaultValues, onSubmit, submitting }) => {
    const { register, handleSubmit, formState: { errors } } =
        useForm<CreatePolicyRequest>({
            resolver: zodResolver(policySchema),
            defaultValues,
        });

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <FormField label="Policy Type *"
                register={register('policyType')}
                error={errors.policyType}
                options={[
                    { value: 'LIFE', label: 'Life' },
                    { value: 'HEALTH', label: 'Health' },
                    { value: 'AUTO', label: 'Auto' },
                    { value: 'HOME', label: 'Home' },
                ]} />
            <FormField label="Premium Amount *" type="number"
                register={register('premiumAmount', { valueAsNumber: true })}
                error={errors.premiumAmount} />
            <FormField label="Start Date *" type="date"
                register={register('startDate')}
                error={errors.startDate} />
            <FormField label="End Date *" type="date"
                register={register('endDate')}
                error={errors.endDate} />
            <button disabled={submitting}
                className="w-full bg-blue-600 text-white py-2 rounded">
                {submitting ? 'Saving…' : 'Save'}
            </button>
        </form>
    );
};

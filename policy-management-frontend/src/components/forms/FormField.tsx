import React from 'react';
import type { FieldError, UseFormRegisterReturn } from 'react-hook-form';

interface Props {
    label: string;
    type?: string;
    register: UseFormRegisterReturn;
    error?: FieldError;
    options?: { value: string; label: string }[];
}

export const FormField: React.FC<Props> = ({
    label, type = 'text', register, error, options,
}) => (
    <div>
        <label className="block text-sm font-medium mb-1">{label}</label>
        {options ? (
            <select {...register}
                className="w-full border rounded p-2">
                <option value="">Select</option>
                {options.map(o => (
                    <option key={o.value} value={o.value}>{o.label}</option>
                ))}
            </select>
        ) : (
            <input type={type} {...register}
                className="w-full border rounded p-2" />
        )}
        {error && <p className="text-red-600 text-xs mt-1">{error.message}</p>}
    </div>
);

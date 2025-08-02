import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { PolicyForm } from '../components/forms/PolicyForm';
import type { CreatePolicyRequest } from '../types/policy';
import { policyService } from '../services/policyService';

export const CreatePolicy: React.FC = () => {
    const [saving, setSaving] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (data: CreatePolicyRequest) => {
        setSaving(true);
        const policy = await policyService.createPolicy(data);
        navigate(`/policies/${policy.policyNumber}`);
    };

    return (
        <div>
            <h2 className="text-2xl font-bold mb-4">Create Policy</h2>
            <PolicyForm onSubmit={handleSubmit} submitting={saving} />
        </div>
    );
};

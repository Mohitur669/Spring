// hooks/usePolicy.ts
import { useState, useCallback } from 'react';
import type { Policy, CreatePolicyRequest } from '../types/policy';
import { policyService } from '../services/policyService';

export const usePolicy = () => {
    const [policies, setPolicies] = useState<Policy[]>([]); // ✅ Always initialize as empty array
    const [policy, setPolicy] = useState<Policy | null>(null);
    const [loading, setLoading] = useState(false);

    const fetchAllPolicies = useCallback(async () => {
        try {
            setLoading(true);
            const data = await policyService.getAllPolicies();
            setPolicies(data || []); // ✅ Handle undefined response
        } catch (error) {
            console.error('Error fetching policies:', error);
            setPolicies([]); // ✅ Set empty array on error
        } finally {
            setLoading(false);
        }
    }, []);

    const fetchPolicy = useCallback(async (num: string) => {
        try {
            setLoading(true);
            const data = await policyService.getPolicyByNumber(num);
            setPolicy(data);
        } catch (error) {
            console.error('Error fetching policy:', error);
            setPolicy(null);
        } finally {
            setLoading(false);
        }
    }, []);

    const createPolicy = useCallback(async (req: CreatePolicyRequest) => {
        try {
            setLoading(true);
            const newPol = await policyService.createPolicy(req);
            return newPol;
        } catch (error) {
            console.error('Error creating policy:', error);
            throw error;
        } finally {
            setLoading(false);
        }
    }, []);

    return {
        policies, policy, loading,
        fetchAllPolicies, fetchPolicy, createPolicy,
    };
};

import { useState } from 'react';
import type { Member } from '../types/member';
import { memberService } from '../services/memberService';

export const useMember = (policyId: number) => {
    const [members, setMembers] = useState<Member[]>([]);
    const [loading, setLoading] = useState(false);

    const fetchMembers = async () => {
        setLoading(true);
        setMembers(await memberService.getMembers(policyId));
        setLoading(false);
    };

    return { members, loading, fetchMembers };
};

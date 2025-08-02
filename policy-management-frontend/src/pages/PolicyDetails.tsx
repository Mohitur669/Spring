import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { usePolicy } from '../hooks/usePolicy';
import { LoadingSpinner } from '../components/common/LoadingSpinner';
import { MemberForm } from '../components/forms/MemberForm';
import type { CreateMemberRequest } from '../types/member';
import { memberService } from '../services/memberService';

export const PolicyDetails: React.FC = () => {
    const { policyNumber } = useParams();
    const { policy, loading, fetchPolicy } = usePolicy(); // Remove parameter
    const [adding, setAdding] = useState(false);

    useEffect(() => {
        if (policyNumber) {
            fetchPolicy(policyNumber);
        }
    }, [policyNumber, fetchPolicy]);

    if (loading || !policy) return <LoadingSpinner />;

    const handleAdd = async (data: CreateMemberRequest) => {
        setAdding(true);
        await memberService.addMember(policy.id, data);
        await fetchPolicy(policyNumber!);
        setAdding(false);
    };

    return (
        <div>
            <h2 className="text-2xl font-bold mb-4">Policy {policy.policyNumber}</h2>
            <p className="mb-4"><b>Status:</b> {policy.status}</p>

            <h3 className="text-xl font-semibold mb-2">Members</h3>
            <table className="min-w-full bg-white rounded shadow mb-6">
                <thead>
                    <tr>
                        {['Name', 'Email', 'Phone', 'Type'].map(h => (
                            <th key={h} className="text-left px-4 py-2 border-b">{h}</th>
                        ))}
                    </tr>
                </thead>
                <tbody>
                    {policy.members.map(m => (
                        <tr key={m.id} className="border-b">
                            <td className="px-4 py-2">{m.firstName} {m.lastName}</td>
                            <td className="px-4 py-2">{m.email}</td>
                            <td className="px-4 py-2">{m.phoneNumber}</td>
                            <td className="px-4 py-2">{m.memberType}</td>
                        </tr>
                    ))}
                </tbody>
            </table>

            <h3 className="text-xl font-semibold mb-3">Add Member</h3>
            <MemberForm onSubmit={handleAdd} submitting={adding} />
        </div>
    );
};

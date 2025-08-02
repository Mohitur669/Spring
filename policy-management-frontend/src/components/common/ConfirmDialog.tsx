import React from 'react';

interface Props {
    title: string;
    message: string;
    onConfirm: () => void;
    onCancel: () => void;
}

export const ConfirmDialog: React.FC<Props> = ({
    title, message, onConfirm, onCancel,
}) => (
    <div className="fixed inset-0 flex items-center justify-center bg-black/40 z-50">
        <div className="bg-white rounded-lg p-6 w-80">
            <h2 className="text-lg font-bold mb-3">{title}</h2>
            <p className="mb-6">{message}</p>
            <div className="flex justify-end space-x-3">
                <button onClick={onCancel}
                    className="px-4 py-2 rounded border border-gray-300">Cancel</button>
                <button onClick={onConfirm}
                    className="px-4 py-2 rounded bg-red-600 text-white">Confirm</button>
            </div>
        </div>
    </div>
);

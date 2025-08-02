import React, { Component } from 'react';
import type { ErrorInfo, ReactNode } from 'react';

interface Props { children: ReactNode }
interface State { hasError: boolean }

export class ErrorBoundary extends Component<Props, State> {
    state: State = { hasError: false };

    static getDerivedStateFromError() { return { hasError: true }; }

    componentDidCatch(err: Error, info: ErrorInfo) {
        console.error('Uncaught error:', err, info);
    }

    render() {
        if (this.state.hasError) {
            return (
                <div className="p-6 text-center text-red-600">
                    <h1 className="text-2xl font-semibold mb-2">Something went wrong.</h1>
                    <button className="bg-blue-600 text-white px-4 py-2 rounded"
                        onClick={() => window.location.reload()}>
                        Reload
                    </button>
                </div>
            );
        }
        return this.props.children;
    }
}

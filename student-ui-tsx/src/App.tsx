import React, { useState } from 'react';

interface Student {
    id?: number;
    roll: number;
    name: string;
    marks: number;
}

const App: React.FC = () => {
    const [addForm, setAddForm] = useState({
        roll: '',
        name: '',
        marks: ''
    });

    const [searchForm, setSearchForm] = useState({
        roll: '',
        name: ''
    });

    const [searchResults, setSearchResults] = useState<Student[]>([]);
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');

    const handleAddFormChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setAddForm(prev => ({ ...prev, [name]: value }));
    };

    const handleSearchFormChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setSearchForm(prev => ({ ...prev, [name]: value }));
    };

    const handleAddStudent = async (e: React.FormEvent) => {
        e.preventDefault();

        const { roll, name, marks } = addForm;

        if (!roll || !name || !marks) {
            setMessage('Please fill all fields');
            return;
        }

        try {
            setLoading(true);
            const studentData = {
                roll: parseInt(roll),
                name,
                marks: parseInt(marks)
            };

            const response = await fetch('/students/createStudentFromAPI', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(studentData)
            });

            if (response.ok) {
                setMessage('Student added successfully!');
                setAddForm({ roll: '', name: '', marks: '' });
            } else {
                const errorText = await response.text();
                throw new Error(`Failed to add student: ${errorText}`);
            }
        } catch (error) {
            console.error('Error adding student:', error);
            setMessage('Error adding student. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    const handleSearchStudent = async (e: React.FormEvent) => {
        e.preventDefault();

        const { roll, name } = searchForm;

        if (!roll && !name) {
            setMessage('Please provide either Roll or Name to search');
            return;
        }

        try {
            setLoading(true);
            const params = new URLSearchParams();
            if (roll) params.append('roll', roll);
            if (name) params.append('name', name);

            const response = await fetch(`/students/findStudents?${params.toString()}`);

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Failed to fetch: ${errorText}`);
            }

            const contentType = response.headers.get('Content-Type');
            if (contentType && contentType.includes('application/json')) {
                const data: Student[] = await response.json();
                console.log('Search results:', data);
                setSearchResults(data);
                setMessage(data.length > 0 ? `Found ${data.length} student(s)` : 'No students found.');
            } else {
                setMessage('Invalid JSON response');
                setSearchResults([]);
            }
        } catch (error) {
            console.error('Error searching students:', error);
            setMessage('Error searching students. Please try again.');
            setSearchResults([]);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gray-100">
            {/* Header */}
            <div className="bg-blue-600 text-white p-4">
                <h1 className="text-3xl font-semibold text-center">Student Management UI</h1>
            </div>

            <div className="container mx-auto p-6 max-w-4xl">
                {/* Add Student Section */}
                <div className="bg-white rounded-lg shadow-md p-6 mb-8">
                    <h2 className="text-2xl font-bold text-gray-800 mb-6">Add Student</h2>

                    <form className="space-y-4" onSubmit={handleAddStudent}>
                        <input
                            type="text"
                            name="roll"
                            placeholder="Roll"
                            value={addForm.roll}
                            onChange={handleAddFormChange}
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                        />

                        <input
                            type="text"
                            name="name"
                            placeholder="Name"
                            value={addForm.name}
                            onChange={handleAddFormChange}
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                        />

                        <input
                            type="number"
                            name="marks"
                            placeholder="Marks"
                            value={addForm.marks}
                            onChange={handleAddFormChange}
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                        />

                        <button
                            type="submit"
                            disabled={loading}
                            className="bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 px-6 rounded-lg transition duration-200 disabled:opacity-50"
                        >
                            {loading ? 'Adding...' : 'Add Student'}
                        </button>
                    </form>
                </div>

                {/* Search Student Section */}
                <div className="bg-white rounded-lg shadow-md p-6 mb-8">
                    <h2 className="text-2xl font-bold text-gray-800 mb-6">Search Student</h2>

                    <form className="space-y-4" onSubmit={handleSearchStudent}>
                        <input
                            type="text"
                            name="roll"
                            placeholder="Search by Roll"
                            value={searchForm.roll}
                            onChange={handleSearchFormChange}
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 outline-none"
                        />

                        <input
                            type="text"
                            name="name"
                            placeholder="Search by Name"
                            value={searchForm.name}
                            onChange={handleSearchFormChange}
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 outline-none"
                        />

                        <button
                            type="submit"
                            disabled={loading}
                            className="bg-green-600 hover:bg-green-700 text-white font-semibold py-3 px-6 rounded-lg transition duration-200 disabled:opacity-50"
                        >
                            {loading ? 'Searching...' : 'Search'}
                        </button>
                    </form>
                </div>

                {/* Message */}
                {message && (
                    <div className={`p-4 rounded-lg mb-6 ${message.includes('Error') || message.includes('Please')
                        ? 'bg-red-100 text-red-700 border border-red-300'
                        : 'bg-green-100 text-green-700 border border-green-300'
                        }`}>
                        {message}
                    </div>
                )}

                {/* Search Results */}
                {searchResults.length > 0 && (
                    <div className="bg-white rounded-lg shadow-md p-6">
                        <h3 className="text-xl font-bold text-gray-800 mb-4">Search Results</h3>
                        <div className="overflow-x-auto">
                            <table className="w-full border-collapse">
                                <thead>
                                    <tr className="bg-gray-50">
                                        <th className="border border-gray-300 px-4 py-3 text-left font-semibold text-gray-700">Roll</th>
                                        <th className="border border-gray-300 px-4 py-3 text-left font-semibold text-gray-700">Name</th>
                                        <th className="border border-gray-300 px-4 py-3 text-left font-semibold text-gray-700">Marks</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {searchResults.map((student, index) => (
                                        <tr key={student.id || index} className="hover:bg-gray-50">
                                            <td className="border border-gray-300 px-4 py-3">{student.roll}</td>
                                            <td className="border border-gray-300 px-4 py-3">{student.name}</td>
                                            <td className="border border-gray-300 px-4 py-3">{student.marks}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default App;
